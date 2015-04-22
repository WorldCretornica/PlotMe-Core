package com.worldcretornica.plotme_core.bukkit;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.worldcretornica.plotme_core.api.IMaterial;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.api.BukkitMaterial;
import com.worldcretornica.plotme_core.bukkit.api.BukkitOfflinePlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import com.worldcretornica.plotme_core.bukkit.listener.BukkitPlotDenyListener;
import com.worldcretornica.plotme_core.bukkit.listener.BukkitPlotListener;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class BukkitServerBridge extends IServerBridge {

    private final PlotMe_CorePlugin plugin;
    private Economy economy;

    public BukkitServerBridge(PlotMe_CorePlugin instance, Logger logger) {
        super(logger);
        plugin = instance;
    }

    @Override
    public IOfflinePlayer getOfflinePlayer(UUID uuid) {
        return new BukkitOfflinePlayer(Bukkit.getOfflinePlayer(uuid));
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
    public void scheduleSyncDelayedTask(Runnable task, int i) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, i);
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
            worldEdit.getWorldEdit().getEventBus().register(new PlotWorldEditListener(plugin));
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
    }

    @Override
    public void setupListeners() {
        PluginManager pm = plugin.getServer().getPluginManager();

        BukkitPlotListener bukkitPlotListener = new BukkitPlotListener(plugin);
        getEventBus().register(bukkitPlotListener);
        pm.registerEvents(bukkitPlotListener, plugin);
        pm.registerEvents(new BukkitPlotDenyListener(plugin), plugin);

    }

    @Override
    public void runTaskAsynchronously(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public void runTaskLaterAsynchronously(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    @Override
    public boolean doesBiomeExist(String name) {

        for (Biome biome : Biome.values()) {
            if (biome.name().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
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
    @Override
    public IPlayer getPlayer(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
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
        return new BukkitOfflinePlayer(offlinePlayer);
    }

    protected IPlayer internalGetPlayer(String name) {

    }

    @Override
    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    @Override
    public boolean has(IPlayer player, double price) {
        return getEconomy().has(((BukkitOfflinePlayer) player).getOfflinePlayer(), price);
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

/*
    @Override
    public boolean createPlotWorld(String worldName, String generator, Map<String, String> args) {
        //Get a seed
        long seed = new Random().nextLong();

        //Check if we have multiverse
        if (getMultiverse() == null && Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
            setMultiverse((JavaPlugin) Bukkit.getPluginManager().getPlugin("Multiverse-Core"));
        }

        //Do we have one of them
        if (getMultiverse() == null) {
            getLogger().info(plugin.getAPI().getUtil().C("ErrWorldPluginNotFound"));
            return false;
        }

        //Find generator
       */
/* IPlotMe_ChunkGenerator plotMeGenerator = plugin.getServerObjectBuilder().getPlotMeGenerator(generator, worldName);

        //Make generator create settings
        if (plotMeGenerator == null) {
            getLogger().info(plugin.getAPI().getUtil().C("ErrCannotFindWorldGen") + " '" + generator + "'");
            return false;
        }
        if (!plotMeGenerator.getManager().createFile(worldName, args)) { //Create the generator configurations
            getLogger().info(plugin.getAPI().getUtil().C("ErrCannotCreateGen1") + " '" + generator + "' " + plugin.getAPI().getUtil()
                    .C("ErrCannotCreateGen2"));
            return false;
        }*//*


        PlotMapInfo tempPlotInfo = new PlotMapInfo(plugin.getAPI(), config, worldName);

        tempPlotInfo.setPlotAutoLimit(Integer.parseInt(args.get("PlotAutoLimit")));
        tempPlotInfo.setDaysToExpiration(Integer.parseInt(args.get("DaysToExpiration")));
        //tempPlotInfo.setAutoLinkPlots(Boolean.parseBoolean(args.get("AutoLinkPlots")));
        tempPlotInfo.setDisableExplosion(Boolean.parseBoolean(args.get("DisableExplosion")));
        tempPlotInfo.setDisableIgnition(Boolean.parseBoolean(args.get("DisableIgnition")));
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
                success = plugin.getServerObjectBuilder().addMultiverseWorld(worldName, String.valueOf(seed), generator);

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
*/

    @Override
    public IMaterial getMaterial(String string) {
        return new BukkitMaterial(Material.valueOf(string));
    }

    public void clearBukkitPlayerMap() {
        plugin.getBukkitPlayerMap().clear();
    }

    public File getWorldFolder() {
        return plugin.getServer().getWorldContainer();
    }

    public ConfigurationSection getDefaultWorld() {
        return org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("default-world.yml"), StandardCharsets.UTF_8));
    }

    @Override
    public List<IOfflinePlayer> getOfflinePlayers() {
        List<IOfflinePlayer> list = new ArrayList<>();
        for (OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
            list.add(new BukkitOfflinePlayer(player));
        }
        return list;
    }

    @Override
    public String addColor(char c, String string) {
        return ChatColor.translateAlternateColorCodes(c, string);
    }
}
