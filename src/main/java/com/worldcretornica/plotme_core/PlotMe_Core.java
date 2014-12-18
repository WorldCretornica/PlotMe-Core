package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IConfigSection;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.utils.Util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlotMe_Core {

    public static final String CAPTION_FILE = "captions.yml";
    public static final String WORLDS_CONFIG_SECTION = "worlds";
    private static final String DEFAULT_GENERATOR_URL = "http://dev.bukkit.org/bukkit-plugins/plotme/";
    //Bridge
    private final IServerBridge serverBridge;
    public Map<String, Map<String, String>> creationbuffer;
    private IWorld worldcurrentlyprocessingexpired;
    private int counterexpired;
    //Spool stuff
    private ConcurrentLinkedQueue<PlotToClear> plotsToClear;
    //Global variables
    private PlotMeCoreManager plotMeCoreManager;
    private SqlManager sqlManager;
    private Util util;

    public PlotMe_Core(IServerBridge serverObjectBuilder) {
        serverBridge = serverObjectBuilder;
    }

    public void disable() {
        getSqlManager().closeConnection();
        serverBridge.unHook();
        plotMeCoreManager.setPlayersIgnoringWELimit(null);
        setWorldCurrentlyProcessingExpired(null);
        creationbuffer = null;
        plotsToClear.clear();
        plotsToClear = null;
    }

    public void enable() {
        setupMySQL();
        setupConfig();
        setupDefaultCaptions();
        setPlotMeCoreManager(new PlotMeCoreManager(this));
        setUtil(new Util(this));
        setupWorlds(); // TODO: Remove concept of pmi so this is not needed
        serverBridge.setupHooks();
        serverBridge.setupCommands();
        serverBridge.setupListeners();
        setupClearSpools();
        getSqlManager().plotConvertToUUIDAsynchronously();
    }

    public void reload() {
        getSqlManager().closeConnection();
        serverBridge.reloadConfig();
        setupConfig();
        reloadCaptionConfig();
        setupDefaultCaptions();
        setupMySQL();
    }

    public Logger getLogger() {
        return serverBridge.getLogger();
    }

    private void setupConfig() {
        // Get the config we will be working with
        IConfigSection config = serverBridge.getConfig();
        config.set("allowToDeny", null);
        config.set("Language", null);
        config.set("language", null);
        // If no world exists add config for a world
        //if (!config.contains("worlds") || config.contains("worlds") && config.getConfigurationSection("worlds").getKeys(false).isEmpty()) {
        if (!(config.contains(WORLDS_CONFIG_SECTION) && !config.getConfigurationSection(WORLDS_CONFIG_SECTION).getKeys(false).isEmpty())) {
            new PlotMapInfo(this, "plotworld");
        }

        // Do any config validation
        if (config.getInt("NbClearSpools") > 100) {
            getLogger().warning("Having more than 100 clear spools seems drastic, changing to 100");
            config.set("NbClearSpools", 100);
        }

        // Copy new values over
        config.copyDefaults(true);

        config.saveConfig();
    }

    private void setupWorlds() {
        IConfigSection worldsCS = serverBridge.getConfig().getConfigurationSection(WORLDS_CONFIG_SECTION);
        for (String world : worldsCS.getKeys(false)) {
            if (getGenManager(world) == null) {
                getLogger().log(Level.SEVERE, "The world {0} either does not exist or not using a PlotMe generator", world);
                getLogger().log(Level.SEVERE, "Please ensure that {0} is set up and that it is using a PlotMe generator", world);
                getLogger().log(Level.SEVERE, "The default generator can be downloaded from " + DEFAULT_GENERATOR_URL);
            } else {
                PlotMapInfo pmi = new PlotMapInfo(this, world);
                plotMeCoreManager.addPlotMap(world, pmi);
            }
        }
        if (getPlotMeCoreManager().getPlotMaps().isEmpty()) {
            getLogger().severe("Uh oh. There are no plotworlds setup.");
            getLogger().severe("Is that a mistake? Disabling PlotMe to stay safe.");
            getServerBridge().disablePlotMe();
        }
    }

    public IConfigSection getCaptionConfig() {
        return serverBridge.getConfig(CAPTION_FILE);
    }

    public void reloadCaptionConfig() {
        serverBridge.getConfig(CAPTION_FILE).reloadConfig();
    }

    private void setupDefaultCaptions() {
        //Changing Captions File Name
        String pluginsFolder = serverBridge.getDataFolder();
        File coreFolder = new File(pluginsFolder);
        File newCaptionFile = new File(coreFolder, CAPTION_FILE);
        for (String plotMeFiles : coreFolder.list()) {
            if (plotMeFiles.startsWith("caption")) {
                if (CAPTION_FILE.equals(plotMeFiles)) {
                    break;
                } else {
                    File oldCaptionFile = new File(coreFolder, plotMeFiles);
                    if (oldCaptionFile.renameTo(newCaptionFile)) {
                        getLogger().info("Renamed Caption File to captions.yml");
                        if (oldCaptionFile.delete()) {
                            getLogger().info("Deleted old caption file.");
                        } else {
                            getLogger().warning("Failed to delete old caption file. ");
                        }
                    }
                }
            }
        }
    }

    private void setupMySQL() {
        IConfigSection config = serverBridge.getConfig();

        boolean usemySQL = config.getBoolean("usemySQL", false);
        String mySQLconn = config.getString("mySQLconn", "jdbc:mysql://localhost:3306/minecraft");
        String mySQLuname = config.getString("mySQLuname", "root");
        String mySQLpass = config.getString("mySQLpass", "password");

        setSqlManager(new SqlManager(this, usemySQL, mySQLuname, mySQLpass, mySQLconn));
    }

    private void setupClearSpools() {
        creationbuffer = new HashMap<>();
        plotsToClear = new ConcurrentLinkedQueue<>();
    }

    public IPlotMe_GeneratorManager getGenManager(String name) {
        IWorld world = serverBridge.getWorld(name);
        if (world == null) {
            return null;
        } else {
            return PlotMeCoreManager.getGenManager(world);
        }
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
        return counterexpired;
    }

    public void setCounterExpired(int counterexpired) {
        this.counterexpired = counterexpired;
    }

    public void addPlotToClear(PlotToClear plotToClear) {
        plotsToClear.offer(plotToClear);

        Runnable pms = new PlotMeSpool(this, plotToClear);
        serverBridge.scheduleSyncRepeatingTask(pms, 0L, 200L);
    }

    public void removePlotToClear(PlotToClear plotToClear, int taskid) {
        plotsToClear.remove(plotToClear);

        serverBridge.cancelTask(taskid);
    }

    public PlotToClear getPlotLocked(String world, String id) {
        for (PlotToClear ptc : plotsToClear.toArray(new PlotToClear[plotsToClear.size()])) {
            if (ptc.getWorld().equalsIgnoreCase(world) && ptc.getPlotId().equalsIgnoreCase(id)) {
                return ptc;
            }
        }

        return null;
    }

    public PlotMeCoreManager getPlotMeCoreManager() {
        return plotMeCoreManager;
    }

    private void setPlotMeCoreManager(PlotMeCoreManager plotMeCoreManager) {
        this.plotMeCoreManager = plotMeCoreManager;
    }

    public IServerBridge getServerBridge() {
        return serverBridge;
    }

    public SqlManager getSqlManager() {
        return sqlManager;
    }

    private void setSqlManager(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public Util getUtil() {
        return util;
    }

    private void setUtil(Util util) {
        this.util = util;
    }

}
