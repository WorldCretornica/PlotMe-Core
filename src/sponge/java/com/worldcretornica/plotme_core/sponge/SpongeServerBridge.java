package com.worldcretornica.plotme_core.sponge;

import com.worldcretornica.configuration.ConfigAccessor;
import com.worldcretornica.configuration.ConfigurationSection;
import com.worldcretornica.plotme_core.PlotWorldEdit;
import com.worldcretornica.plotme_core.api.IMaterial;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.IEventFactory;
import com.worldcretornica.plotme_core.sponge.api.SpongePlayer;
import com.worldcretornica.plotme_core.sponge.api.SpongeWorld;
import com.worldcretornica.plotme_core.sponge.event.SpongeEventFactory;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class SpongeServerBridge extends IServerBridge {

    private final PlotMe_Sponge plugin;
    private final IEventFactory eventFactory;

    public SpongeServerBridge(PlotMe_Sponge instance) {
        plugin = instance;
        eventFactory = new SpongeEventFactory(plugin.getGame());
    }

    @Override
    public IOfflinePlayer getOfflinePlayer(UUID uuid) {
        return null;
    }

    @Override
    public IOfflinePlayer getOfflinePlayer(String player) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPlayer getPlayer(UUID uuid) {
        Player player = plugin.getGame().getServer().get().getPlayer(uuid).orNull();
        if (player != null) {
            return new SpongePlayer(player);
        } else {
            return null;
        }
    }

    @Override
    public IPlayer getPlayerExact(String name) {
        Player player = plugin.getGame().getServer().get().getPlayer(name).orNull();
        if (player != null) {
            return new SpongePlayer(player);
        } else {
            return null;
        }
    }

    @Override
    public Collection<IPlayer> getOnlinePlayers() {
        Collection<IPlayer> players = new ArrayList<>();

        for (Player player : plugin.getGame().getServer().get().getOnlinePlayers()) {
            players.add(new SpongePlayer(player));
        }
        return players;
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
    public void cancelTask(int taskId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void scheduleSyncDelayedTask(Runnable task, int i) {
        // TODO Auto-generated method stub
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
    public EconomyResponse depositPlayer(IOfflinePlayer currentBidder, double currentBid) {
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
        World world = plugin.getGame().getServer().get().getWorld(worldName).orNull();
        if (world != null) {
            return new SpongeWorld(world);
        } else {
            return null;
        }
    }

    @Override
    public void setupCommands() {
        CommandService cmdService = plugin.getGame().getCommandDispatcher();
        cmdService.register(plugin, new SpongeCommand(plugin), "plotme");

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
    public boolean doesBiomeExist(String name) {
        return true;
    }

    @Override
    public IEventFactory getEventFactory() {
        return eventFactory;
    }

    @Override
    public File getDataFolder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveResource(boolean replace) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<String> getBiomes() {
        List<String> biomes = new ArrayList<>();
        for (BiomeType type : plugin.getGame().getRegistry().getBiomes()) {
            biomes.add(type.getName());
        }
        return biomes;
    }

    @Override
    public Collection<IWorld> getWorlds() {
        Collection<IWorld> worlds = new ArrayList<>();

        for (World world : plugin.getGame().getServer().get().getWorlds()) {
            worlds.add(new SpongeWorld(world));
        }
        return worlds;
    }

/*
    @Override
    public boolean createPlotWorld(String worldName, String generator, Map<String, String> args) {
        // TODO Auto-generated method stub
        return false;
    }
*/

    @Override
    public IMaterial getMaterial(String string) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConfigurationSection loadDefaultConfig(ConfigAccessor configFile, String world) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public File getWorldFolder() {
        return null;
    }

    @Override
    public List<IOfflinePlayer> getOfflinePlayers() {
        return null;
    }

    @Override
    public String addColor(char c, String string) {
        return null;
    }

    @Override
    public void runTaskLaterAsynchronously(Runnable runnable, long delay) {
        // TODO Auto-generated method stub

    }

}
