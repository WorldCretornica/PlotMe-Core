package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IConfigSection;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.utils.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlotMe_Core {

    private static final String LANG_PATH = "Language";
    private static final String DEFAULT_LANG = "english";
    private static final String CAPTIONS_PATTERN = "caption-%s.yml";
    private static final String DEFAULT_GENERATOR_URL = "http://dev.bukkit.org/bukkit-plugins/plotme/";
    //Bridge
    private final IServerBridge serverBridge;
    //Config accessors for language <lang, accessor>
    private final Map<String, IConfigSection> captionsCA = new HashMap<>();
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
        this.worldcurrentlyprocessingexpired = null;
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
        serverBridge.setupListeners();
        serverBridge.setupCommands();
        serverBridge.setupHooks();
        setupClearSpools();
        getSqlManager().plotConvertToUUIDAsynchronously();
    }

    public void reload() {
        getSqlManager().closeConnection();
        serverBridge.reloadConfig();
        setupConfig();
        for (String lang : captionsCA.keySet()) {
            reloadCaptionConfig(lang);
        }
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

        // If no world exists add config for a world
        //if (!config.contains("worlds") || config.contains("worlds") && config.getConfigurationSection("worlds").getKeys(false).isEmpty()) {
        if (!(config.contains("worlds") && !config.getConfigurationSection("worlds").getKeys(false).isEmpty())) {
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
        IConfigSection worldsCS = serverBridge.getConfig().getConfigurationSection("worlds");
        for (String worldname : worldsCS.getKeys(false)) {
            String world = worldname.toLowerCase();
            if (getGenManager(world) == null) {
                getLogger().log(Level.SEVERE, "The world {0} either does not exist or not using a PlotMe generator", world);
                getLogger().log(Level.SEVERE, "Please ensure that {0} is set up and that it is using a PlotMe generator", world);
                getLogger().log(Level.SEVERE, "The default generator can be downloaded from " + DEFAULT_GENERATOR_URL);
            } else {
                PlotMapInfo pmi = new PlotMapInfo(this, world);
                plotMeCoreManager.addPlotMap(world, pmi);
            }
        }
    }

    private String loadCaptionConfig(String language) {
        getLogger().info("method at line 118: " + !captionsCA.containsKey(language));
        if (!captionsCA.containsKey(language)) {
            String configFilename = String.format(CAPTIONS_PATTERN, language);
            IConfigSection ca = serverBridge.getConfig(configFilename);
            captionsCA.put(language, ca);
        }
        if (captionsCA.get(language).getKeys(false).isEmpty()) {
            if (language.equals(DEFAULT_LANG)) {
                setupDefaultCaptions();
            } else {
                getLogger().log(Level.WARNING, "Could not load caption file for {0} or the language file was empty. Using " + DEFAULT_LANG, language);
                return loadCaptionConfig(DEFAULT_LANG);
            }
        }
        return language;
    }

    public IConfigSection getCaptionConfigCA(String language) {
        language = loadCaptionConfig(language);
        return captionsCA.get(language);
    }

    public IConfigSection getCaptionConfig() {
        return getCaptionConfigCA(serverBridge.getConfig().getString(LANG_PATH));
    }

    public void reloadCaptionConfig() {
        reloadCaptionConfig(serverBridge.getConfig().getString(LANG_PATH));
    }

    public void reloadCaptionConfig(String lang) {
        getCaptionConfigCA(lang).reloadConfig();
    }

    private void setupDefaultCaptions() {
        String fileName = String.format(CAPTIONS_PATTERN, DEFAULT_LANG);
        serverBridge.saveResource(fileName, true);
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
