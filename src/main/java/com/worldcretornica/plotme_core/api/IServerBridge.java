package com.worldcretornica.plotme_core.api;

import com.worldcretornica.configuration.ConfigAccessor;
import com.worldcretornica.configuration.ConfigurationSection;
import com.worldcretornica.configuration.file.YamlConfiguration;
import com.worldcretornica.plotme_core.PlotWorldEdit;
import com.worldcretornica.plotme_core.api.event.IEventFactory;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class IServerBridge {

    private boolean usingLwc;

    public abstract IOfflinePlayer getOfflinePlayer(UUID uuid);

    @SuppressWarnings("unused")
    public abstract IOfflinePlayer getOfflinePlayer(String player);

    /**
     * Gets the player from the given UUID.
     *
     * @param uuid UUID of the player to retrieve
     * @return a player if one was found, null otherwise
     */
    public abstract IPlayer getPlayer(UUID uuid);

    public abstract IPlayer getPlayerExact(String name);

    public abstract Collection<IPlayer> getOnlinePlayers();

    public abstract Logger getLogger();

    public abstract int scheduleSyncRepeatingTask(Runnable func, long l, long l2);

    public abstract void cancelTask(int taskId);

    public abstract void scheduleSyncDelayedTask(Runnable task, int i);

    public abstract void setupHooks();

    public abstract Economy getEconomy();

    /**
     * Gets balance of a player
     *
     * @param player of the player
     * @return Amount currently held in players account
     */
    public abstract double getBalance(IPlayer player);

    public abstract EconomyResponse withdrawPlayer(IPlayer player, double price);

    public abstract EconomyResponse depositPlayer(IOfflinePlayer currentBidder, double currentBid);

    public abstract PlotWorldEdit getPlotWorldEdit();

    public boolean isUsingLwc() {
        return usingLwc;
    }

    protected void setUsingLwc(boolean usingLwc) {
        this.usingLwc = usingLwc;
    }

    /**
     * Gets the world with the given name.
     *
     * @param worldName the name of the world
     * @return a world with the given name, or null if none exists
     */
    public abstract IWorld getWorld(String worldName);

    public abstract void setupCommands();

    public abstract void unHook();

    public abstract void setupListeners();

    public abstract void runTaskAsynchronously(Runnable runnable);

    public abstract void runTaskLaterAsynchronously(Runnable runnable, long delay);

    public abstract IBiome getBiome(String name);

    public abstract IEventFactory getEventFactory();

    public abstract File getDataFolder();

    @SuppressWarnings("unused")
    public abstract void saveResource(boolean replace);

    @SuppressWarnings("unused")
    public abstract boolean addMultiverseWorld(String worldName, String seed, String generator);

    public abstract List<String> getBiomes();

    @SuppressWarnings("unused")
    /**
     * Get all Existing Plotworlds.
     * @return all plotworlds on the server
     */
    public abstract Collection<IWorld> getWorlds();

    //public abstract boolean createPlotWorld(String worldName, String generator, Map<String, String> args);

    @SuppressWarnings("unused")
    public abstract IMaterial getMaterial(String string);

    public ConfigurationSection loadDefaultConfig(ConfigAccessor configFile, String world) {
        ConfigurationSection defaultWorld = getDefaultWorld();
        ConfigurationSection configSection;
        if (configFile.getConfig().contains(world)) {
            configSection = configFile.getConfig().getConfigurationSection(world);
        } else {
            configFile.getConfig().set(world, defaultWorld);
            configFile.saveConfig();
            configSection = configFile.getConfig().getConfigurationSection(world);
        }
        for (String path : defaultWorld.getKeys(true)) {
            configSection.addDefault(path, defaultWorld.get(path));
        }
        return configSection;
    }

    public ConfigurationSection getDefaultWorld() {
        return YamlConfiguration
                .loadConfig(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("default-world.yml"), StandardCharsets.UTF_8));
    }

}