package com.worldcretornica.plotme_core;

import com.google.common.eventbus.EventBus;
import com.worldcretornica.configuration.ConfigAccessor;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.storage.Database;
import com.worldcretornica.plotme_core.storage.MySQLConnector;
import com.worldcretornica.plotme_core.storage.SQLiteConnector;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class PlotMe_Core {

    //Bridge
    private final IServerBridge serverBridge;
    private final AbstractSchematicUtil schematicutil;
    private final File pluginFolder;
    private final HashMap<String, IPlotMe_GeneratorManager> managers = new HashMap<>();
    //Spool stuff
    private final ConcurrentLinkedQueue<PlotToClear> plotsToClear = new ConcurrentLinkedQueue<>();
    private IWorld worldcurrentlyprocessingexpired;
    private int counterExpired;
    private Database sqlManager;
    //Caption and Config File.
    private ConfigAccessor configFile;
    private ConfigAccessor captionFile;
    private EventBus eventBus;


    public PlotMe_Core(IServerBridge serverObjectBuilder, AbstractSchematicUtil schematicutil, File pluginFolder) {
        this.serverBridge = serverObjectBuilder;
        this.schematicutil = schematicutil;
        this.pluginFolder = pluginFolder;
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
        setWorldCurrentlyProcessingExpired(null);
        plotsToClear.clear();
        managers.clear();
    }

    public void enable() {
        EventBus plotmeEventBus = new EventBus("PlotMe"); //todo work on new event system
        setEventBus(plotmeEventBus);
        PlotMeCoreManager.getInstance().setPlugin(this);
        configFile = new ConfigAccessor(pluginFolder, "config.yml");
        captionFile = new ConfigAccessor(pluginFolder, "captions.yml");
        setupConfigFiles();
        serverBridge.setupCommands();
        setupSQL();
        serverBridge.setupHooks();
        serverBridge.setupListeners();
        if (getConfig().getBoolean("coreDatabaseUpdate")) {
            getSqlManager().coreDatabaseUpdate();
        }
        //getSqlManager().plotConvertToUUIDAsynchronously();
    }

    public void reload() {
        getSqlManager().closeConnection();
        setupConfigFiles();
        configFile.reloadFile();
        captionFile.reloadFile();
        setupSQL();
        PlotMeCoreManager.getInstance().getPlotMaps().clear();

        for (String worldname : managers.keySet()) {
            setupWorld(worldname.toLowerCase());
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

    private void setupWorld(String world) {
        getServerBridge().loadDefaultConfig(configFile, "worlds." + world);
        configFile.saveConfig();
        PlotMapInfo pmi = new PlotMapInfo(this, configFile, world);
        PlotMeCoreManager.getInstance().addPlotMap(world, pmi);
    }

    public FileConfiguration getCaptionConfig() {
        return captionFile.getConfig();
    }

    /**
     * Setup SQL Database
     */
    private void setupSQL() {
        FileConfiguration config = getConfig();
        boolean fileFound = false;
        if (config.getBoolean("usemySQL", false)) {
            String url = config.getString("mySQLconn");
            String user = config.getString("mySQLuname");
            String pass = config.getString("mySQLpass");
            setSqlManager(new MySQLConnector(this, url, user, pass));
            getSqlManager().createTables();
            getSqlManager().legacyConverter();
            getConfig().set("Verison17DBUpdate", false);
        } else {
            setSqlManager(new SQLiteConnector(this));
            getSqlManager().createTables();
            for (String file : getServerBridge().getDataFolder().list()) {
                if (file.equalsIgnoreCase("plots.db")) {
                    fileFound = true;
                    break;
                } else {
                    fileFound = false;
                }
            }
        }
        getSqlManager().startConnection();
        getSqlManager().createTables();
        if (fileFound) {
            getSqlManager().legacyConverter();
        } else {
            getConfig().set("Verison17DBUpdate", false);
        }
    }

    public void addManager(String world, IPlotMe_GeneratorManager manager) {
        managers.put(world.toLowerCase(), manager);
        setupWorld(world.toLowerCase());
    }

    public IPlotMe_GeneratorManager removeManager(String world) {
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
        getLogger().info("plot to clear add " + plotToClear.getPlotId());
        PlotMeSpool pms = new PlotMeSpool(this, plotToClear);
        pms.setTaskId(serverBridge.scheduleSyncRepeatingTask(pms, 40, 60));
    }

    public void removePlotToClear(PlotToClear plotToClear, int taskId) {
        plotsToClear.remove(plotToClear);

        serverBridge.cancelTask(taskId);
        getLogger().info("removed taskid " + taskId);
    }

    public PlotToClear getPlotLocked(PlotId id) {
        if (plotsToClear.isEmpty()) {
            return null;
        }
        for (PlotToClear ptc : plotsToClear.toArray(new PlotToClear[plotsToClear.size()])) {
            if (ptc.getPlotId().equals(id)) {
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

    public YamlConfiguration getConfig() {
        return configFile.getConfig();
    }

    public String moneyFormat(double price, boolean showSign) {
        if (price == 0) {
            return "";
        }

        String format = String.valueOf(Math.round(Math.abs(price)));

        Economy economy = getServerBridge().getEconomy();

        if (economy != null) {
            if (price <= 1.0 && price >= -1.0) {
                format = format + " " + economy.currencyNameSingular();
            } else {
                format = format + " " + economy.currencyNamePlural();
            }
        }

        if (showSign) {
            if (price > 0.0) {
                return ("+" + format);
            } else {
                return ("-" + format);
            }
        } else {
            return format;
        }
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
