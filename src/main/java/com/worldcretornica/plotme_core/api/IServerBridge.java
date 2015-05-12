package com.worldcretornica.plotme_core.api;

import com.google.common.base.Optional;
import com.worldcretornica.configuration.ConfigAccessor;
import com.worldcretornica.plotme_core.api.event.eventbus.EventBus;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class IServerBridge {

    private final EventBus eventBus = new EventBus();
    private final Logger logger;

    private boolean usingLwc;
    private boolean usingWEdit;

    public IServerBridge(Logger bridgeLogger) {
        logger = bridgeLogger;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public abstract IOfflinePlayer getOfflinePlayer(UUID uuid);

    public abstract IOfflinePlayer getOfflinePlayer(String player);

    /**
     * Gets the player from the given UUID.
     *
     * @param uuid UUID of the player to retrieve
     * @return a player if one was found, null otherwise
     */
    public abstract IPlayer getPlayer(UUID uuid);

    public abstract IPlayer getPlayer(String name);

    public abstract Collection<IPlayer> getOnlinePlayers();

    public Logger getLogger() {
        return logger;
    }

    public abstract int scheduleSyncRepeatingTask(Runnable func, long l, long l2);

    public abstract void cancelTask(int taskId);

    public abstract void scheduleSyncDelayedTask(Runnable task, int i);

    public abstract void setupHooks();

    public abstract Optional<Economy> getEconomy();

    /**
     * Gets balance of a player
     *
     * @param player of the player
     * @param price
     * @return Amount currently held in players account
     */
    public abstract boolean has(IPlayer player, double price);

    public abstract EconomyResponse withdrawPlayer(IPlayer player, double price);

    public abstract EconomyResponse depositPlayer(IOfflinePlayer currentBidder, double currentBid);

    public boolean isUsingLwc() {
        return usingLwc;
    }

    protected void setUsingLwc(boolean usingLwc) {
        this.usingLwc = usingLwc;
    }

    public boolean isUsingWEdit() {
        return usingWEdit;
    }

    public void setUsingWEdit(boolean usingWEdit) {
        this.usingWEdit = usingWEdit;
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

    public abstract boolean doesBiomeExist(String name);

    public abstract File getDataFolder();

    public void saveResource(boolean replace) {
        YamlConfiguration.loadConfiguration(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("default-world.yml"), StandardCharsets.UTF_8));
    }

    public abstract List<String> getBiomes();

    /**
     * Get all Existing Plotworlds.
     * @return all plotworlds on the server
     */
    public abstract Collection<IWorld> getWorlds();

    //public abstract boolean createPlotWorld(String worldName, String generator, Map<String, String> args);

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
        configFile.saveConfig();
        return configSection;
    }

    public abstract ConfigurationSection getDefaultWorld();

    public abstract File getWorldFolder();

    public abstract List<IOfflinePlayer> getOfflinePlayers();

    public abstract String addColor(char c, String string);
}