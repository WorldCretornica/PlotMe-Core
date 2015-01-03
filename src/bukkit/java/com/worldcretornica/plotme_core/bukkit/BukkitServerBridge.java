package com.worldcretornica.plotme_core.bukkit;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotWorldEdit;
import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.api.event.IEventFactory;
import com.worldcretornica.plotme_core.bukkit.MultiWorldWrapper.WorldGeneratorWrapper;
import com.worldcretornica.plotme_core.bukkit.api.*;
import com.worldcretornica.plotme_core.bukkit.event.BukkitEventFactory;
import com.worldcretornica.plotme_core.bukkit.listener.BukkitPlotDenyListener;
import com.worldcretornica.plotme_core.bukkit.listener.BukkitPlotListener;
import com.worldcretornica.plotme_core.bukkit.listener.BukkitPlotWorldEditListener;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class BukkitServerBridge implements IServerBridge {

    private final PlotMe_CorePlugin plugin;
    private final IEventFactory eventfactory;
    private Economy economy;
    private PlotWorldEdit plotworldedit;
    private boolean usinglwc;
    private MultiWorldWrapper multiworld;
    private MultiverseWrapper multiverse;

    public BukkitServerBridge(PlotMe_CorePlugin instance) {
        plugin = instance;
        eventfactory = new BukkitEventFactory();
    }

    private static MultiWorldWrapper getMultiWorldWrapper() {
        if (Bukkit.getPluginManager().isPluginEnabled("MultiWorld")) {
            return new MultiWorldWrapper((JavaPlugin) Bukkit.getPluginManager().getPlugin("MultiWorld"));
        } else {
            return null;
        }
    }

    private static MultiverseWrapper getMultiverseWrapper() {
        if (Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
            return new MultiverseWrapper((JavaPlugin) Bukkit.getPluginManager().getPlugin("Multiverse-Core"));
        } else {
            return null;
        }
    }

    @Override
    public IOfflinePlayer getOfflinePlayer(UUID uuid) {
        return new BukkitOfflinePlayer(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public void reloadConfig() {
        plugin.reloadConfig();
    }

    @Override
    public Logger getLogger() {
        return plugin.getLogger();
    }

    @Override
    public void scheduleSyncRepeatingTask(Runnable func, long l, long l2) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, func, l, l2);
    }

    @Override
    public void cancelTask(int taskid) {
        Bukkit.getScheduler().cancelTask(taskid);
    }

    @Override
    public void scheduleSyncDelayedTask(Runnable task, int i) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, i);
    }

    @Override
    public void setupHooks() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        if (pluginManager.getPlugin("Vault") != null) {
            setupEconomy();
        }

        if (pluginManager.getPlugin("WorldEdit") != null) {

            PlotMe_Core plotMeCore = plugin.getAPI();
            WorldEditPlugin worldEdit = (WorldEditPlugin) pluginManager.getPlugin("WorldEdit");
            PlotWorldEdit we = null;
            try {
                we = new PlotWorldEdit(plotMeCore, worldEdit);
                setPlotWorldEdit(we);
            } catch (SecurityException | IllegalArgumentException unused) {
                getLogger().warning("Unable to hook to WorldEdit properly, please contact the developper of plotme with your WorldEdit version.");
                setPlotWorldEdit(null);
            }

            pluginManager.registerEvents(new BukkitPlotWorldEditListener(plugin, we, plugin), plugin);
        }

        setUsinglwc(pluginManager.getPlugin("LWC") != null);
    }

    @Override
    public Economy getEconomy() {
        return economy;
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
    }

    @Override
    public PlotWorldEdit getPlotWorldEdit() {
        return plotworldedit;
    }

    private void setPlotWorldEdit(PlotWorldEdit plotworldedit) {
        this.plotworldedit = plotworldedit;
    }

    @Override
    public boolean getUsinglwc() {
        return usinglwc;
    }

    public void setUsinglwc(boolean usinglwc) {
        this.usinglwc = usinglwc;
    }

    @Override
    public IWorld getWorld(String name) {
        World world = Bukkit.getWorld(name);
        if (world == null) {
            return null;
        } else {
            return new BukkitWorld(world);
        }
    }

    @Override
    public void setupCommands() {
        plugin.getCommand("plotme").setExecutor(new BukkitCommand(plugin));
    }

    @Override
    public void unHook() {
        economy = null;
        plotworldedit = null;
        usinglwc = false;
    }

    @Override
    public void setupListeners() {
        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new BukkitPlotListener(plugin.getAPI()), plugin);

        pm.registerEvents(new BukkitPlotDenyListener(plugin), plugin);

    }

    @Override
    public void runTaskAsynchronously(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public IBiome getBiome(String name) {
        Biome biome = null;

        for (Biome bio : Biome.values()) {
            if (bio.name().equalsIgnoreCase(name)) {
                biome = bio;
            }
        }

        if (biome == null) {
            return null;
        } else {
            return new BukkitBiome(biome);
        }
    }

    @Override
    public IPlayer getPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        } else {
            return new BukkitPlayer(player);
        }
    }

    /**
     * Gets the player with the given name.
     *
     * @param playerName Player name
     * @return returns a an instance of IPlayer if found, otherwise null
     */
    @Deprecated
    @Override
    public IPlayer getPlayerExact(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            return null;
        } else {
            return new BukkitPlayer(player);
        }
    }

    @Override
    public IOfflinePlayer getOfflinePlayer(String player) {
        @SuppressWarnings("deprecation")
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        if (offlinePlayer == null) {
            return null;
        } else {
            return new BukkitOfflinePlayer(offlinePlayer);
        }
    }

    @Override
    public IEventFactory getEventFactory() {
        return eventfactory;
    }

    @Override
    public InputStream getResource(String path) {
        return plugin.getResource(path);
    }

    @Override
    public String getDataFolder() {
        return plugin.getDataFolder().getAbsolutePath();
    }

    @Override
    public IConfigSection getConfig() {
        return new BukkitConfigSection(plugin);
    }

    @Override
    public IConfigSection getConfig(String file) {

        File configfile = new File(plugin.getDataFolder().getAbsolutePath(), file);
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(configfile);
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            plugin.getLogger().severe("Can't read configuration file");
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            plugin.getLogger().severe("Invalid configuration format");
            e.printStackTrace();
        }

        return new BukkitConfigSection(plugin, config);
    }

    @Override
    public void saveResource(String fileName, boolean replace) {
        plugin.saveResource(fileName, replace);
    }

    @Override
    public IPlotMe_ChunkGenerator getPlotMeGenerator(String pluginname, String worldname) {
        if (Bukkit.getPluginManager().isPluginEnabled(pluginname)) {
            Plugin genplugin = Bukkit.getPluginManager().getPlugin(pluginname);
            if (genplugin != null) {
                ChunkGenerator gen = genplugin.getDefaultWorldGenerator(worldname, "");
                if (gen instanceof IBukkitPlotMe_ChunkGenerator) {
                    return new BukkitPlotMe_ChunkGeneratorBridge((IBukkitPlotMe_ChunkGenerator) gen);
                }
            }
        }
        return null;
    }

    @Override
    public boolean addMultiverseWorld(String worldname, String environment, String seed, String worldtype, boolean bool, String generator) {
        return getMultiverseWrapper().getMVWorldManager().addWorld(worldname, Environment.valueOf(environment), seed, WorldType.valueOf(worldtype), bool, generator);
    }

    @Override
    public double getBalance(IPlayer player) {
        return getEconomy().getBalance(((BukkitOfflinePlayer) player).getOfflinePlayer());
    }

    @Override
    public EconomyResponse withdrawPlayer(IPlayer player, double price) {
        return getEconomy().withdrawPlayer(((BukkitOfflinePlayer) player).getOfflinePlayer(), price);
    }

    @Override
    public EconomyResponse depositPlayer(IOfflinePlayer player, double currentBid) {
        return getEconomy().depositPlayer(((BukkitOfflinePlayer) player).getOfflinePlayer(), currentBid);
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        List<IPlayer> players = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(new BukkitPlayer(player));
        }

        return players;
    }

    @Override
    public List<String> getBiomes() {
        List<String> biomes = new ArrayList<>();
        for (Biome biome : Biome.values()) {
            biomes.add(biome.name());
        }
        return biomes;
    }

    @Override
    public IPlotMe_ChunkGenerator getPlotMeGenerator(String worldname) {
        World world = Bukkit.getWorld(worldname);
        if (world != null) {
            ChunkGenerator generator = world.getGenerator();
            if (generator instanceof IPlotMe_ChunkGenerator) {
                return (IPlotMe_ChunkGenerator) generator;
            }
        }
        return null;
    }

    @Override
    public List<IWorld> getWorlds() {
        List<IWorld> worlds = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            worlds.add(new BukkitWorld(world));
        }

        return worlds;
    }

    @Override
    public boolean createPlotWorld(String worldname, String generator, Map<String, String> args) {
        //Get a seed
        Long seed = new Random().nextLong();

        //Check if we have multiworld
        if (getMultiWorld() == null) {
            if (Bukkit.getPluginManager().isPluginEnabled("MultiWorld")) {
                setMultiworld((JavaPlugin) Bukkit.getPluginManager().getPlugin("MultiWorld"));
            }
        }
        //Check if we have multiverse
        if (getMultiverse() == null) {
            if (Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
                setMultiverse((JavaPlugin) Bukkit.getPluginManager().getPlugin("Multiverse-Core"));
            }
        }

        //Do we have one of them
        if (getMultiWorld() == null && getMultiverse() == null) {
            getLogger().info(plugin.getAPI().getUtil().C("ErrWorldPluginNotFound"));
            return false;
        }

        //Find generator
        IPlotMe_ChunkGenerator plotMeGenerator = plugin.getServerObjectBuilder().getPlotMeGenerator(generator, worldname);

        //Make generator create settings
        if (plotMeGenerator == null) {
            getLogger().info(plugin.getAPI().getUtil().C("ErrCannotFindWorldGen") + " '" + generator + "'");
            return false;
        }
        if (!plotMeGenerator.getManager().createConfig(worldname, args)) { //Create the generator configurations
            getLogger().info(plugin.getAPI().getUtil().C("ErrCannotCreateGen1") + " '" + generator + "' " + plugin.getAPI().getUtil().C("ErrCannotCreateGen2"));
            return false;
        }

        PlotMapInfo tempPlotInfo = new PlotMapInfo(plugin.getAPI(), worldname);

        tempPlotInfo.setPlotAutoLimit(Integer.parseInt(args.get("PlotAutoLimit")));
        tempPlotInfo.setDaysToExpiration(Integer.parseInt(args.get("DaysToExpiration")));
        tempPlotInfo.setAutoLinkPlots(Boolean.parseBoolean(args.get("AutoLinkPlots")));
        tempPlotInfo.setDisableExplosion(Boolean.parseBoolean(args.get("DisableExplosion")));
        tempPlotInfo.setDisableIgnition(Boolean.parseBoolean(args.get("DisableIgnition")));
        tempPlotInfo.setUseProgressiveClear(Boolean.parseBoolean(args.get("UseProgressiveClear")));
        tempPlotInfo.setUseEconomy(Boolean.parseBoolean(args.get("UseEconomy")));
        tempPlotInfo.setCanPutOnSale(Boolean.parseBoolean(args.get("CanPutOnSale")));
        tempPlotInfo.setRefundClaimPriceOnReset(Boolean.parseBoolean(args.get("RefundClaimPriceOnReset")));
        tempPlotInfo.setRefundClaimPriceOnSetOwner(Boolean.parseBoolean(args.get("RefundClaimPriceOnSetOwner")));
        tempPlotInfo.setClaimPrice(Double.parseDouble(args.get("ClaimPrice")));
        tempPlotInfo.setClearPrice(Double.parseDouble(args.get("ClearPrice")));
        tempPlotInfo.setAddPlayerPrice(Double.parseDouble(args.get("AddPlayerPrice")));
        tempPlotInfo.setDenyPlayerPrice(Double.parseDouble(args.get("DenyPlayerPrice")));
        tempPlotInfo.setRemovePlayerPrice(Double.parseDouble(args.get("RemovePlayerPrice")));
        tempPlotInfo.setUndenyPlayerPrice(Double.parseDouble(args.get("UndenyPlayerPrice")));
        tempPlotInfo.setPlotHomePrice(Double.parseDouble(args.get("PlotHomePrice")));
        tempPlotInfo.setSellToPlayerPrice(Double.parseDouble(args.get("SellToPlayerPrice")));
        tempPlotInfo.setBiomeChangePrice(Double.parseDouble(args.get("BiomeChangePrice")));
        tempPlotInfo.setProtectPrice(Double.parseDouble(args.get("ProtectPrice")));
        tempPlotInfo.setDisposePrice(Double.parseDouble(args.get("DisposePrice")));

        plugin.getAPI().getPlotMeCoreManager().addPlotMap(worldname.toLowerCase(), tempPlotInfo);

        //Are we using multiworld?
        if (getMultiWorldWrapper() != null) {
            boolean success = false;
            if (getMultiWorld().isEnabled()) {
                WorldGeneratorWrapper worldGenerator;

                try {
                    worldGenerator = WorldGeneratorWrapper.getGenByName();
                } catch (DelegateClassException ex) {
                    ex.printStackTrace();
                    return false;
                }

                try {
                    success = getMultiWorld().getDataManager().makeWorld(worldname, worldGenerator, seed, generator);
                } catch (DelegateClassException ex) {
                    ex.printStackTrace();
                    return false;
                }

                if (success) {
                    try {
                        getMultiWorld().getDataManager().loadWorld(worldname);
                        getMultiWorld().getDataManager().save();
                    } catch (DelegateClassException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                } else {
                    getLogger().warning(plugin.getAPI().getUtil().C("ErrCannotCreateMW"));
                }
            } else {
                getLogger().warning(plugin.getAPI().getUtil().C("ErrMWDisabled"));
            }
            return success;
        }

        //Are we using multiverse?
        if (getMultiverse() != null) {
            boolean success = false;
            if (getMultiverse().isEnabled()) {
                success = plugin.getServerObjectBuilder().addMultiverseWorld(worldname, "NORMAL", seed.toString(), "NORMAL", true, generator);

                if (!success) {
                    getLogger().info(plugin.getAPI().getUtil().C("ErrCannotCreateMV"));
                }
            } else {
                getLogger().info(plugin.getAPI().getUtil().C("ErrMVDisabled"));
            }
            return success;
        }

        return false;
    }

    private MultiWorldWrapper getMultiWorld() {
        return multiworld;
    }

    private MultiverseWrapper getMultiverse() {
        return multiverse;
    }

    private void setMultiverse(JavaPlugin multiverse) {
        this.multiverse = new MultiverseWrapper(multiverse);
    }

    private void setMultiworld(JavaPlugin multiworld) {
        this.multiworld = new MultiWorldWrapper(multiworld);
    }

    @Override
    public IMaterial getMaterial(String string) {
        return new BukkitMaterial(Material.valueOf(string));
    }

    @Override
    public ILocation createLocation(IWorld world, int x, int y, int z) {
        return world.createLocation(x, y, z);
    }

    @Override
    public IEntityType getEntityType(String string) {
        return new BukkitEntityType(EntityType.valueOf(string));
    }

    @Override
    public IConfigSection loadDefaultConfig(String world) {
        ConfigurationSection defaultCS = getDefaultWorld();
        ConfigurationSection configSection;
        if (plugin.getConfig().contains(world)) {
            configSection = plugin.getConfig().getConfigurationSection(world);
        } else {
            plugin.getConfig().set(world, defaultCS);
            plugin.saveConfig();
            configSection = plugin.getConfig().getConfigurationSection(world);
        }
        for (String path : defaultCS.getKeys(true)) {
            configSection.addDefault(path, defaultCS.get(path));
        }
        return new BukkitConfigSection(plugin, plugin.getConfig(), configSection);
    }

    @Override
    public void disablePlotMe() {
        plugin.getPluginLoader().disablePlugin(plugin);
    }

    private ConfigurationSection getDefaultWorld() {
        InputStream defConfigStream = plugin.getResource("default-world.yml");
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(defConfigStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            isr = new InputStreamReader(defConfigStream);
        }
        return YamlConfiguration.loadConfiguration(isr);
    }
}
