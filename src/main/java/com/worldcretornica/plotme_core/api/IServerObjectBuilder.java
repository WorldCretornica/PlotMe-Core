package com.worldcretornica.plotme_core.api;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import com.worldcretornica.plotme_core.api.event.IEventFactory;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public interface IServerObjectBuilder {

    public IOfflinePlayer getOfflinePlayer(UUID uuid);
    
    public IOfflinePlayer getOfflinePlayer(String player);
    
    public IPlayer getPlayer(UUID uuid);
    
    public IPlayer getPlayerExact(String name);
    
    public void reloadConfig();
    
    public Logger getLogger();
    
    public void scheduleSyncRepeatingTask(Runnable func, long l, long l2);
    
    public void cancelTask(int taskid);

    public void scheduleSyncDelayedTask(Runnable task, int i);
    
    public void setupHooks();
    
    public Economy getEconomy();
    public double getBalance(IOfflinePlayer playerbidder);
    public EconomyResponse withdrawPlayer(IOfflinePlayer p, double price);
    public EconomyResponse depositPlayer(IOfflinePlayer playercurrentbidder, double currentBid);
    
    public PlotWorldEdit getPlotWorldEdit();
    
    public boolean getUsinglwc();

    public String getVersion();

    public IWorld getWorld(String name);
    
    public void sendMessage(ICommandSender cs, String message);
    
    public void setupCommands();

    public void unHook();

    public void setupListeners();

    public void runTaskAsynchronously(Runnable runnable);
    
    public IBiome getBiome(String name);
   
    public IEventFactory getEventFactory();
    
    public InputStream getResource(String path);
    
    public String getDataFolder();
    
    public String getColor(String color);
    
    public IConfigSection getConfig();
    
    public IConfigSection getConfig(String path);

    public void saveResource(String fileName, boolean b);
    
    public IPlotMe_ChunkGenerator getPlotMeGenerator(String pluginname, String worldname);
    
    public IPlotMe_ChunkGenerator getPlotMeGenerator(String worldname);
    
    public boolean addMultiverseWorld(String worldname, String environment, String seed, String worldtype, boolean bool, String generator);

    public List<IPlayer> getOnlinePlayers();

    public List<String> getBiomes();

    public String stripColor(String string);

    public boolean checkWorldName(String string);

    public boolean worldExists(String string);

    public List<IWorld> getWorlds();
    
    public boolean CreatePlotWorld(ICommandSender cs, String worldname, String generator, Map<String, String> args);

    public IMaterial getMaterial(String string);

    public ILocation createLocation(IWorld w, int x, int y, int z);

    public IEntityType getEntityType(String string);

    public IConfigSection getConfig(InputStream defConfigStream);
}
