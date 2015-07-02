package com.worldcretornica.plotme_core;

import com.worldcretornica.configuration.ConfigAccessor;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.eventbus.EventBus;
import com.worldcretornica.plotme_core.bukkit.SchematicUtil;
import com.worldcretornica.plotme_core.storage.Database;
import com.worldcretornica.plotme_core.storage.MySQLConnector;
import com.worldcretornica.plotme_core.storage.SQLiteConnector;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlotMe_Core {

    private final AbstractSchematicUtil schematicutil = new SchematicUtil(this);
    private final HashMap<IWorld, IPlotMe_GeneratorManager> managers = new HashMap<>();
    //Spool stuff
    //private final ConcurrentLinkedQueue<PlotToClear> plotsToClear = new ConcurrentLinkedQueue<>();
    private final EventBus eventBus = new EventBus();
    //Bridge
    private IServerBridge serverBridge;
    private IWorld worldcurrentlyprocessingexpired;
    private int counterExpired;
    private Database sqlManager;
    //Caption and Config File.
    private ConfigAccessor configFile;


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
        //plotsToClear.clear();
        managers.clear();
    }

    public void enable() {
        PlotMeCoreManager.getInstance().setPlugin(this);
        configFile = new ConfigAccessor(getServerBridge().getDataFolder(), "config.yml");
        setupConfigFiles();
        setupSQL();
        serverBridge.setupHooks();
        if (getConfig().getInt("ExpirePlotCleanupTimer") > 0) {
            //20L * 60 = 1 minute in ticks
            serverBridge
                    .runTaskTimerAsynchronously(new PlotExpireCleanup(this), 20L * 60 * 30, 20L * 60 * getConfig().getInt("ExpirePlotCleanupTimer"));
        }
        //getSqlManager().plotConvertToUUIDAsynchronously();
    }

    public void reload() {
        getSqlManager().closeConnection();
        setupConfigFiles();
        configFile.reloadFile();
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
        getConfig().set("Version", "0.17.1");
        // Copy new values over
        getConfig().options().copyDefaults(true);
        configFile.saveConfig();
    }

    private void createConfigs() {
        if (configFile.createFile()) {
            getLogger().info("Created Config File");
        }
    }

    private void setupWorld(IWorld world) {
        getLogger().info("PlotMe is recieving data from the Generator");
        getServerBridge().loadDefaultConfig(configFile, "worlds." + world.getName().toLowerCase());
        PlotMapInfo pmi = new PlotMapInfo(configFile, world.getName().toLowerCase());
        PlotMeCoreManager.getInstance().addPlotMap(world, pmi);
        getSqlManager().loadPlotsAsynchronously(world);
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

    public String C(String caption, Object... args) {
        ResourceBundle captions = ResourceBundle.getBundle("messages");

        String string;
        try {
            string = captions.getString(caption);
        } catch (MissingResourceException ex) {
            return "[Missing caption \"" + caption + "\". Please report this to the author of PlotMe.]";
        }

        return ChatColor.translateAlternateColorCodes('&', MessageFormat.format(string, args));
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

    public void addPlotToClear(Plot plot, ClearReason reason, ICommandSender sender) {
        getLogger().log(Level.INFO, "plot to clear add {0}", plot.getId());
        PlotMeSpool pms = new PlotMeSpool(this, plot, reason, sender);
        pms.setTaskId(serverBridge.scheduleSyncRepeatingTask(pms, 20L * 5, 20L * 10));
    }

    public void removePlotToClear(int taskId) {
        serverBridge.cancelTask(taskId);
        getLogger().log(Level.INFO, "removed taskid {0}", taskId);
    }

    public boolean isPlotLocked(PlotId id) {
/*        if (plotsToClear.isEmpty()) {
            return false;
        }
        for (PlotToClear ptc : plotsToClear) {
            if (ptc.getPlot().getId().equals(id)) {
                return true;
            }
        }*/
        //TODO fix this for 0.17.1

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
