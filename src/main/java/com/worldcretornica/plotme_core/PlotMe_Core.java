package com.worldcretornica.plotme_core;

import com.worldcretornica.configuration.ConfigAccessor;
import com.worldcretornica.configuration.file.FileConfiguration;
import com.worldcretornica.configuration.file.YamlConfiguration;
import com.worldcretornica.plotme_core.api.IConfigSection;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.AbstractSchematicUtil;
import com.worldcretornica.plotme_core.storage.Database;
import com.worldcretornica.plotme_core.storage.MySQLConnector;
import com.worldcretornica.plotme_core.storage.SQLiteConnector;
import com.worldcretornica.plotme_core.utils.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlotMe_Core {

    //Bridge
    private final IServerBridge serverBridge;
    private final AbstractSchematicUtil schematicutil;
    private HashMap<String, IPlotMe_GeneratorManager> managers;
    private IWorld worldcurrentlyprocessingexpired;
    private int counterExpired;
    //Spool stuff
    private ConcurrentLinkedQueue<PlotToClear> plotsToClear;
    //Global variables
    private Database sqlManager;
    private Util util;
    private File configFile;
    private FileConfiguration fileConfiguration;
    private File captionFile;

    public PlotMe_Core(IServerBridge serverObjectBuilder, AbstractSchematicUtil schematicutil) {
        this.serverBridge = serverObjectBuilder;
        this.schematicutil = schematicutil;
        managers = new HashMap<>();
    }

    public IPlotMe_GeneratorManager getGenManager(String name) {
        return managers.get(name.toLowerCase());
    }

    public AbstractSchematicUtil getSchematicUtil() {
        return this.schematicutil;
    }

    public void disable() {
        getSqlManager().closeConnection();
        PlotMeCoreManager.getInstance().getPlotMaps().clear();
        serverBridge.unHook();
        PlotMeCoreManager.getInstance().setPlayersIgnoringWELimit(null);
        setWorldCurrentlyProcessingExpired(null);
        plotsToClear = null;
        managers.clear();
        managers = null;
    }

    public void enable() {
        PlotMeCoreManager.getInstance().setPlugin(this);
        setupSQL();
        configFile = new File(getServerBridge().getDataFolder(), "config.yml");
        captionFile = new File(getServerBridge().getDataFolder(), "captions.yml");
        createCoreConfig();
        setupConfig();
        setupDefaultCaptions();
        serverBridge.setupCommands();
        setUtil(new Util(this));
        serverBridge.setupHooks();
        serverBridge.setupListeners();
        setupClearSpools();
        if (serverBridge.getConfig().getBoolean("setupDatabase")) {

        }
        getSqlManager().startConnection();
        getSqlManager().createTables();
        if (serverBridge.getConfig().getBoolean("coreDatabaseUpdate")) {
            getSqlManager().coreDatabaseUpdate();
        }
        getSqlManager().plotConvertToUUIDAsynchronously();
    }

    public void reload() {
        getSqlManager().closeConnection();
        serverBridge.reloadConfig();
        setupConfig();
        reloadCaptionConfig();
        setupDefaultCaptions();
        setupSQL();
        PlotMeCoreManager.getInstance().getPlotMaps().clear();

        for (String worldname : managers.keySet()) {
            setupWorld(worldname.toLowerCase());
        }
    }

    public Logger getLogger() {
        return serverBridge.getLogger();
    }

    private void setupConfig() {
        // Get the config we will be working with
        FileConfiguration config = getConfig();
        if (config.getConfigurationSection("worlds").getKeys(false).isEmpty()) {
            new PlotMapInfo(this, "plotworld");
        }
        // Do any config validation
        if (config.getInt("NbClearSpools") > 100) {
            getLogger().warning("Having more than 100 clear spools seems drastic, changing to 100");
            config.set("NbClearSpools", 100);
        }

        // Copy new values over
        config.options().copyDefaults(true);
        saveConfiguration();
    }

    private void saveConfiguration() {
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            getLogger().severe("Could not save config to " + configFile);
        }
    }

    private void setupFiles() {
        createCoreConfig();
        createCaptions();
    }

    private void createCaptions() {
        ConfigAccessor config = new ConfigAccessor(this, "captions.yml");
        config.createFile();
    }

    private void createCoreConfig() {
        ConfigAccessor config = new ConfigAccessor(this, "config.yml");
        config.createFile();
    }

    private void setupWorld(String worldname) {
        if (getGenManager(worldname) == null) {
            getLogger().log(Level.SEVERE, "The world {0} either does not exist or not using a PlotMe generator", worldname);
            getLogger().log(Level.SEVERE, "Please ensure that {0} is set up and that it is using a PlotMe generator", worldname);
        } else {
            PlotMapInfo pmi = new PlotMapInfo(this, worldname);
            //Lets just hide a bit of code to clean up the config in here.
            IConfigSection config = getServerBridge().loadDefaultConfig("worlds." + worldname);
            config.set("BottomBlockId", null);
            config.set("AutoLinkPlots", null);
            config.saveConfig();
            PlotMeCoreManager.getInstance().addPlotMap(worldname, pmi);
        }

        if (PlotMeCoreManager.getInstance().getPlotMaps().isEmpty()) {
            getLogger().severe("Uh oh. There are no plotworlds setup.");
            getLogger().severe("Is that a mistake? Try making sure you setup PlotMe Correctly PlotMe to stay safe.");
        }
    }

    public IConfigSection getCaptionConfig() {
        return serverBridge.getCaptionConfig();
    }

    public void reloadCaptionConfig() {
        serverBridge.getCaptionConfig().reloadConfig();
    }

    private void setupDefaultCaptions() {
        if (!captionFile.exists()) {
            new ConfigAccessor(this, "captions.yml").createFile();
            getServerBridge().saveResource(true);
        }
    }

    /**
     * Setup SQL Database
     */
    private void setupSQL() {
        FileConfiguration config = serverBridge.getConfig();
        if (config.getBoolean("usemySQL", false)) {
            String url = config.getString("mySQLconn");
            String user = config.getString("mySQLuname");
            String pass = config.getString("mySQLpass");
            setSqlManager(new MySQLConnector(this, url, user, pass));
        } else {
            setSqlManager(new SQLiteConnector(this));
            getSqlManager().createTables();
        }
    }

    private void setupClearSpools() {
        plotsToClear = new ConcurrentLinkedQueue<>();
    }

    public void addManager(String world, IPlotMe_GeneratorManager manager) {
        managers.put(world.toLowerCase(), manager);
        setupWorld(world.toLowerCase());
    }

    public IPlotMe_GeneratorManager removeManager(String world) {
        return managers.remove(world);
    }

    public void scheduleTask(Runnable task) {
        getLogger().info(util.C("MsgStartDeleteSession"));

        for (int ctr = 0; ctr < 10; ctr++) {
            serverBridge.scheduleSyncDelayedTask(task, ctr * 100);
        }
    }

    public IWorld getWorldCurrentlyProcessingExpired() {
        return worldcurrentlyprocessingexpired;
    }

    public void setWorldCurrentlyProcessingExpired(IWorld worldcurrentlyprocessingexpired) {
        this.worldcurrentlyprocessingexpired = worldcurrentlyprocessingexpired;
    }

    public int getCounterExpired() {
        return counterExpired;
    }

    public void setCounterExpired(int counterExpired) {
        this.counterExpired = counterExpired;
    }

    public void addPlotToClear(PlotToClear plotToClear) {
        plotsToClear.offer(plotToClear);
        getLogger().info("plot to clear add " + plotToClear.getPlotId());
        PlotMeSpool pms = new PlotMeSpool(this, plotToClear);
        pms.setTaskId(serverBridge.scheduleSyncRepeatingTask(pms, 0L, 60L));
    }

    public void removePlotToClear(PlotToClear plotToClear, int taskId) {
        plotsToClear.remove(plotToClear);

        serverBridge.cancelTask(taskId);
        getLogger().info("removed taskid " + taskId);
    }

    public PlotToClear getPlotLocked(IWorld world, PlotId id) {
        if (plotsToClear.isEmpty()) {
            return null;
        }
        for (PlotToClear ptc : plotsToClear.toArray(new PlotToClear[plotsToClear.size()])) {
            if (ptc.getWorld() == world && ptc.getPlotId().equals(id)) {
                return ptc;
            }
        }

        return null;
    }

    public IServerBridge getServerBridge() {
        return serverBridge;
    }

    public Database getSqlManager() {
        return sqlManager;
    }

    private void setSqlManager(Database sqlManager) {
        this.sqlManager = sqlManager;
    }

    public Util getUtil() {
        return util;
    }

    private void setUtil(Util util) {
        this.util = util;
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            reloadConfig();
        }
        return fileConfiguration;
    }

    private void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = getResource("config.yml");
        if (defConfigStream == null) {
            return;
        }
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8));
        fileConfiguration.setDefaults(defConfig);

    }

    private InputStream getResource(String filename) {
        try {
            URL url = getClass().getClassLoader().getResource(filename);
            if (url == null) {
                return null;
            }
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }
}
