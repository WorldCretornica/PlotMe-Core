package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.utils.Util;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlotMe_Core {

    public static final String LANG_PATH = "language";
    public static final String DEFAULT_LANG = "english";
    public static final String CAPTIONS_PATTERN = "caption-%s.yml";
    public static final String DEFAULT_GENERATOR_URL = "http://dev.bukkit.org/bukkit-plugins/plotme/";

    //Config accessors for language <lang, accessor>
    private final Map<String, IConfigSection> captionsCA = new HashMap<>();

    // Worlds that do not have a world generator
    private final Set<String> badWorlds = new HashSet<>();

    private IWorld worldcurrentlyprocessingexpired;
    private ICommandSender cscurrentlyprocessingexpired;
    private Integer counterexpired;
    private Integer nbperdeletionprocessingexpired;

    public Map<String, Map<String, String>> creationbuffer;

    //Spool stuff
    private ConcurrentLinkedQueue<PlotToClear> plotsToClear;

    //Global variables
    private PlotMeCoreManager plotmecoremanager;
    private SqlManager sqlmanager;
    private Util util;
    private Boolean initialized = false;
    
    //Bridge
    private IServerBridge serverBridge;

    public PlotMe_Core(IServerBridge serverObjectBuilder) {
        this.serverBridge = serverObjectBuilder;
    }
    
    public void disable() {
        getSqlManager().closeConnection();
        serverBridge.unHook();
        getPlotMeCoreManager().setPlayersIgnoringWELimit(null);
        setWorldCurrentlyProcessingExpired(null);
        setCommandSenderCurrentlyProcessingExpired(null);
        creationbuffer = null;
        plotsToClear.clear();
        plotsToClear = null;
        initialized = null;

        if (creationbuffer != null) {
            creationbuffer.clear();
            creationbuffer = null;
        }
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
        initialized = true;
        sqlmanager.plotConvertToUUIDAsynchronously();
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
    
    public IServerBridge getServerBridge() {
        return serverBridge;
    }
    
    public Logger getLogger() {
        return getServerBridge().getLogger();
    }
    
    public String getName() {
        return "PlotMe-Core";
    }

    private void setupConfig() {
        // Get the config we will be working with
        final IConfigSection config = getServerBridge().getConfig();

        // Move old configs to new locations
        config.set(LANG_PATH, config.getString("Language"));

        // Delete old configs
        config.set("Language", null);

        // If no world exists add config for a world
        if (!config.contains("worlds") || config.getConfigurationSection("worlds").getKeys(false).isEmpty()) {
            new PlotMapInfo(this, "plotworld");
        }

        // Do any config validation
        if (config.getInt("NbClearSpools") > 100) {
            getLogger().warning("Having more than 100 clear spools seems drastic, changing to 100");
            config.set("NbClearSpools", 100);
        }

        // Load config-old.yml
        // config-old.yml should be used to import settings from by DefaultGenerator
        final IConfigSection oldConfig = getServerBridge().getConfig("config-old.yml");

        if (oldConfig != null) {
            // Create a list of old world configs that should be moved to config-old.yml
            final Collection<String> oldWorldConfigs = new HashSet<>();
            oldWorldConfigs.add("PathWidth");
            oldWorldConfigs.add("PlotSize");
            oldWorldConfigs.add("XTranslation");
            oldWorldConfigs.add("ZTranslation");
            oldWorldConfigs.add("BottomBlockId");
            oldWorldConfigs.add("WallBlockId");
            oldWorldConfigs.add("PlotFloorBlockId");
            oldWorldConfigs.add("PlotFillingBlockId");
            oldWorldConfigs.add("RoadMainBlockId");
            oldWorldConfigs.add("RoadStripeBlockId");
            oldWorldConfigs.add("RoadHeight");
            oldWorldConfigs.add("ProtectedWallBlockId");
            oldWorldConfigs.add("ForSaleWallBlockId");
            oldWorldConfigs.add("AuctionWallBlockId");
    
            // Copy defaults for all worlds
            IConfigSection worldsCS = config.getConfigurationSection("worlds");
            IConfigSection oldWorldsCS = oldConfig.getConfigurationSection("worlds");
            if (oldWorldsCS == null) {
                oldWorldsCS = oldConfig.createSection("worlds");
            }
            for (String worldname : worldsCS.getKeys(false)) {
                // Get the current config section
                final IConfigSection worldCS = worldsCS.getConfigurationSection(worldname);
    
                // Find old world data an move it to oldConfig
                IConfigSection oldWorldCS = oldWorldsCS.getConfigurationSection(worldname);
                for (String path : oldWorldConfigs) {
                    if (worldCS.contains(path)) {
                        if (oldWorldCS == null) {
                            oldWorldCS = oldWorldsCS.createSection(worldname);
                        }
                        oldWorldCS.set(path, worldCS.get(path));
                        worldCS.set(path, null);
                    }
                }
            }
            
            // Copy new values over
            config.copyDefaults(true);

            // Save the config file back to disk
            if (!oldWorldsCS.getKeys(false).isEmpty()) {
                oldConfig.saveConfig();
            }
        } else {
            // Copy new values over
            config.copyDefaults(true);
        }

        config.saveConfig();
    }

    private void setupWorlds() {
        final IConfigSection worldsCS = getServerBridge().getConfig().getConfigurationSection("worlds");
        for (String worldname : worldsCS.getKeys(false)) {
            getPlotMeCoreManager().addPlotMap(worldname.toLowerCase(), new PlotMapInfo(this, worldname));
            if (getGenManager(worldname) == null) {
                serverBridge.getLogger().log(Level.SEVERE, "The world {0} either does not exist or not using a PlotMe generator", worldname);
                serverBridge.getLogger().log(Level.SEVERE, "Please ensure that {0} is set up and that it is using a PlotMe generator", worldname);
                serverBridge.getLogger().log(Level.SEVERE, "The default generator can be downloaded from " + DEFAULT_GENERATOR_URL);
                badWorlds.add(worldname);
            }
        }
    }

    private String loadCaptionConfig(String lang) {
        if (!captionsCA.containsKey(lang)) {
            String configFilename = String.format(CAPTIONS_PATTERN, lang);
            IConfigSection ca = getServerBridge().getConfig(configFilename);
            captionsCA.put(lang, ca);
        }
        if (captionsCA.get(lang).getKeys(false).isEmpty()) {
            if (lang.equals(DEFAULT_LANG)) {
                setupDefaultCaptions();
            } else {
                serverBridge.getLogger().log(Level.WARNING, "Could not load caption file for {0} or the language file was empty. Using " + DEFAULT_LANG, lang);
                return loadCaptionConfig(DEFAULT_LANG);
            }
        }
        return lang;
    }

    private IConfigSection getCaptionConfigCA(String lang) {
        lang = loadCaptionConfig(lang);
        return captionsCA.get(lang);
    }

    public IConfigSection getCaptionConfig() {
        return getCaptionConfig(getServerBridge().getConfig().getString(LANG_PATH));
    }

    public IConfigSection getCaptionConfig(String lang) {
        return getCaptionConfigCA(lang);
    }

    public void reloadCaptionConfig() {
        reloadCaptionConfig(getServerBridge().getConfig().getString(LANG_PATH));
    }

    public void reloadCaptionConfig(String lang) {
        getCaptionConfigCA(lang).reloadConfig();
    }

    public void saveCaptionConfig() {
        saveCaptionConfig(getServerBridge().getConfig().getString(LANG_PATH));
    }

    public void saveCaptionConfig(String lang) {
        getCaptionConfigCA(lang).saveConfig();
    }

    private void setupDefaultCaptions() {
        String fileName = String.format(CAPTIONS_PATTERN, DEFAULT_LANG);
        getServerBridge().saveResource(fileName, true);
    }

    private void setupMySQL() {
        IConfigSection config = getServerBridge().getConfig();

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

    public boolean cPerms(ICommandSender sender, String node) {
        return sender.hasPermission(node);
    }

    public IPlotMe_GeneratorManager getGenManager(IWorld w) {
        if (w.isPlotMeGenerator()) {
            return w.getGenerator().getManager();
        } else {
            return null;
        }
    }

    public IPlotMe_GeneratorManager getGenManager(String name) {
        IWorld w = serverBridge.getWorld(name);
        if (w == null) {
            return null;
        } else {
            return getGenManager(w);
        }
    }

    public int getPlotLimit(IPlayer p) {
        int max = -2;

        if (p.hasPermission("plotme.limit.*")) {
            return -1;
        } else {
            int maxlimit = 255;
            for (int ctr = 0; ctr < maxlimit; ctr++) {
                if (p.hasPermission("plotme.limit." + ctr)) {
                    max = ctr;
                }
            }

        }

        if (max == -2) {
            if (cPerms(p, "plotme.admin")) {
                return -1;
            } else if (cPerms(p, "plotme.use")) {
                return 1;
            } else {
                return 0;
            }
        }

        return max;
    }

    public String getDate() {
        return getDate(Calendar.getInstance());
    }

    private String getDate(Calendar cal) {
        int imonth = cal.get(Calendar.MONTH) + 1;
        int iday = cal.get(Calendar.DAY_OF_MONTH) + 1;
        String month;

        if (imonth < 10) {
            month = "0" + imonth;
        } else {
            month = "" + imonth;
        }

        String day;
        if (iday < 10) {
            day = "0" + iday;
        } else {
            day = "" + iday;
        }

        return "" + cal.get(Calendar.YEAR) + "-" + month + "-" + day;
    }

    public String getDate(Date expireddate) {
        return expireddate.toString();
    }

    public void scheduleTask(Runnable task, int eachseconds, int howmanytimes) {
        getCommandSenderCurrentlyProcessingExpired().sendMessage(getUtil().C("MsgStartDeleteSession"));

        for (int ctr = 0; ctr < howmanytimes / getNbPerDeletionProcessingExpired(); ctr++) {
            serverBridge.scheduleSyncDelayedTask(task, ctr * eachseconds * 20);
        }
    }

    public String getVersion() {
        return serverBridge.getVersion();
    }

    public Set<String> getBadWorlds() {
        return badWorlds;
    }

    public IWorld getWorldCurrentlyProcessingExpired() {
        return worldcurrentlyprocessingexpired;
    }

    public void setWorldCurrentlyProcessingExpired(IWorld worldcurrentlyprocessingexpired) {
        this.worldcurrentlyprocessingexpired = worldcurrentlyprocessingexpired;
    }

    public Integer getCounterExpired() {
        return counterexpired;
    }

    public void setCounterExpired(Integer counterexpired) {
        this.counterexpired = counterexpired;
    }

    public void addPlotToClear(PlotToClear plotToClear) {
        this.plotsToClear.offer(plotToClear);
        
        PlotMeSpool pms = new PlotMeSpool(this, plotToClear);
        serverBridge.scheduleSyncRepeatingTask(pms, 0L, 200L);
    }
    
    public void removePlotToClear(PlotToClear plotToClear, int taskid) {
        this.plotsToClear.remove(plotToClear);

        serverBridge.cancelTask(taskid);
    }

    public boolean isPlotLocked(String world, String id) {
        for (PlotToClear ptc : plotsToClear.toArray(new PlotToClear[0])) {
            if (ptc.getWorld().equalsIgnoreCase(world) && ptc.getPlotId().equalsIgnoreCase(id)) {
                return true;
            }
        }

        return false;
    }

    public PlotToClear getPlotLocked(String world, String id) {
        for (PlotToClear ptc : plotsToClear.toArray(new PlotToClear[0])) {
            if (ptc.getWorld().equalsIgnoreCase(world) && ptc.getPlotId().equalsIgnoreCase(id)) {
                return ptc;
            }
        }

        return null;
    }

    public Integer getNbPerDeletionProcessingExpired() {
        return nbperdeletionprocessingexpired;
    }

    public void setNbPerDeletionProcessingExpired(
            Integer nbperdeletionprocessingexpired) {
        this.nbperdeletionprocessingexpired = nbperdeletionprocessingexpired;
    }

    public ICommandSender getCommandSenderCurrentlyProcessingExpired() {
        return cscurrentlyprocessingexpired;
    }

    public void setCommandSenderCurrentlyProcessingExpired(ICommandSender cscurrentlyprocessingexpired) {
        this.cscurrentlyprocessingexpired = cscurrentlyprocessingexpired;
    }

    public PlotMeCoreManager getPlotMeCoreManager() {
        return plotmecoremanager;
    }

    private void setPlotMeCoreManager(PlotMeCoreManager plotmecoremanager) {
        this.plotmecoremanager = plotmecoremanager;
    }

    public SqlManager getSqlManager() {
        return sqlmanager;
    }

    private void setSqlManager(SqlManager sqlmanager) {
        this.sqlmanager = sqlmanager;
    }

    public Util getUtil() {
        return util;
    }

    private void setUtil(Util util) {
        this.util = util;
    }
    
    public boolean getInitialized() {
        return this.initialized;
    }
}
