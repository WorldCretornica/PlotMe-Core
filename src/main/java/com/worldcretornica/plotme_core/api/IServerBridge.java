package com.worldcretornica.plotme_core.api;

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

    IPlayer getPlayer(UUID uuid);

    IPlayer getPlayerExact(String name);

    List<IPlayer> getOnlinePlayers();

    Logger getLogger();

    void scheduleSyncRepeatingTask(Runnable func, long l, long l2);

    void cancelTask(int taskid);

    void scheduleSyncDelayedTask(Runnable task, int i);

    void setupHooks();

    Economy getEconomy();

    double getBalance(IOfflinePlayer playerbidder);

    EconomyResponse withdrawPlayer(IOfflinePlayer p, double price);

    EconomyResponse depositPlayer(IOfflinePlayer playercurrentbidder, double currentBid);

    PlotWorldEdit getPlotWorldEdit();

    boolean getUsinglwc();

    String getVersion();

    IWorld getWorld(String name);

    void sendMessage(ICommandSender cs, String message);

    void setupCommands();

    void unHook();

    void setupListeners();

    void runTaskAsynchronously(Runnable runnable);

    IBiome getBiome(String name);

    IEventFactory getEventFactory();

    InputStream getResource(String path);

    String getDataFolder();

    String getColor(String color);

    void reloadConfig();

    IConfigSection getConfig();

    IConfigSection getConfig(String file);

    void saveResource(String fileName, boolean b);

    boolean addMultiverseWorld(String worldname, String environment, String seed, String worldtype, boolean bool, String generator);

    List<String> getBiomes();

    String stripColor(String string);

    boolean checkWorldName(String string);

    boolean worldExists(String string);

    List<IWorld> getWorlds();

    boolean CreatePlotWorld(ICommandSender cs, String worldname, String generator, Map<String, String> args);

    IMaterial getMaterial(String string);

    ILocation createLocation(IWorld w, int x, int y, int z);

    IEntityType getEntityType(String string);

    IConfigSection getConfig(InputStream defConfigStream);

    IConfigSection loadDefaultConfig(String string);
}
