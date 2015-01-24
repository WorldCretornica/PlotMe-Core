package com.worldcretornica.plotme_core.sponge;

import com.worldcretornica.plotme_core.PlotWorldEdit;
import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IConfigSection;
import com.worldcretornica.plotme_core.api.IMaterial;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.IEventFactory;
import com.worldcretornica.plotme_core.bukkit.event.BukkitEventFactory;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class SpongeServerBridge extends IServerBridge {

    private final PlotMe_Sponge plugin;
    private final IEventFactory eventFactory;
    
    public SpongeServerBridge(PlotMe_Sponge instance) {
        plugin = instance;
        eventFactory = new BukkitEventFactory();
    }
    
    @Override
    public IOfflinePlayer getOfflinePlayer(UUID uuid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IOfflinePlayer getOfflinePlayer(String player) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPlayer getPlayer(UUID uuid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPlayer getPlayerExact(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Logger getLogger() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int scheduleSyncRepeatingTask(Runnable func, long l, long l2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void cancelTask(int taskid) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int scheduleSyncDelayedTask(Runnable task, int i) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setupHooks() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Economy getEconomy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getBalance(IPlayer player) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public EconomyResponse withdrawPlayer(IPlayer player, double price) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(IOfflinePlayer playercurrentbidder, double currentBid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PlotWorldEdit getPlotWorldEdit() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IWorld getWorld(String worldName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setupCommands() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unHook() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setupListeners() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void runTaskAsynchronously(Runnable runnable) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public IBiome getBiome(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IEventFactory getEventFactory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getResource(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDataFolder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void reloadConfig() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public IConfigSection getConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IConfigSection getConfig(String file) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveResource(String fileName, boolean replace) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean addMultiverseWorld(String worldName, String seed, String generator) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<String> getBiomes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IWorld> getWorlds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean createPlotWorld(String worldName, String generator, Map<String, String> args) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public IMaterial getMaterial(String string) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IConfigSection loadDefaultConfig(String string) {
        // TODO Auto-generated method stub
        return null;
    }

}
