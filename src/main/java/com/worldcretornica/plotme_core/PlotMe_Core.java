package com.worldcretornica.plotme_core;

import com.worldcretornica.configuration.ConfigAccessor;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.eventbus.EventBus;
import com.worldcretornica.plotme_core.bukkit.SchematicUtil;
import com.worldcretornica.plotme_core.storage.Database;
import com.worldcretornica.plotme_core.storage.MySQLConnector;
import com.worldcretornica.plotme_core.storage.SQLiteConnector;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlotMe_Core {

    private final AbstractSchematicUtil schematicutil = new SchematicUtil(this);
    private final HashMap<IWorld, IPlotMe_GeneratorManager> managers = new HashMap<>();
    //Spool stuff
    private final ConcurrentLinkedQueue<PlotToClear> plotsToClear = new ConcurrentLinkedQueue<>();
    //Bridge
    private IServerBridge serverBridge;
    private IWorld worldcurrentlyprocessingexpired;
    private int counterExpired;
    private Database sqlManager;
    //Caption and Config File.
    private ConfigAccessor configFile;
    private ConfigAccessor captionFile;
    private EventBus eventBus = new EventBus();


    public PlotMe_Core() {
    }

    public IPlotMe_GeneratorManager getGenManager(IWorld world) {
        return managers.get(world);
    }

    public AbstractSchematicUtil getSchematicUtil() {
        return this.schematicutil;
    }

    public void registerServerBridge(IServerBridge bridge) {
        serverBridge = bridge;
    }

    public void disable() {
        getSqlManager().closeConnection();
        PlotMeCoreManager.getInstance().getPlotMaps().clear();
        setWorldCurrentlyProcessingExpired(null);
        plotsToClear.clear();
        managers.clear();
    }

    public void enable() {
        PlotMeCoreManager.getInstance().setPlugin(this);
        configFile = new ConfigAccessor(getServerBridge().getDataFolder(), "config.yml");
        captionFile = new ConfigAccessor(getServerBridge().getDataFolder(), "captions.yml");
        setupConfigFiles();
        setupSQL();
        serverBridge.setupHooks();
        //getSqlManager().plotConvertToUUIDAsynchronously();
    }

    public void reload() {
        getSqlManager().closeConnection();
        setupConfigFiles();
        configFile.reloadFile();
        captionFile.reloadFile();
        setupSQL();
        PlotMeCoreManager.getInstance().getPlotMaps().clear();

        for (IWorld world : managers.keySet()) {
            setupWorld(world);
        }
    }

    public Logger getLogger() {
        return serverBridge.getLogger();
    }

    private void setupConfigFiles() {
        createConfigs();
        captionFile.saveConfig();
        // Get the config we will be working with
        FileConfiguration config = getConfig();
        // Do any config validation
        if (config.getInt("NbClearSpools") > 20) {
            getLogger().warning("Having more than 20 clear spools seems drastic, changing to 20");
            config.set("NbClearSpools", 20);
        }
        //Check if the config doesn't have the worlds section. This should happen only if there is no config file for the plugin already.
        if (!config.contains("worlds")) {
            getServerBridge().loadDefaultConfig(configFile, "worlds.plotworld");
        }
        // Copy new values over
        getConfig().options().copyDefaults(true);
        configFile.saveConfig();
    }

    private void createConfigs() {
        if (configFile.createFile()) {
            getLogger().info("Created Config File");
        }
        if (captionFile.createFile()) {
            getLogger().info("Created Caption File");
        }
    }

    private void setupWorld(IWorld world) {
        getServerBridge().loadDefaultConfig(configFile, "worlds." + world.getName().toLowerCase());
        PlotMapInfo pmi = new PlotMapInfo(this, configFile, world);
        PlotMeCoreManager.getInstance().addPlotMap(world, pmi);
        getSqlManager().loadPlotsAsynchronously(world);
    }

    public FileConfiguration getCaptionConfig() {
        return captionFile.getConfig();
    }

    /**
     * Setup SQL Database
     */
    private void setupSQL() {
        FileConfiguration config = getConfig();
        if (config.getBoolean("usemySQL", false)) {
            String url = config.getString("mySQLconn");
            String user = config.getString("mySQLuname");
            String pass = config.getString("mySQLpass");
            setSqlManager(new MySQLConnector(this, url, user, pass));
        } else {
            setSqlManager(new SQLiteConnector(this));
        }
    }

    /**
     * The point where the generator activates PlotMe
     */
    public void addManager(IWorld world, IPlotMe_GeneratorManager manager) {
        managers.put(world, manager);
        setupWorld(world);
    }

    public IPlotMe_GeneratorManager removeManager(IWorld world) {
        return managers.remove(world);
    }

    public void scheduleTask(Runnable task) {
        getLogger().info(this.C("MsgStartDeleteSession"));

        for (int ctr = 0; ctr < 10; ctr++) {
            serverBridge.scheduleSyncDelayedTask(task, ctr * 100);
        }
    }

    public String C(String caption) {
        return addColor(this.getCaptionConfig().getString(caption, "Missing caption: " + caption));
    }

    private String addColor(String string) {
        return getServerBridge().addColor('&', string);
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
        getLogger().log(Level.INFO, "plot to clear add {0}", plotToClear.getPlotId());
        PlotMeSpool pms = new PlotMeSpool(this, plotToClear);
        pms.setTaskId(serverBridge.scheduleSyncRepeatingTask(pms, 40, 60));
    }

    public void removePlotToClear(PlotToClear plotToClear, int taskId) {
        plotsToClear.remove(plotToClear);

        serverBridge.cancelTask(taskId);
        getLogger().log(Level.INFO, "removed taskid {0}", taskId);
    }

    public boolean isPlotLocked(Plot plot) {
        if (plotsToClear.isEmpty()) {
            return false;
        }
        for (PlotToClear ptc : plotsToClear) {
            if (ptc.getPlot().equals(plot)) {
                return true;
            }
        }

        return false;
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

    public YamlConfiguration getConfig() {
        return configFile.getConfig();
    }

    public EventBus getEventBus() {
        return eventBus;
    }

}
