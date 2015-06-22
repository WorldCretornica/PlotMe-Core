package com.worldcretornica.plotme_core.sponge;

import com.google.common.base.Optional;
import com.worldcretornica.configuration.ConfigAccessor;
import com.worldcretornica.plotme_core.api.BridgeLogger;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.sponge.api.SpongePlayer;
import com.worldcretornica.plotme_core.sponge.api.SpongeUser;
import com.worldcretornica.plotme_core.sponge.api.SpongeWorld;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.configuration.ConfigurationSection;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.player.User;
import org.spongepowered.api.service.profile.GameProfileResolver;
import org.spongepowered.api.service.scheduler.SynchronousScheduler;
import org.spongepowered.api.service.user.UserStorage;
import org.spongepowered.api.world.World;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class SpongeServerBridge extends IServerBridge {

    private final PlotMe_Sponge plugin;
    private final BridgeLogger bridgeLogger;

    public SpongeServerBridge(PlotMe_Sponge instance, BridgeLogger bridgeLogger) {
        super(bridgeLogger);
        plugin = instance;
        this.bridgeLogger = bridgeLogger;
    }

    @Override
    public IOfflinePlayer getOfflinePlayer(UUID uuid) {
        Optional<GameProfileResolver> service = plugin.getGame().getServiceManager().provide(GameProfileResolver.class);
        Optional<UserStorage> service2 = plugin.getGame().getServiceManager().provide(UserStorage.class);
        if (service.isPresent() && service2.isPresent()) {
            try {
                Optional<User> userOptional = service2.get().get(service.get().get(uuid).get());
                if (userOptional.isPresent()) {
                    return new SpongeUser(userOptional.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                //I think this is highly unlikely
            }
        }
        return null;
    }

    @Override public IOfflinePlayer getOfflinePlayer(String string) {
        return null;
    }

    @Override
    public IPlayer getPlayer(UUID uuid) {
        Player player = plugin.getGame().getServer().getPlayer(uuid).orNull();
        if (player != null) {
            return new SpongePlayer(player);
        } else {
            return null;
        }
    }

    @Override
    public IPlayer getPlayer(String name) {
        Player player = plugin.getGame().getServer().getPlayer(name).orNull();
        if (player != null) {
            return new SpongePlayer(player);
        } else {
            return null;
        }
    }

    @Override
    public Collection<IPlayer> getOnlinePlayers() {
        Collection<IPlayer> players = new ArrayList<>();

        for (Player player : plugin.getGame().getServer().getOnlinePlayers()) {
            players.add(new SpongePlayer(player));
        }
        return players;
    }

    @Override
    public Logger getLogger() {
        return bridgeLogger;
    }

    @Override public int runTaskTimerAsynchronously(Runnable task, long delay, long period) {
        return 0;
    }

    @Override
    public int scheduleSyncRepeatingTask(Runnable func, long delay, long l2) {
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
    public Optional<Economy> getEconomy() {
        return Optional.absent();
    }

    @Override
    public boolean has(IPlayer player, double price) {
        // TODO Auto-generated method stub
        return false;
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
    public void runTaskAsynchronously(Runnable runnable) {
        // TODO Auto-generated method stub

    }

    @Override
    public File getDataFolder() {
        return plugin.getConfigDir();
    }

    @Override public int runTask(Runnable task) {
        return 0;
    }

    @Override
    public Collection<IWorld> getWorlds() {
        Collection<IWorld> worlds = new ArrayList<>();

        for (World world : plugin.getGame().getServer().getWorlds()) {
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
    public ConfigurationSection loadDefaultConfig(ConfigAccessor configFile, String world) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConfigurationSection getDefaultWorld() {
        return null;
    }

    @Override
    public File getWorldFolder() {
        return null;
    }

    @Override
    public String addColor(char c, String string) {
        return null;
    }

    @Override public void runTaskLater(Runnable runnable, long delay) {
        plugin.getGame().getServiceManager().provide(SynchronousScheduler.class).get().runTaskAfter(plugin,runnable,delay);
    }

    @Override
    public void runTaskLaterAsynchronously(Runnable runnable, long delay) {
        // TODO Auto-generated method stub

    }

}
