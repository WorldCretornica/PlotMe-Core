package com.worldcretornica.plotme_core.api;

import com.google.common.base.Optional;
import com.worldcretornica.configuration.ConfigAccessor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class IServerBridge {

    private final Logger logger;
    private final List<String> biomes = Arrays.asList("Ocean", "Plains", "Desert", "Extreme Hills", "Forest", "Tiaga", "Forest", "Swampland", "River",
            "Hell",
            "The End",
            "FrozenOcean", "FrozenRiver", "Ice Plains", "Ice Mountains", "MushroomIsland", "MushroomIslandShore", "Beach", "DesertHills",
            "ForestHills", "TiagaHills", "Extreme Hills Edge", "Jungle", "JungleHills", "JungleEdge", "Deep Ocean", "Stone Beach", "Cold Beach",
            "Birch Forest", "Birch Forest Hills", "Roofed Forest", "Cold Taiga", "Cold Taiga Hills", "Mega Taiga", "Mega Taiga Hills",
            "Extreme Hills+", "Savanna", "Savanna Plateau", "Mesa", "Mesa Plateau F", "Mesa Plateau");
    private boolean usingLwc;


    public IServerBridge(Logger bridgeLogger) {
        logger = bridgeLogger;
    }

    public abstract IOfflinePlayer getOfflinePlayer(UUID uuid);

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

    /**
     * Gets the world with the given name.
     *
     * @param worldName the name of the world
     * @return a world with the given name, or null if none exists
     */
    public abstract IWorld getWorld(String worldName) {

    }

    public abstract void runTaskAsynchronously(Runnable runnable);

    public abstract void runTaskLaterAsynchronously(Runnable runnable, long delay);

    public boolean getBiome(String name) {
        return biomes.contains(name);
    }

    public abstract File getDataFolder();

    public List<String> getBiomes() {
        return biomes;
    }

    /**
     * Get all Existing Plotworlds.
     * @return all plotworlds on the server
     */
    public abstract Collection<IWorld> getWorlds();

    //public abstract boolean createPlotWorld(String worldName, String generator, Map<String, String> args);

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

    public abstract String addColor(char c, String string);

    public abstract void runTaskLater(Runnable runnable, long delay);
}