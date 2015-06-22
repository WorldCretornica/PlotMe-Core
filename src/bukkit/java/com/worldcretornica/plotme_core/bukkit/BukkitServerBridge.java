package com.worldcretornica.plotme_core.bukkit;

import com.google.common.base.Optional;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.api.BukkitOfflinePlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class BukkitServerBridge extends IServerBridge {

    private final PlotMe_CorePlugin plotMeCorePlugin;

    public BukkitServerBridge(PlotMe_CorePlugin plotMeCorePlugin, Logger logger) {
        super(logger);
        this.plotMeCorePlugin = plotMeCorePlugin;
    }

    @Override
    public IOfflinePlayer getOfflinePlayer(UUID uuid) {
        return new BukkitOfflinePlayer(Bukkit.getOfflinePlayer(uuid));
    }

    @Override public IOfflinePlayer getOfflinePlayer(String string) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(string);
        if (player == null) {
            return null;
        } else {
            return new BukkitOfflinePlayer(player);
        }
    }

    @Override
    public int scheduleSyncRepeatingTask(Runnable func, long delay, long period) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(plotMeCorePlugin, func, delay, period);
    }

    @Override
    public void cancelTask(int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    @Override
    public void scheduleSyncDelayedTask(Runnable task, int i) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plotMeCorePlugin, task, i);
    }

    /**
     * Setup PlotMe plugin hooks
     */
    @Override
    public void setupHooks() {
        PluginManager pluginManager = plotMeCorePlugin.getServer().getPluginManager();
        if (pluginManager.getPlugin("WorldEdit") != null) {
            WorldEditPlugin worldEdit = (WorldEditPlugin) pluginManager.getPlugin("WorldEdit");
            worldEdit.getWorldEdit().getEventBus().register(new PlotWorldEditListener(plotMeCorePlugin.getAPI()));
        }

        setUsingLwc(pluginManager.getPlugin("LWC") != null);
    }

    /**
     * Get Economy from Vault
     * @return
     */
    @Override
    public Optional<Economy> getEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            return Optional.of(economyProvider.getProvider());
        }
        return Optional.absent();
    }

    @Override
    public void runTaskAsynchronously(Runnable runnable) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskAsynchronously(plotMeCorePlugin, runnable);
    }

    @Override
    public void runTaskLaterAsynchronously(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(PlotMe_CorePlugin.getInstance(), runnable, delay);
    }

    @Override
    public IPlayer getPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        } else {
            return PlotMe_CorePlugin.getInstance().wrapPlayer(player);
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
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            return null;
        } else {
            return PlotMe_CorePlugin.getInstance().wrapPlayer(player);
        }
    }

    @Override
    public File getDataFolder() {
        return plotMeCorePlugin.getDataFolder();
    }

    @Override
    public boolean has(IPlayer player, double price) {
        if (getEconomy().isPresent()) {
            return getEconomy().get().has(((BukkitOfflinePlayer) player).getOfflinePlayer(), price);
        } else {
            return false;
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(IPlayer player, double price) {
        if (getEconomy().isPresent()) {
            return getEconomy().get().withdrawPlayer(((BukkitOfflinePlayer) player).getOfflinePlayer(), price);
        } else {
            return null;
        }
    }

    @Override

    public EconomyResponse depositPlayer(IOfflinePlayer player, double price) {
        if (getEconomy().isPresent()) {
            return getEconomy().get().depositPlayer(((BukkitOfflinePlayer) player).getOfflinePlayer(), price);
        } else {
            return null;
        }
    }

    @Override
    public Collection<IPlayer> getOnlinePlayers() {
        Collection<IPlayer> players = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(PlotMe_CorePlugin.getInstance().wrapPlayer(player));
        }

        return players;
    }

    @Override public int runTaskTimerAsynchronously(Runnable task, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plotMeCorePlugin, task, delay, period).getTaskId();
    }

    @Override public int runTask(Runnable task) {
        return Bukkit.getScheduler().runTask(plotMeCorePlugin, task).getTaskId();
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

    public void clearBukkitPlayerMap() {
        PlotMe_CorePlugin.getInstance().getBukkitPlayerMap().clear();
    }

    public File getWorldFolder() {
        return plotMeCorePlugin.getServer().getWorldContainer();
    }

    public ConfigurationSection getDefaultWorld() {
        return org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("default-world.yml"), StandardCharsets.UTF_8));
    }

    @Override public String addColor(char c, String string) {
        return ChatColor.translateAlternateColorCodes(c, string);
    }

    @Override public void runTaskLater(Runnable runnable, long delay) {
        Bukkit.getServer().getScheduler().runTaskLater(plotMeCorePlugin, runnable, delay);
    }
}
