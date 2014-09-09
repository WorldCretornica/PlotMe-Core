package com.worldcretornica.plotme_core;

import com.griefcraft.model.Protection;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.worldcretornica.plotme_core.api.v0_14b.IPlotMe_ChunkGenerator;
import com.worldcretornica.plotme_core.api.v0_14b.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.listener.PlayerListener;
import com.worldcretornica.plotme_core.listener.PlotDenyListener;
import com.worldcretornica.plotme_core.listener.PlotListener;
import com.worldcretornica.plotme_core.listener.PlotWorldEditListener;
import com.worldcretornica.plotme_core.utils.Util;
import me.flungo.bukkit.tools.ConfigAccessor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

public class PlotMe_Core extends JavaPlugin {

    public static final String LANG_PATH = "language";
    public static final String DEFAULT_LANG = "english";
    public static final String CAPTIONS_PATTERN = "caption-%s.yml";
    public static final String DEFAULT_GENERATOR_URL = "http://dev.bukkit.org/bukkit-plugins/plotme/";

    //Config accessors for language <lang, accessor>
    private final HashMap<String, ConfigAccessor> captionsCA = new HashMap<>();

    private Economy economy = null;
    private Boolean usinglwc = false;

    // Worlds that do not have a world generator
    private final Set<String> badWorlds = new HashSet<>();

    private World worldcurrentlyprocessingexpired;
    private CommandSender cscurrentlyprocessingexpired;
    private Integer counterexpired;
    private Integer nbperdeletionprocessingexpired;

    public Map<String, Map<String, String>> creationbuffer = null;

    //Spool stuff
    private ConcurrentLinkedQueue<PlotToClear> plotsToClear = null;

    //Global variables
    private PlotMeCoreManager plotmecoremanager = null;
    private SqlManager sqlmanager = null;
    private PlotWorldEdit plotworldedit = null;
    private Util util = null;

    @Override
    public void onDisable() {
        getSqlManager().closeConnection();
        setEconomy(null);
        setUsinglwc(null);
        getPlotMeCoreManager().setPlayersIgnoringWELimit(null);
        setWorldCurrentlyProcessingExpired(null);
        setCommandSenderCurrentlyProcessingExpired(null);
        creationbuffer = null;
        plotsToClear.clear();
        plotsToClear = null;

        if (creationbuffer != null) {
            creationbuffer.clear();
            creationbuffer = null;
        }
    }

    @Override
    public void onEnable() {
        setupMySQL();
        setupConfig();
        setupDefaultCaptions();
        setPlotMeCoreManager(new PlotMeCoreManager(this));
        setUtil(new Util(this));
        setupWorlds(); // TODO: Remove concept of pmi so this is not needed
        setupListeners();
        setupCommands();
        setupHooks();
        setupClearSpools();
        doMetric();
    }

    public void reload() {
        getSqlManager().closeConnection();
        reloadConfig();
        setupConfig();
        for (String lang : captionsCA.keySet()) {
            reloadCaptionConfig(lang);
        }
        reloadCaptionConfig();
        setupDefaultCaptions();
        setupMySQL();

    }

    private void doMetric() {
        try {
            Metrics metrics = new Metrics(this);

            Graph graphNbWorlds = metrics.createGraph("Plot worlds");

            graphNbWorlds.addPlotter(new Metrics.Plotter("Number of plot worlds") {
                @Override
                public int getValue() {
                    return getPlotMeCoreManager().getPlotMaps().size();
                }
            });

            graphNbWorlds.addPlotter(new Metrics.Plotter("Average Plot size") {
                @Override
                public int getValue() {

                    if (!getPlotMeCoreManager().getPlotMaps().isEmpty()) {
                        int totalplotsize = 0;

                        for (String s : getPlotMeCoreManager().getPlotMaps().keySet()) {
                            if (getPlotMeCoreManager().getGenMan(s) != null) {
                                if (getPlotMeCoreManager().getGenMan(s).getPlotSize(s) != 0) {
                                    totalplotsize += getPlotMeCoreManager().getGenMan(s).getPlotSize(s);
                                }
                            }
                        }

                        return totalplotsize / getPlotMeCoreManager().getPlotMaps().size();
                    } else {
                        return 0;
                    }
                }
            });

            graphNbWorlds.addPlotter(new Metrics.Plotter("Number of plots") {
                @Override
                public int getValue() {
                    int nbplot = 0;

                    for (String map : getPlotMeCoreManager().getPlotMaps().keySet()) {
                        nbplot += getSqlManager().getPlotCount(map);
                    }

                    return nbplot;
                }
            });

            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }

    private void setupConfig() {
        // Get the config we will be working with
        final FileConfiguration config = getConfig();

        // Move old configs to new locations
        config.set(LANG_PATH, config.getString("Language"));

        // Delete old configs
        config.set("Language", null);
        config.set("AdvancedLogging", null);

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
        final ConfigAccessor oldConfCA = new ConfigAccessor(this, "config-old.yml");
        final FileConfiguration oldConfig = oldConfCA.getConfig();

        // Create a list of old world configs that should be moved to config-old.yml
        final Set<String> oldWorldConfigs = new HashSet<>();
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
        ConfigurationSection worldsCS = config.getConfigurationSection("worlds");
        ConfigurationSection oldWorldsCS = oldConfig.getConfigurationSection("worlds");
        if (oldWorldsCS == null) {
            oldWorldsCS = oldConfig.createSection("worlds");
        }
        for (String worldname : worldsCS.getKeys(false)) {
            // Get the current config section
            final ConfigurationSection worldCS = worldsCS.getConfigurationSection(worldname);

            // Find old world data an move it to oldConfig
            ConfigurationSection oldWorldCS = oldWorldsCS.getConfigurationSection(worldname);
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
        config.options().copyDefaults(true);

        // Save the config file back to disk
        if (!oldWorldsCS.getKeys(false).isEmpty()) {
            oldConfCA.saveConfig();
        }
        saveConfig();
    }

    private void setupWorlds() {
        final ConfigurationSection worldsCS = getConfig().getConfigurationSection("worlds");
        for (String worldname : worldsCS.getKeys(false)) {
            getPlotMeCoreManager().addPlotMap(worldname.toLowerCase(), new PlotMapInfo(this, worldname));
            if (getGenManager(worldname) == null) {
                getLogger().log(Level.SEVERE, "The world {0} either does not exist or not using a PlotMe generator", worldname);
                getLogger().log(Level.SEVERE, "Please ensure that {0} is set up and that it is using a PlotMe generator", worldname);
                getLogger().log(Level.SEVERE, "The default generator can be downloaded from " + DEFAULT_GENERATOR_URL);
                badWorlds.add(worldname);
            }
        }
    }

    private String loadCaptionConfig(String lang) {
        if (!captionsCA.containsKey(lang)) {
            String configFilename = String.format(CAPTIONS_PATTERN, lang);
            ConfigAccessor ca = new ConfigAccessor(this, configFilename);
            captionsCA.put(lang, ca);
        }
        if (captionsCA.get(lang).getConfig().getKeys(false).isEmpty()) {
            if (lang.equals(DEFAULT_LANG)) {
                setupDefaultCaptions();
            } else {
                getLogger().log(Level.WARNING, "Could not load caption file for {0}"
                        + " or the language file was empty. Using " + DEFAULT_LANG, lang);
                return loadCaptionConfig(DEFAULT_LANG);
            }
        }
        return lang;
    }

    private ConfigAccessor getCaptionConfigCA(String lang) {
        lang = loadCaptionConfig(lang);
        return captionsCA.get(lang);
    }

    public FileConfiguration getCaptionConfig() {
        return getCaptionConfig(getConfig().getString(LANG_PATH));
    }

    public FileConfiguration getCaptionConfig(String lang) {
        return getCaptionConfigCA(lang).getConfig();
    }

    public void reloadCaptionConfig() {
        reloadCaptionConfig(getConfig().getString(LANG_PATH));
    }

    public void reloadCaptionConfig(String lang) {
        getCaptionConfigCA(lang).reloadConfig();
    }

    public void saveCaptionConfig() {
        saveCaptionConfig(getConfig().getString(LANG_PATH));
    }

    public void saveCaptionConfig(String lang) {
        getCaptionConfigCA(lang).saveConfig();
    }

    private void setupDefaultCaptions() {
        String fileName = String.format(CAPTIONS_PATTERN, DEFAULT_LANG);
        saveResource(fileName, true);
    }

    private void setupMySQL() {
        FileConfiguration config = getConfig();

        boolean usemySQL = config.getBoolean("usemySQL", false);
        String mySQLconn = config.getString("mySQLconn", "jdbc:mysql://localhost:3306/minecraft");
        String mySQLuname = config.getString("mySQLuname", "root");
        String mySQLpass = config.getString("mySQLpass", "password");

        setSqlManager(new SqlManager(this, usemySQL, mySQLuname, mySQLpass, mySQLconn));
    }

    private void setupListeners() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerListener(this), this);

        pm.registerEvents(new PlotListener(this), this);

        if (getConfig().getBoolean("allowToDeny")) {
            pm.registerEvents(new PlotDenyListener(this), this);
        }
    }

    private void setupHooks() {
        PluginManager pm = getServer().getPluginManager();

        if (pm.getPlugin("Vault") != null) {
            setupEconomy();
        }

        if (pm.getPlugin("WorldEdit") != null) {
            setPlotWorldEdit(new PlotWorldEdit(this, (WorldEditPlugin) pm.getPlugin("WorldEdit")));
            pm.registerEvents(new PlotWorldEditListener(this), this);
        }

        if (pm.getPlugin("LWC") != null) {
            setUsinglwc(true);
        }
    }

    private void setupCommands() {
        getCommand("plotme").setExecutor(new PMCommand(this));
    }

    private void setupClearSpools() {
        creationbuffer = new HashMap<>();
        plotsToClear = new ConcurrentLinkedQueue<>();
    }

    public void sendMessage(CommandSender cs, String message) {
        cs.sendMessage(ChatColor.AQUA + "[" + message + "] " + ChatColor.RESET + message);
    }

    public boolean cPerms(CommandSender sender, String node) {
        return sender.hasPermission(node);
    }

    public IPlotMe_GeneratorManager getGenManager(World w) {
        if (w.getGenerator() instanceof IPlotMe_ChunkGenerator) {
            IPlotMe_ChunkGenerator cg = (IPlotMe_ChunkGenerator) w.getGenerator();
            return cg.getManager();
        } else {
            return null;
        }
    }

    public IPlotMe_GeneratorManager getGenManager(String name) {
        World w = getServer().getWorld(name);
        if (w == null) {
            return null;
        } else {
            return getGenManager(getServer().getWorld(name));
        }
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            setEconomy(economyProvider.getProvider());
        }
    }

    public int getPlotLimit(Player p) {
        int max = -2;

        int maxlimit = 255;

        if (p.hasPermission("plotme.limit.*")) {
            return -1;
        } else {
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
        String day;

        if (imonth < 10) {
            month = "0" + imonth;
        } else {
            month = "" + imonth;
        }

        if (iday < 10) {
            day = "0" + iday;
        } else {
            day = "" + iday;
        }

        return "" + cal.get(Calendar.YEAR) + "-" + month + "-" + day;
    }

    public String getDate(java.sql.Date expireddate) {
        return expireddate.toString();
    }

    public void scheduleTask(Runnable task, int eachseconds, int howmanytimes) {
        getCommandSenderCurrentlyProcessingExpired().sendMessage(getUtil().C("MsgStartDeleteSession"));

        for (int ctr = 0; ctr < (howmanytimes / getNbPerDeletionProcessingExpired()); ctr++) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, task, ctr * eachseconds * 20);
        }
    }

    public void scheduleProtectionRemoval(final Location bottom, final Location top) {
        int x1 = bottom.getBlockX();
        int y1 = bottom.getBlockY();
        int z1 = bottom.getBlockZ();
        int x2 = top.getBlockX();
        int y2 = top.getBlockY();
        int z2 = top.getBlockZ();
        World w = bottom.getWorld();

        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                for (int y = y1; y <= y2; y++) {
                    final Block block = w.getBlockAt(x, y, z);

                    Bukkit.getScheduler().runTask(this, new Runnable() {
                        @Override
                        public void run() {
                            Protection protection = com.griefcraft.lwc.LWC.getInstance().findProtection(block);

                            if (protection != null) {
                                protection.remove();
                            }
                        }
                    });
                }
            }
        }
    }

    public String getVersion() {
        return getDescription().getVersion();
    }

    public Set<String> getBadWorlds() {
        return badWorlds;
    }

    public World getWorldCurrentlyProcessingExpired() {
        return worldcurrentlyprocessingexpired;
    }

    public void setWorldCurrentlyProcessingExpired(
            World worldcurrentlyprocessingexpired) {
        this.worldcurrentlyprocessingexpired = worldcurrentlyprocessingexpired;
    }

    public Integer getCounterExpired() {
        return counterexpired;
    }

    public void setCounterExpired(Integer counterexpired) {
        this.counterexpired = counterexpired;
    }

    public Economy getEconomy() {
        return economy;
    }

    private void setEconomy(Economy economy) {
        this.economy = economy;
    }

    public Boolean getUsinglwc() {
        return usinglwc;
    }

    public void setUsinglwc(Boolean usinglwc) {
        this.usinglwc = usinglwc;
    }

    public void addPlotToClear(PlotToClear plotToClear) {
        this.plotsToClear.offer(plotToClear);
        
        PlotMeSpool pms = new PlotMeSpool(this, plotToClear);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, pms, 0L, 200L);
    }
    
    public void removePlotToClear(PlotToClear plotToClear, int taskid) {
        this.plotsToClear.remove(plotToClear);

        Bukkit.getScheduler().cancelTask(taskid);
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

    public CommandSender getCommandSenderCurrentlyProcessingExpired() {
        return cscurrentlyprocessingexpired;
    }

    public void setCommandSenderCurrentlyProcessingExpired(
            CommandSender cscurrentlyprocessingexpired) {
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

    public PlotWorldEdit getPlotWorldEdit() {
        return plotworldedit;
    }

    private void setPlotWorldEdit(PlotWorldEdit plotworldedit) {
        this.plotworldedit = plotworldedit;
    }

    public Util getUtil() {
        return util;
    }

    private void setUtil(Util util) {
        this.util = util;
    }
}
