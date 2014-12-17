package com.worldcretornica.plotme_core.api;

import com.worldcretornica.plotme_core.PlotWorldEdit;
import com.worldcretornica.plotme_core.api.event.IEventFactory;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public interface IServerBridge {

    IPlotMe_ChunkGenerator getPlotMeGenerator(String pluginname, String worldname);

    IPlotMe_ChunkGenerator getPlotMeGenerator(String worldname);

    IOfflinePlayer getOfflinePlayer(UUID uuid);

    IOfflinePlayer getOfflinePlayer(String player);

    /**
     * Gets the player from the given UUID.
     *
     * @param uuid UUID of the player to retrieve
     * @return a player if one was found, null otherwise
     */
    IPlayer getPlayer(UUID uuid);

    IPlayer getPlayerExact(String name);

    List<IPlayer> getOnlinePlayers();

    Logger getLogger();

    void scheduleSyncRepeatingTask(Runnable func, long l, long l2);

    void cancelTask(int taskid);

    void scheduleSyncDelayedTask(Runnable task, int i);

    void setupHooks();

    Economy getEconomy();

    /**
     * Gets balance of a player
     *
     * @param player of the player
     *
     * @return Amount currently held in players account
     */
    double getBalance(IPlayer player);

    EconomyResponse withdrawPlayer(IPlayer player, double price);

    EconomyResponse depositPlayer(IOfflinePlayer playercurrentbidder, double currentBid);

    PlotWorldEdit getPlotWorldEdit();

    boolean getUsinglwc();

    /**
     * Gets the world with the given name.
     *
     * @param name the name of the world to retrieve. Converted to lowercase in Bukkit/Spigot
     *
     * @return a world with the given name, or null if none exists
     */
    IWorld getWorld(String name);

    void setupCommands();

    void unHook();

    void setupListeners();

    void runTaskAsynchronously(Runnable runnable);

    IBiome getBiome(String name);

    IEventFactory getEventFactory();

    InputStream getResource(String path);

    String getDataFolder();

    void reloadConfig();

    IConfigSection getConfig();

    IConfigSection getConfig(String file);

    void saveResource(String fileName, boolean replace);

    boolean addMultiverseWorld(String worldname, String environment, String seed, String worldtype, boolean bool, String generator);

    List<String> getBiomes();

    String stripColor(String string);

    List<IWorld> getWorlds();

    boolean createPlotWorld(String worldname, String generator, Map<String, String> args);

    IMaterial getMaterial(String string);

    ILocation createLocation(IWorld world, int x, int y, int z);

    IEntityType getEntityType(String string);

    IConfigSection loadDefaultConfig(String string);

    void disablePlotMe();
}