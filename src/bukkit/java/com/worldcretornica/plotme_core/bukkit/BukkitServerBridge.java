package com.worldcretornica.plotme_core.bukkit;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotWorldEdit;
import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IConfigSection;
import com.worldcretornica.plotme_core.api.IMaterial;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.IEventFactory;
import com.worldcretornica.plotme_core.bukkit.api.*;
import com.worldcretornica.plotme_core.bukkit.event.*;
import com.worldcretornica.plotme_core.bukkit.listener.*;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

public class BukkitServerBridge extends IServerBridge {

    private final PlotMe_CorePlugin plugin;
    private final IEventFactory eventFactory;
    private Economy economy;
    private PlotWorldEdit plotworldedit;
    private MultiverseWrapper multiverse;

    public BukkitServerBridge(PlotMe_CorePlugin instance) {
        plugin = instance;
        eventFactory = new BukkitEventFactory();
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

    /**
     * PlotMe Logger
     * @return logger
     */
    @Override

    public Logger getLogger() {
        return plugin.getLogger();
    }

    @Override
    public int scheduleSyncRepeatingTask(Runnable func, long l, long l2) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, func, l, l2);
    }

    @Override
    public void cancelTask(int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    @Override
    public int scheduleSyncDelayedTask(Runnable task, int i) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, i);
    }

    /**
     * Setup PlotMe plugin hooks
     */
    @Override
    public void setupHooks() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        if (pluginManager.getPlugin("Vault") != null) {
            setupEconomy();
        }

        if (pluginManager.getPlugin("WorldEdit") != null) {

            WorldEditPlugin worldEdit = (WorldEditPlugin) pluginManager.getPlugin("WorldEdit");
            PlotWorldEdit we = null;
            try {
                we = new PlotWorldEdit(worldEdit);
                setPlotWorldEdit(we);
            } catch (SecurityException | IllegalArgumentException unused) {
                getLogger().warning("Unable to hook to WorldEdit properly, please contact the developer of plotme with your WorldEdit version.");
                setPlotWorldEdit(null);
            }

            pluginManager.registerEvents(new BukkitPlotWorldEditListener(we, plugin), plugin);
        }

        setUsingLwc(pluginManager.getPlugin("LWC") != null);
    }

    /**
     * Get Economy from Vault
     * @return
     */
    @Override
    public Economy getEconomy() {
        return economy;
    }

    /**
     * Register economy with Vault
     */
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
    public IWorld getWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }
        return new BukkitWorld(world);
    }

    @Override
    public void setupCommands() {
        plugin.getCommand("plotme").setExecutor(new BukkitCommand(plugin));
    }

    @Override
    public void unHook() {
        economy = null;
        plotworldedit = null;
    }

    @Override
    public void setupListeners() {
        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new BukkitPlotListener(plugin), plugin);
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
            return plugin.wrapPlayer(player);
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
            return plugin.wrapPlayer(player);
        }
    }

    @Override

    public IOfflinePlayer getOfflinePlayer(String player) {
        @SuppressWarnings("deprecation")
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return new BukkitOfflinePlayer(offlinePlayer);
    }

    @Override

    public IEventFactory getEventFactory() {
        return eventFactory;
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

        File configFile = new File(plugin.getDataFolder().getAbsolutePath(), file);
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(configFile);
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
    public boolean addMultiverseWorld(String worldName, String seed, String generator) {
        return getMultiverseWrapper().getMVWorldManager().addWorld(worldName, seed, generator);
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

    public EconomyResponse depositPlayer(IOfflinePlayer currentBidder, double currentBid) {
        return getEconomy().depositPlayer(((BukkitOfflinePlayer) currentBidder).getOfflinePlayer(), currentBid);
    }

    @Override
    public Collection<IPlayer> getOnlinePlayers() {
        Collection<IPlayer> players = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(plugin.wrapPlayer(player));
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
    public Collection<IWorld> getWorlds() {
        List<IWorld> worlds = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            worlds.add(new BukkitWorld(world));
        }

        return worlds;
    }

    /**
     * Please do not use this method if you need to create a plotworld
     * @param worldName Name of the Plotworld
     * @param generator PlotMe Generator
     * @param args
     * @return
     */
    @Override
    public boolean createPlotWorld(String worldName, String generator, Map<String, String> args) {
        //Get a seed
        Long seed = new Random().nextLong();

        //Check if we have multiverse
        if (getMultiverse() == null) {
            if (Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
                setMultiverse((JavaPlugin) Bukkit.getPluginManager().getPlugin("Multiverse-Core"));
            }
        }

        //Do we have one of them
        if (getMultiverse() == null) {
            getLogger().info(plugin.getAPI().getUtil().C("ErrWorldPluginNotFound"));
            return false;
        }

        //Find generator
       /* IPlotMe_ChunkGenerator plotMeGenerator = plugin.getServerObjectBuilder().getPlotMeGenerator(generator, worldName);

        //Make generator create settings
        if (plotMeGenerator == null) {
            getLogger().info(plugin.getAPI().getUtil().C("ErrCannotFindWorldGen") + " '" + generator + "'");
            return false;
        }
        if (!plotMeGenerator.getManager().createConfig(worldName, args)) { //Create the generator configurations
            getLogger().info(plugin.getAPI().getUtil().C("ErrCannotCreateGen1") + " '" + generator + "' " + plugin.getAPI().getUtil()
                    .C("ErrCannotCreateGen2"));
            return false;
        }*/

        PlotMapInfo tempPlotInfo = new PlotMapInfo(plugin.getAPI(), worldName);

        tempPlotInfo.setPlotAutoLimit(Integer.parseInt(args.get("PlotAutoLimit")));
        tempPlotInfo.setDaysToExpiration(Integer.parseInt(args.get("DaysToExpiration")));
        //tempPlotInfo.setAutoLinkPlots(Boolean.parseBoolean(args.get("AutoLinkPlots")));
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

        PlotMeCoreManager.getInstance().addPlotMap(worldName, tempPlotInfo);

        //Are we using multiverse?
        if (getMultiverse() != null) {
            boolean success = false;
            if (getMultiverse().isEnabled()) {
                success = plugin.getServerObjectBuilder().addMultiverseWorld(worldName, seed.toString(), generator);

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

    private MultiverseWrapper getMultiverse() {
        return multiverse;
    }

    private void setMultiverse(JavaPlugin multiverse) {
        this.multiverse = new MultiverseWrapper(multiverse);
    }

    @Override
    public IMaterial getMaterial(String string) {
        return new BukkitMaterial(Material.valueOf(string));
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

    public void clearBukkitPlayerMap() {
        plugin.getBukkitPlayerMap().clear();
    }
}
