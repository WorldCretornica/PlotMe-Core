package com.worldcretornica.plotme_core.bukkit;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.api.event.IEventFactory;
import com.worldcretornica.plotme_core.bukkit.MultiWorldWrapper.WorldGeneratorWrapper;
import com.worldcretornica.plotme_core.bukkit.api.*;
import com.worldcretornica.plotme_core.bukkit.event.BukkitEventFactory;
import com.worldcretornica.plotme_core.bukkit.listener.BukkitPlayerListener;
import com.worldcretornica.plotme_core.bukkit.listener.BukkitPlotDenyListener;
import com.worldcretornica.plotme_core.bukkit.listener.BukkitPlotListener;
import com.worldcretornica.plotme_core.bukkit.listener.BukkitPlotWorldEditListener;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Logger;

public class BukkitServerBridge implements IServerBridge {

    private PlotMe_CorePlugin plugin;
    private Economy economy;
    private PlotWorldEdit plotworldedit;
    private boolean usinglwc;
    private IEventFactory eventfactory;

    private MultiWorldWrapper multiworld;
    private MultiverseWrapper multiverse;
    
    public BukkitServerBridge(PlotMe_CorePlugin instance) {
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
            
            PlotMe_Core plotme_core = plugin.getAPI();
            WorldEditPlugin worldeditplugin = (WorldEditPlugin) pm.getPlugin("WorldEdit");

            try {
                Class.forName("com.sk89q.worldedit.function.mask.Mask");
                PlotWorldEdit pwe = (PlotWorldEdit) Class.forName("com.worldcretornica.plotme_core.bukkit.worldedit.PlotWorldEdit6_0_0").getConstructor(PlotMe_Core.class, WorldEditPlugin.class).newInstance(plotme_core, worldeditplugin);
                setPlotWorldEdit(pwe);
            } catch (Exception unused) {
                try {
                    PlotWorldEdit pwe = (PlotWorldEdit) Class.forName("com.worldcretornica.plotme_core.bukkit.worldedit.PlotWorldEdit5_7").getConstructor(PlotMe_Core.class, WorldEditPlugin.class).newInstance(plotme_core, worldeditplugin);
                    setPlotWorldEdit(pwe);
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

    @Override
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
    public IConfigSection getConfig(String file) {
        
        File configfile = new File(plugin.getDataFolder().getAbsolutePath(), file);
        YamlConfiguration config = new YamlConfiguration();
        
        try 
        {
            config.load(configfile);
        } catch (FileNotFoundException ignored) {
        }
        catch (IOException e) 
        {
            plugin.getLogger().severe("Can't read configuration file");
            e.printStackTrace();
        } 
        catch (InvalidConfigurationException e) 
        {
            plugin.getLogger().severe("Invalid configuration format");
            e.printStackTrace();
        }
        
        return new BukkitConfigSection(plugin, config);
    }

    @Override
    public void saveResource(String fileName, boolean b) {
        plugin.saveResource(fileName, b);
    }

    @Override
    public IPlotMe_ChunkGenerator getPlotMeGenerator(String pluginname, String worldname) {
        if (Bukkit.getPluginManager().isPluginEnabled(pluginname)) {
            Plugin genplugin = Bukkit.getPluginManager().getPlugin(pluginname);
            if (genplugin != null) {
                ChunkGenerator gen = genplugin.getDefaultWorldGenerator(worldname, "");
                if (gen instanceof IBukkitPlotMe_ChunkGenerator) {
                    return new BukkitPlotMe_ChunkGeneratorBridge((IBukkitPlotMe_ChunkGenerator) gen);
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

    @Override
    public IPlotMe_ChunkGenerator getPlotMeGenerator(String worldname) {
        World w = Bukkit.getWorld(worldname);
        if (w != null) {
            ChunkGenerator cg = w.getGenerator();
            if (cg instanceof IPlotMe_ChunkGenerator) {
                return (IPlotMe_ChunkGenerator) cg;
            }
        }
        return null;
    }

    @Override
    public boolean worldExists(String worldname) {
        return Bukkit.getWorlds().contains(worldname);
    }

    @Override
    public List<IWorld> getWorlds() {
        List<IWorld> worlds = new ArrayList<>();
        
        for(World w : Bukkit.getWorlds()) {
            worlds.add(new BukkitWorld(w));
        }
        
        return worlds;
    }

    @Override
    public boolean CreatePlotWorld(ICommandSender cs, String worldname, String generator, Map<String, String> args) {
        //Get a seed
        Long seed = new Random().nextLong();

        //Check if we have multiworld
        if (getMultiWorld() == null) {
            if (Bukkit.getPluginManager().isPluginEnabled("MultiWorld")) {
                setMultiworld((JavaPlugin) Bukkit.getPluginManager().getPlugin("MultiWorld"));
            }
        }
        //Check if we have multiverse
        if (getMultiverse() == null) {
            if (Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
                setMultiverse((JavaPlugin) Bukkit.getPluginManager().getPlugin("Multiverse-Core"));
            }
        }

        //Do we have one of them
        if (getMultiWorld() == null && getMultiverse() == null) {
            cs.sendMessage("[" + plugin.getName() + "] " + plugin.getAPI().getUtil().C("ErrWorldPluginNotFound"));
            return false;
        }

        //Find generator
        IPlotMe_ChunkGenerator bukkitplugin = plugin.getServerObjectBuilder().getPlotMeGenerator(generator, worldname);

        //Make generator create settings
        if (bukkitplugin == null) {
            cs.sendMessage("[" + plugin.getName() + "] " + plugin.getAPI().getUtil().C("ErrCannotFindWorldGen") + " '" + generator + "'");
            return false;
        } else if (!bukkitplugin.getManager().createConfig(worldname, args, cs)) { //Create the generator configurations
            cs.sendMessage("[" + plugin.getName() + "] " + plugin.getAPI().getUtil().C("ErrCannotCreateGen1") + " '" + generator + "' " + plugin.getAPI().getUtil().C("ErrCannotCreateGen2"));
            return false;
        }

        PlotMapInfo tempPlotInfo = new PlotMapInfo(plugin.getAPI(), worldname);

        tempPlotInfo.setPlotAutoLimit(Integer.parseInt(args.get("PlotAutoLimit")));
        tempPlotInfo.setDaysToExpiration(Integer.parseInt(args.get("DaysToExpiration")));
        tempPlotInfo.setAutoLinkPlots(Boolean.parseBoolean(args.get("AutoLinkPlots")));
        tempPlotInfo.setDisableExplosion(Boolean.parseBoolean(args.get("DisableExplosion")));
        tempPlotInfo.setDisableIgnition(Boolean.parseBoolean(args.get("DisableIgnition")));
        tempPlotInfo.setUseProgressiveClear(Boolean.parseBoolean(args.get("UseProgressiveClear")));
        tempPlotInfo.setUseEconomy(Boolean.parseBoolean(args.get("UseEconomy")));
        tempPlotInfo.setCanPutOnSale(Boolean.parseBoolean(args.get("CanPutOnSale")));
        tempPlotInfo.setCanSellToBank(Boolean.parseBoolean(args.get("CanSellToBank")));
        tempPlotInfo.setRefundClaimPriceOnReset(Boolean.parseBoolean(args.get("RefundClaimPriceOnReset")));
        tempPlotInfo.setRefundClaimPriceOnSetOwner(Boolean.parseBoolean(args.get("RefundClaimPriceOnSetOwner")));
        tempPlotInfo.setClaimPrice(Double.parseDouble(args.get("ClaimPrice")));
        tempPlotInfo.setClearPrice(Double.parseDouble(args.get("ClearPrice")));
        tempPlotInfo.setAddPlayerPrice(Double.parseDouble(args.get("AddPlayerPrice")));
        tempPlotInfo.setDenyPlayerPrice(Double.parseDouble(args.get("DenyPlayerPrice")));
        tempPlotInfo.setRemovePlayerPrice(Double.parseDouble(args.get("RemovePlayerPrice")));
        tempPlotInfo.setUndenyPlayerPrice(Double.parseDouble(args.get("UndenyPlayerPrice")));
        tempPlotInfo.setPlotHomePrice(Double.parseDouble(args.get("PlotHomePrice")));
        tempPlotInfo.setCanCustomizeSellPrice(Boolean.parseBoolean(args.get("CanCustomizeSellPrice")));
        tempPlotInfo.setSellToPlayerPrice(Double.parseDouble(args.get("SellToPlayerPrice")));
        tempPlotInfo.setSellToBankPrice(Double.parseDouble(args.get("SellToBankPrice")));
        tempPlotInfo.setBuyFromBankPrice(Double.parseDouble(args.get("BuyFromBankPrice")));
        tempPlotInfo.setAddCommentPrice(Double.parseDouble(args.get("AddCommentPrice")));
        tempPlotInfo.setBiomeChangePrice(Double.parseDouble(args.get("BiomeChangePrice")));
        tempPlotInfo.setProtectPrice(Double.parseDouble(args.get("ProtectPrice")));
        tempPlotInfo.setDisposePrice(Double.parseDouble(args.get("DisposePrice")));

        plugin.getAPI().getPlotMeCoreManager().addPlotMap(worldname.toLowerCase(), tempPlotInfo);

        //Are we using multiworld?
        if (getMultiWorldWrapper() != null) {
            boolean success = false;

            if (getMultiWorld().isEnabled()) {
                WorldGeneratorWrapper env;

                try {
                    env = WorldGeneratorWrapper.getGenByName("plugin");
                } catch (DelegateClassException ex) {
                    ex.printStackTrace();
                    return false;
                }

                try {
                    success = getMultiWorld().getDataManager().makeWorld(worldname, env, seed, generator);
                } catch (DelegateClassException ex) {
                    ex.printStackTrace();
                    return false;
                }

                if (success) {
                    try {
                        getMultiWorld().getDataManager().loadWorld(worldname, true);
                        getMultiWorld().getDataManager().save();
                    } catch (DelegateClassException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                } else {
                    cs.sendMessage("[" + plugin.getName() + "] " + plugin.getAPI().getUtil().C("ErrCannotCreateMW"));
                }
            } else {
                cs.sendMessage("[" + plugin.getName() + "] " + plugin.getAPI().getUtil().C("ErrMWDisabled"));
            }
            return success;
        }

        //Are we using multiverse?
        if (getMultiverse() != null) {
            boolean success = false;
            if (getMultiverse().isEnabled()) {
                success = plugin.getServerObjectBuilder().addMultiverseWorld(worldname, "NORMAL", seed.toString(), "NORMAL", true, generator);

                if (!success) {
                    cs.sendMessage("[" + plugin.getName() + "] " + plugin.getAPI().getUtil().C("ErrCannotCreateMV"));
                }
            } else {
                cs.sendMessage("[" + plugin.getName() + "] " + plugin.getAPI().getUtil().C("ErrMVDisabled"));
            }
            return success;
        }

        return false;
    }

    private MultiWorldWrapper getMultiWorld() {
        return multiworld;
    }
    
    private MultiverseWrapper getMultiverse() {
        return multiverse;
    }
    
    private void setMultiverse(JavaPlugin multiverse) {
        this.multiverse = new MultiverseWrapper(multiverse);
    }
    
    private void setMultiworld(JavaPlugin multiworld) {
        this.multiworld = new MultiWorldWrapper(multiworld);
    }
    
    private MultiWorldWrapper getMultiWorldWrapper() {
        if (Bukkit.getPluginManager().isPluginEnabled("MultiWorld")) {
            return new MultiWorldWrapper((JavaPlugin) Bukkit.getPluginManager().getPlugin("MultiWorld"));
        } else {
            return null;
        }
    }

    private MultiverseWrapper getMultiverseWrapper() {
        if (Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
            return new MultiverseWrapper((JavaPlugin) Bukkit.getPluginManager().getPlugin("Multiverse-Core"));
        } else {
            return null;
        }
    }

    @Override
    public IMaterial getMaterial(String string) {
        return new BukkitMaterial(Material.valueOf(string));
    }

    @Override
    public ILocation createLocation(IWorld w, int x, int y, int z) {
        return w.createLocation(x, y, z);
    }

    @Override
    public IEntityType getEntityType(String string) {
        return new BukkitEntityType(EntityType.valueOf(string));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IConfigSection getConfig(InputStream defConfigStream) {
        return new BukkitConfigSection(plugin, YamlConfiguration.loadConfiguration(defConfigStream));
    }
    
    @Override
    public IConfigSection loadDefaultConfig(String worldPath) {
        ConfigurationSection defaultCS = getDefaultWorld();
        ConfigurationSection configCS;
        if (plugin.getConfig().contains(worldPath)) {
            configCS = plugin.getConfig().getConfigurationSection(worldPath);
        } else {
            plugin.getConfig().set(worldPath, defaultCS);
            plugin.saveConfig();
            configCS = plugin.getConfig().getConfigurationSection(worldPath);
        }
        for (String path : defaultCS.getKeys(true)) {
            configCS.addDefault(path, defaultCS.get(path));
        }
        return new BukkitConfigSection(plugin, plugin.getConfig(), configCS);
    }
    
    private ConfigurationSection getDefaultWorld() {
        InputStream defConfigStream = plugin.getResource("default-world.yml");
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(defConfigStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            isr = new InputStreamReader(defConfigStream);
        }
        return YamlConfiguration.loadConfiguration(isr);
    }
}
