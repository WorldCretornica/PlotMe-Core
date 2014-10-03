package com.worldcretornica.plotme_core.bukkit.api;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.api.event.IEventFactory;
import com.worldcretornica.plotme_core.api.v0_14b.IPlotMe_ChunkGenerator;
import com.worldcretornica.plotme_core.bukkit.*;
import com.worldcretornica.plotme_core.bukkit.event.BukkitEventFactory;
import com.worldcretornica.plotme_core.bukkit.listener.*;
import com.worldcretornica.plotme_core.worldedit.PlotWorldEdit;

public class BukkitServerObjectBuilder implements IServerObjectBuilder {

    private PlotMe_CorePlugin plugin;
    private Economy economy = null;
    private PlotWorldEdit plotworldedit = null;
    private boolean usinglwc = false;
    private IEventFactory eventfactory = null;
    
    public BukkitServerObjectBuilder(PlotMe_CorePlugin instance) {
        plugin = instance;
        eventfactory = new BukkitEventFactory();
    }
    
    @Override
    public IOfflinePlayer getOfflinePlayer(UUID uuid) {
        return new BukkitOfflinePlayer(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public void reloadConfig() {
        plugin.reloadConfig();
    }

    @Override
    public Logger getLogger() {
        return plugin.getLogger();
    }

    @Override
    public void scheduleSyncRepeatingTask(Runnable func, long l, long l2) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, func, l, l2);
    }

    @Override
    public void cancelTask(int taskid) {
        Bukkit.getScheduler().cancelTask(taskid);
    }

    @Override
    public void scheduleSyncDelayedTask(Runnable task, int i) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, i);
    }

    @Override
    public void setupHooks() {
        PluginManager pm = plugin.getServer().getPluginManager();

        if (pm.getPlugin("Vault") != null) {
            setupEconomy();
        }

        if (pm.getPlugin("WorldEdit") != null) {
            try {
                Class.forName("com.sk89q.worldedit.function.mask.Mask");
                setPlotWorldEdit((PlotWorldEdit) Class.forName("com.worldcretornica.plotme_core.worldedit.PlotWorldEdit6_0_0").getConstructor(PlotMe_Core.class, WorldEditPlugin.class).newInstance(this, pm.getPlugin("WorldEdit")));
            } catch (Exception unused) {
                try {
                    setPlotWorldEdit((PlotWorldEdit) Class.forName("com.worldcretornica.plotme_core.worldedit.PlotWorldEdit5_7").getConstructor(PlotMe_Core.class, WorldEditPlugin.class).newInstance(this, pm.getPlugin("WorldEdit")));
                } catch (Exception unused2) {
                    getLogger().warning("Unable to hook to WorldEdit properly, please contact the developper of plotme with your WorldEdit version.");
                    setPlotWorldEdit(null);
                }
            }
            
            pm.registerEvents(new BukkitPlotWorldEditListener(plugin), plugin);
        }

        if (pm.getPlugin("LWC") != null) {
            setUsinglwc(true);
        }
    }

    @Override
    public Economy getEconomy() {
        return economy;
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
    }

    @Override
    public PlotWorldEdit getPlotWorldEdit() {
        return plotworldedit;
    }

    private void setPlotWorldEdit(PlotWorldEdit plotworldedit) {
        this.plotworldedit = plotworldedit;
    }

    public boolean getUsinglwc() {
        return usinglwc;
    }

    public void setUsinglwc(Boolean usinglwc) {
        this.usinglwc = usinglwc;
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public IWorld getWorld(String name) {
        return new BukkitWorld(Bukkit.getWorld(name));
    }

    @Override
    public void sendMessage(ICommandSender cs, String message) {
        cs.sendMessage(ChatColor.AQUA + "[" + message + "] " + ChatColor.RESET + message);
    }

    @Override
    public void setupCommands() {
        plugin.getCommand("plotme").setExecutor(new BukkitCommand(plugin));
    }

    @Override
    public void unHook() {
        economy = null;
    }

    @Override
    public void setupListeners() {
        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new BukkitPlayerListener(plugin), plugin);

        pm.registerEvents(new BukkitPlotListener(plugin), plugin);

        if (plugin.getConfig().getBoolean("allowToDeny")) {
            pm.registerEvents(new BukkitPlotDenyListener(plugin), plugin);
        }
    }

    @Override
    public void runTaskAsynchronously(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public IBiome getBiome(String name) {
        Biome biome = null;
        
        for (Biome bio : Biome.values()) {
            if (bio.name().equalsIgnoreCase(name)) {
                biome = bio;
            }
        }
        
        if (biome == null) {
            return null;
        } else {
            return new BukkitBiome(biome);
        }
    }

    @Override
    public IPlayer getPlayer(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) {
            return null;
        } else {
            return new BukkitPlayer(p); 
        }
    }

    @Override
    public IPlayer getPlayerExact(String name) {
        @SuppressWarnings("deprecation")
        Player p = Bukkit.getPlayerExact(name);
        if (p == null) {
            return null;
        } else {
            return new BukkitPlayer(p); 
        }
    }

    @Override
    public IOfflinePlayer getOfflinePlayer(String player) {
        @SuppressWarnings("deprecation")
        OfflinePlayer op = Bukkit.getOfflinePlayer(player);
        if (op != null) {
            return new BukkitOfflinePlayer(op);
        } else {
            return null;
        }
    }

    @Override
    public IEventFactory getEventFactory() {
        return eventfactory;
    }

    @Override
    public InputStream getResource(String path) {
        return plugin.getResource(path);
    }

    @Override
    public String getDataFolder() {
        return plugin.getDataFolder().getAbsolutePath();
    }

    @Override
    public String getColor(String color) {
        return ChatColor.valueOf(color).toString();
    }

    @Override
    public IConfigSection getConfig() {
        return new BukkitConfigSection(plugin);
    }

    @Override
    public IConfigSection getConfig(String path) {
        return new BukkitConfigSection(plugin).getConfigurationSection(path);
    }

    @Override
    public void saveResource(String fileName, boolean b) {
        plugin.saveResource(fileName, b);
    }

    @Override
    public MultiWorldWrapper getMultiWorldWrapper() {
        if (Bukkit.getPluginManager().isPluginEnabled("MultiWorld")) {
            return new MultiWorldWrapper((JavaPlugin) Bukkit.getPluginManager().getPlugin("MultiWorld"));
        } else {
            return null;
        }
    }

    @Override
    public MultiverseWrapper getMultiverseWrapper() {
        if (Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
            return new MultiverseWrapper((JavaPlugin) Bukkit.getPluginManager().getPlugin("Multiverse-Core"));
        } else {
            return null;
        }
    }

    @Override
    public IPlotMe_ChunkGenerator getPlotMeGenerator(String pluginname, String worldname) {
        if (Bukkit.getPluginManager().isPluginEnabled(pluginname)) {
            JavaPlugin genplugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin(pluginname);
            if (genplugin != null) {
                ChunkGenerator gen = genplugin.getDefaultWorldGenerator(worldname, "");
                if (gen != null && gen instanceof IPlotMe_ChunkGenerator) {
                    return (IPlotMe_ChunkGenerator) gen;
                }
            }
        }
        return null;
    }

    @Override
    public boolean addMultiverseWorld(String worldname, String environment, String seed, String worldtype, boolean bool, String generator) {
        return getMultiverseWrapper().getMVWorldManager().addWorld(worldname, Environment.valueOf(environment), seed, WorldType.valueOf(worldtype), bool, generator);
    }

    @Override
    public double getBalance(IOfflinePlayer playerbidder) {
        return getEconomy().getBalance(((BukkitOfflinePlayer) playerbidder).getOfflinePlayer());
    }

    @Override
    public EconomyResponse withdrawPlayer(IOfflinePlayer p, double price) {
        return getEconomy().withdrawPlayer(((BukkitOfflinePlayer) p).getOfflinePlayer(), price);
    }

    @Override
    public EconomyResponse depositPlayer(IOfflinePlayer playercurrentbidder, double currentBid) {
        return getEconomy().depositPlayer(((BukkitOfflinePlayer) playercurrentbidder).getOfflinePlayer(), currentBid);
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        List<IPlayer> players = new ArrayList<>();
        
        for(Player p : Bukkit.getOnlinePlayers()) {
            players.add(new BukkitPlayer(p));
        }
        
        return players;
    }

    @Override
    public List<String> getBiomes() {
        List<String> biomes = new ArrayList<>();
        for (Biome b : Biome.values()) {
            biomes.add(b.name());
        }
        return biomes;
    }

    @Override
    public String stripColor(String text) {
        return ChatColor.stripColor(text);
    }

    @Override
    public boolean checkWorldName(String name) {
        try {
            MultiWorldWrapper.Utils.checkWorldName(name);
        } catch (DelegateClassException e) {
            return false;
        }
        return true;
    }
}
