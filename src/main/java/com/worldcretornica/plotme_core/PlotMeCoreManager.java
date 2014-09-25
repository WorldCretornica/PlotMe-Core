package com.worldcretornica.plotme_core;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.worldcretornica.plotme_core.MultiWorldWrapper.WorldGeneratorWrapper;
import com.worldcretornica.plotme_core.api.v0_14b.IPlotMe_ChunkGenerator;
import com.worldcretornica.plotme_core.api.v0_14b.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.event.PlotMeEventFactory;
import com.worldcretornica.plotme_core.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class PlotMeCoreManager {

    private PlotMe_Core plugin = null;
    private MultiWorldWrapper multiworld = null;
    private MultiverseWrapper multiverse = null;

    private HashSet<UUID> playersignoringwelimit = null;
    private HashMap<String, PlotMapInfo> plotmaps = null;

    public PlotMeCoreManager(PlotMe_Core instance) {
        plugin = instance;
        setPlayersIgnoringWELimit(new HashSet<UUID>());
        plotmaps = new HashMap<>();
    }

    public boolean CreatePlotWorld(CommandSender cs, String worldname, String generator, Map<String, String> args) {
        //Get a seed
        Long seed = (new java.util.Random()).nextLong();

        //Check if we have multiworld
        if (getMultiworld() == null) {
            if (Bukkit.getPluginManager().isPluginEnabled("MultiWorld")) {
                setMultiworld((JavaPlugin) Bukkit.getPluginManager().getPlugin("MultiWorld"));
            }
        }
        //Check if we have multiverse
        if (getMultiverse() == null) {
            if (Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
                setMultiverse(((JavaPlugin) Bukkit.getPluginManager().getPlugin("Multiverse-Core")));
            }
        }

        //Do we have one of them
        if (getMultiworld() == null && getMultiverse() == null) {
            cs.sendMessage("[" + plugin.getName() + "] " + Util().C("ErrWorldPluginNotFound"));
            return false;
        }

        //Find generator
        Plugin bukkitplugin = Bukkit.getPluginManager().getPlugin(generator);

        //Make generator create settings
        if (bukkitplugin == null) {
            cs.sendMessage("[" + plugin.getName() + "] " + Util().C("ErrCannotFindWorldGen") + " '" + generator + "'");
            return false;
        } else {
            ChunkGenerator cg = bukkitplugin.getDefaultWorldGenerator(worldname, "");
            if (cg instanceof IPlotMe_ChunkGenerator) {
                //Create the generator configurations
                if (!((IPlotMe_ChunkGenerator) cg).getManager().createConfig(worldname, args, cs)) {
                    cs.sendMessage("[" + plugin.getName() + "] " + Util().C("ErrCannotCreateGen1") + " '" + generator + "' " + Util().C("ErrCannotCreateGen2"));
                    return false;
                }
            } else {
                cs.sendMessage("[" + plugin.getName() + "] " + Util().C("ErrCannotCreateGen1") + " '" + generator + "' " + Util().C("ErrCannotCreateGen3"));
                return false;
            }
        }

        PlotMapInfo tempPlotInfo = new PlotMapInfo(plugin, worldname);

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

        addPlotMap(worldname.toLowerCase(), tempPlotInfo);

        //Are we using multiworld?
        if (getMultiworld() != null) {
            boolean success = false;

            if (getMultiworld().isEnabled()) {
                WorldGeneratorWrapper env;

                try {
                    env = WorldGeneratorWrapper.getGenByName("plugin");
                } catch (DelegateClassException ex) {
                    ex.printStackTrace();
                    return false;
                }

                try {
                    success = getMultiworld().getDataManager().makeWorld(worldname, env, seed, generator);
                } catch (DelegateClassException ex) {
                    ex.printStackTrace();
                    return false;
                }

                if (success) {
                    try {
                        getMultiworld().getDataManager().loadWorld(worldname, true);
                        getMultiworld().getDataManager().save();
                    } catch (DelegateClassException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                } else {
                    cs.sendMessage("[" + bukkitplugin.getName() + "] " + Util().C("ErrCannotCreateMW"));
                }
            } else {
                cs.sendMessage("[" + bukkitplugin.getName() + "] " + Util().C("ErrMWDisabled"));
            }
            return success;
        }

        //Are we using multiverse?
        if (getMultiverse() != null) {
            boolean success = false;

            if (getMultiverse().isEnabled()) {
                success = getMultiverse().getMVWorldManager().addWorld(worldname, Environment.NORMAL, seed.toString(), WorldType.NORMAL, true, generator);

                if (!success) {
                    cs.sendMessage("[" + bukkitplugin.getName() + "] " + Util().C("ErrCannotCreateMV"));
                }
            } else {
                cs.sendMessage("[" + bukkitplugin.getName() + "] " + Util().C("ErrMVDisabled"));
            }
            return success;
        }

        return false;
    }

    public int getIdX(String id) {
        return Integer.parseInt(id.substring(0, id.indexOf(";")));
    }

    public int getIdZ(String id) {
        return Integer.parseInt(id.substring(id.indexOf(";") + 1));
    }

    public int getNbOwnedPlot(Player p) {
        return getNbOwnedPlot(p.getUniqueId(), p.getName(), p.getWorld());
    }

    public int getNbOwnedPlot(Player p, World w) {
        return getNbOwnedPlot(p.getUniqueId(), p.getName(), w);
    }

    public int getNbOwnedPlot(UUID uuid, String name, World w) {
        return plugin.getSqlManager().getPlotCount(w.getName(), uuid, name);
    }

    public boolean isEconomyEnabled(String worldname) {
        if (plugin.getConfig().getBoolean("globalUseEconomy") || plugin.getEconomy() != null) {
            return false;
        }
        PlotMapInfo pmi = getMap(worldname);

        if (pmi == null) {
            return false;
        } else {
            return pmi.isUseEconomy() && plugin.getConfig().getBoolean("globalUseEconomy") && plugin.getEconomy() != null;
        }
    }

    public boolean isEconomyEnabled(World w) {
        return isEconomyEnabled(w.getName());
    }

    public boolean isEconomyEnabled(Player p) {
        return isEconomyEnabled(p.getWorld().getName());
    }

    public boolean isEconomyEnabled(Block b) {
        return isEconomyEnabled(b.getWorld().getName());
    }

    public PlotMapInfo getMap(World w) {
        if (w == null) {
            return null;
        } else {
            String worldname = w.getName().toLowerCase();

            if (plotmaps.containsKey(worldname)) {
                return plotmaps.get(worldname);
            } else {
                return null;
            }
        }
    }

    public PlotMapInfo getMap(String name) {
        String worldname = name.toLowerCase();

        if (plotmaps.containsKey(worldname)) {
            return plotmaps.get(worldname);
        } else {
            return null;
        }
    }

    public PlotMapInfo getMap(Location l) {
        if (l == null) {
            return null;
        } else {
            String worldname = l.getWorld().getName().toLowerCase();

            if (plotmaps.containsKey(worldname)) {
                return plotmaps.get(worldname);
            } else {
                return null;
            }
        }
    }

    public PlotMapInfo getMap(Player p) {
        if (p == null) {
            return null;
        } else {
            String worldname = p.getWorld().getName().toLowerCase();
            if (plotmaps.containsKey(worldname)) {
                return plotmaps.get(worldname);
            } else {
                return null;
            }
        }
    }

    public PlotMapInfo getMap(Block b) {
        if (b == null) {
            return null;
        } else {
            String worldname = b.getWorld().getName().toLowerCase();

            if (plotmaps.containsKey(worldname)) {
                return plotmaps.get(worldname);
            } else {
                return null;
            }
        }
    }

    public Plot getPlotById(World w, String id) {
        PlotMapInfo pmi = getMap(w);

        if (pmi == null) {
            return null;
        }

        return pmi.getPlot(id);
    }

    public Plot getPlotById(String name, String id) {
        PlotMapInfo pmi = getMap(name);

        if (pmi == null) {
            return null;
        }

        return pmi.getPlot(id);
    }

    public Plot getPlotById(Player p, String id) {
        PlotMapInfo pmi = getMap(p);

        if (pmi == null) {
            return null;
        }

        return pmi.getPlot(id);
    }

    public Plot getPlotById(Player p) {
        return getPlotById(p.getLocation());
    }

    public Plot getPlotById(Location l) {
        PlotMapInfo pmi = getMap(l);
        String id = getPlotId(l);

        if (pmi == null || id.isEmpty()) {
            return null;
        }

        return pmi.getPlot(id);
    }

    public Plot getPlotById(Block b, String id) {
        PlotMapInfo pmi = getMap(b);

        if (pmi == null) {
            return null;
        }

        return pmi.getPlot(id);
    }

    public Plot getPlotById(Block b) {
        return getPlotById(b.getLocation());
    }

    public void removePlot(World w, String id) {
        PlotMapInfo pmi = getMap(w);

        if (pmi == null) {
            return;
        }

        pmi.removePlot(id);
    }

    public void addPlot(World w, String id, Plot plot) {
        PlotMapInfo pmi = getMap(w);

        if (pmi == null) {
            return;
        }

        pmi.addPlot(id, plot);
        PlotMeEventFactory.callPlotLoadedEvent(plugin, w, plot);
    }

    public World getFirstWorld() {
        if (plotmaps != null) {
	        if (plotmaps.keySet().toArray().length > 0) {
	            return Bukkit.getWorld((String) plotmaps.keySet().toArray()[0]);
	        }
        }
        return null;
    }

    public World getFirstWorld(String player) {
        String world = plugin.getSqlManager().getFirstWorld(player);

        if (!world.isEmpty()) {
            return Bukkit.getWorld(world);
        } else {
            return null;
        }
    }

    public boolean isPlotWorld(World w) {
        return getGenMan(w) != null && plotmaps.containsKey(w.getName().toLowerCase());
    }

    public boolean isPlotWorld(String name) {
        if (getGenMan(name) == null) {
            return false;
        } else {
            return plotmaps.containsKey(name.toLowerCase());
        }
    }

    public boolean isPlotWorld(Location l) {
        if (l == null || getGenMan(l) == null) {
            return false;
        } else {
            return plotmaps.containsKey(l.getWorld().getName().toLowerCase());
        }
    }

    public boolean isPlotWorld(Player p) {
        if (p == null || getGenMan(p.getWorld()) == null) {
            return false;
        } else {
            return plotmaps.containsKey(p.getWorld().getName().toLowerCase());
        }
    }

    public boolean isPlotWorld(Block b) {
        if (b == null || getGenMan(b.getWorld()) == null) {
            return false;
        } else {
            return plotmaps.containsKey(b.getWorld().getName().toLowerCase());
        }
    }

    public boolean isPlotWorld(BlockState b) {
        if (b == null || getGenMan(b.getWorld()) == null) {
            return false;
        } else {
            return plotmaps.containsKey(b.getWorld().getName().toLowerCase());
        }
    }

    public Plot createPlot(World w, String id, String owner, UUID uuid) {
        if (isPlotAvailable(id, w) && !id.isEmpty()) {
            Plot plot = new Plot(plugin, owner, uuid, w, id, getMap(w).getDaysToExpiration());

            setOwnerSign(w, plot);

            addPlot(w, id, plot);

            plugin.getSqlManager().addPlot(plot, getIdX(id), getIdZ(id), w);
            return plot;
        } else {
            return null;
        }
    }

    public boolean movePlot(World w, String idFrom, String idTo) {

        if (!getGenMan(w).movePlot(w, w, idFrom, idTo)) {
            return false;
        }

        Plot plot1 = getPlotById(w, idFrom);
        Plot plot2 = getPlotById(w, idTo);

        if (plot1 != null) {
            if (plot2 != null) {
                int idX = getIdX(idTo);
                int idZ = getIdZ(idTo);
                plugin.getSqlManager().deletePlot(idX, idZ, plot2.getWorld());
                removePlot(w, idFrom);
                removePlot(w, idTo);
                idX = getIdX(idFrom);
                idZ = getIdZ(idFrom);
                plugin.getSqlManager().deletePlot(idX, idZ, plot1.getWorld());

                plot2.setId(idFrom);
                plugin.getSqlManager().addPlot(plot2, idX, idZ, topX(idFrom, w), bottomX(idFrom, w), topZ(idFrom, w), bottomZ(idFrom, w));
                addPlot(w, idFrom, plot2);

                List<String[]> comments = plot2.getComments();
                for (int i = 0; i < comments.size(); i++) {
                    UUID uuid = null;

                    if (comments.get(i).length >= 3) {
                        String strUUID = comments.get(i)[2];
                        try {
                            uuid = UUID.fromString(strUUID);
                        } catch (Exception e) {
                        }
                    }
                    plugin.getSqlManager().addPlotComment(comments.get(i), i, idX, idZ, plot2.getWorld(), uuid);
                }
                
                HashMap<String, UUID> allowed = plot2.allowed().getAllPlayers();
                for (String player : allowed.keySet()) {
                    plugin.getSqlManager().addPlotAllowed(player, allowed.get(player), idX, idZ, plot2.getWorld());
                }
                
                HashMap<String, UUID> denied = plot2.denied().getAllPlayers();
                for (String player : denied.keySet()) {
                    plugin.getSqlManager().addPlotDenied(player, denied.get(player), idX, idZ, plot2.getWorld());
                }

                idX = getIdX(idTo);
                idZ = getIdZ(idTo);
                plot1.setId(idTo);
                plugin.getSqlManager().addPlot(plot1, idX, idZ, topX(idTo, w), bottomX(idTo, w), topZ(idTo, w), bottomZ(idTo, w));
                addPlot(w, idTo, plot1);

                
                comments = plot1.getComments();
                for (int i = 0; i < comments.size(); i++) {
                    UUID uuid = null;

                    if (comments.get(i).length >= 3) {
                        String strUUID = comments.get(i)[2];
                        try {
                            uuid = UUID.fromString(strUUID);
                        } catch (Exception e) {
                        }
                    }
                    plugin.getSqlManager().addPlotComment(comments.get(i), i, idX, idZ, plot1.getWorld(), uuid);
                }

                allowed = plot1.allowed().getAllPlayers();
                for (String player : allowed.keySet()) {
                    plugin.getSqlManager().addPlotAllowed(player, allowed.get(player), idX, idZ, plot1.getWorld());
                }
                
                denied = plot1.denied().getAllPlayers();
                for (String player : denied.keySet()) {
                    plugin.getSqlManager().addPlotDenied(player, denied.get(player), idX, idZ, plot1.getWorld());
                }

                setOwnerSign(w, plot1);
                setSellSign(w, plot1);
                setOwnerSign(w, plot2);
                setSellSign(w, plot2);

            } else {
                int idX = getIdX(idFrom);
                int idZ = getIdZ(idFrom);
                plugin.getSqlManager().deletePlot(idX, idZ, plot1.getWorld());
                removePlot(w, idFrom);
                idX = getIdX(idTo);
                idZ = getIdZ(idTo);
                plot1.setId(idTo);
                plugin.getSqlManager().addPlot(plot1, idX, idZ, topX(idTo, w), bottomX(idTo, w), topZ(idTo, w), bottomZ(idTo, w));
                addPlot(w, idTo, plot1);

                List<String[]> comments = plot1.getComments();
                for (int i = 0; i < comments.size(); i++) {
                    UUID uuid = null;

                    if (comments.get(i).length >= 3) {
                        String strUUID = comments.get(i)[2];
                        try {
                            uuid = UUID.fromString(strUUID);
                        } catch (Exception e) {
                        }
                    }
                    plugin.getSqlManager().addPlotComment(comments.get(i), i, idX, idZ, plot1.getWorld(), uuid);
                }
                
                HashMap<String, UUID> allowed = plot1.allowed().getAllPlayers();
                for (String player : allowed.keySet()) {
                    plugin.getSqlManager().addPlotAllowed(player, allowed.get(player), idX, idZ, plot1.getWorld());
                }
                
                HashMap<String, UUID> denied = plot1.denied().getAllPlayers();
                for (String player : denied.keySet()) {
                    plugin.getSqlManager().addPlotDenied(player, denied.get(player), idX, idZ, plot1.getWorld());
                }

                setOwnerSign(w, plot1);
                setSellSign(w, plot1);
                getGenMan(w).removeOwnerDisplay(w, idFrom);
                getGenMan(w).removeSellerDisplay(w, idFrom);

            }
        } else if (plot2 != null) {
            int idX = getIdX(idTo);
            int idZ = getIdZ(idTo);
            plugin.getSqlManager().deletePlot(idX, idZ, plot2.getWorld());
            removePlot(w, idTo);

            idX = getIdX(idFrom);
            idZ = getIdZ(idFrom);
            plot2.setId(idFrom);
            plugin.getSqlManager().addPlot(plot2, idX, idZ, topX(idFrom, w), bottomX(idFrom, w), topZ(idFrom, w), bottomZ(idFrom, w));
            addPlot(w, idFrom, plot2);

            List<String[]> comments = plot2.getComments();
            for (int i = 0; i < comments.size(); i++) {
                UUID uuid = null;

                if (comments.get(i).length >= 3) {
                    String strUUID = comments.get(i)[2];
                    try {
                        uuid = UUID.fromString(strUUID);
                    } catch (Exception e) {
                    }
                }
                plugin.getSqlManager().addPlotComment(comments.get(i), i, idX, idZ, plot2.getWorld(), uuid);
            }

            HashMap<String, UUID> allowed = plot2.allowed().getAllPlayers();
            for (String player : allowed.keySet()) {
                plugin.getSqlManager().addPlotAllowed(player, allowed.get(player), idX, idZ, plot2.getWorld());
            }

            HashMap<String, UUID> denied = plot2.denied().getAllPlayers();
            for (String player : denied.keySet()) {
                plugin.getSqlManager().addPlotDenied(player, denied.get(player), idX, idZ, plot2.getWorld());
            }

            setOwnerSign(w, plot2);
            setSellSign(w, plot2);
            getGenMan(w).removeOwnerDisplay(w, idTo);
            getGenMan(w).removeSellerDisplay(w, idTo);
        }

        return true;
    }

    public void RemoveLWC(World w, String id) {
        if (plugin.getUsinglwc()) {
            Location bottom = getGenMan(w).getBottom(w, id);
            Location top = getGenMan(w).getTop(w, id);
            final int x1 = bottom.getBlockX();
            final int y1 = bottom.getBlockY();
            final int z1 = bottom.getBlockZ();
            final int x2 = top.getBlockX();
            final int y2 = top.getBlockY();
            final int z2 = top.getBlockZ();
            final String wname = w.getName();

            Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    LWC lwc = com.griefcraft.lwc.LWC.getInstance();
                    List<Protection> protections = lwc.getPhysicalDatabase().loadProtections(wname, x1, x2, y1, y2, z1, z2);

                    for (Protection protection : protections) {
                        protection.remove();
                    }
                }
            });
        }
    }

    public void setOwnerSign(World w, Plot plot) {
        String line1;
        String line2 = "";
        String line4 = "";
        if (plot.getPlotName().length() > 16){
            line1 = plot.getPlotName().substring(0,16);
            line2 = plot.getPlotName().substring(16);
        } else{
            line1 = plot.getPlotName();
        }
        String id = plot.getId();
        if ((Util().C("SignId") + id).length() > 16) {
            //ERROR ID1
            plugin.getLogger().info("Error has occurred. Error Code: ID1. Please report this error to http://dev.bukkit.org/bukkit-plugins/plotme/");
        } else {
            line4 = Util().C("SignId") + id;
        }
        String line3 = plot.getOwner();

        getGenMan(w).setOwnerDisplay(w, id, line1, line2, line3, line4);
    }

    public void setSellSign(World w, Plot plot) {
        String line1 = "";
        String line2 = "";
        String line3 = "";
        String line4 = "";
        String id = plot.getId();

        getGenMan(w).removeSellerDisplay(w, id);

        if (plot.isForSale() || plot.isAuctionned()) {
            if (plot.isForSale()) {
                line1 = Util().C("SignForSale");
                line2 = Util().C("SignPrice");
                if (plot.getCustomPrice() % 1 == 0) {
                    line3 = Util().C("SignPriceColor") + Math.round(plot.getCustomPrice());
                } else {
                    line3 = Util().C("SignPriceColor") + plot.getCustomPrice();
                }
                line4 = "/plotme " + Util().C("CommandBuy");
            }

            getGenMan(w).setSellerDisplay(w, plot.getId(), line1, line2, line3, line4);

            line1 = "";
            line2 = "";
            line3 = "";
            line4 = "";

            if (plot.isAuctionned()) {
                line1 = Util().C("SignOnAuction");
                if (plot.getCurrentBidder().isEmpty()) {
                    line2 = Util().C("SignMinimumBid");
                } else {
                    line2 = Util().C("SignCurrentBid");
                }
                if (plot.getCurrentBid() % 1 == 0) {
                    line3 = Util().C("SignCurrentBidColor") + Math.round(plot.getCurrentBid());
                } else {
                    line3 = Util().C("SignCurrentBidColor") + plot.getCurrentBid();
                }
                line4 = "/plotme " + Util().C("CommandBid") + " <x>";
            }

            getGenMan(w).setAuctionDisplay(w, id, line1, line2, line3, line4);
        }
    }

    public void adjustLinkedPlots(String id, World world) {
        //TODO
        Map<String, Plot> plots = new HashMap<>(); //getPlots(world);

        IPlotMe_GeneratorManager gm = getGenMan(world);

        int x = getIdX(id);
        int z = getIdZ(id);

        Plot p11 = plots.get(id);

        if (p11 != null) {
            Plot p01 = plots.get((x - 1) + ";" + z);
            Plot p10 = plots.get(x + ";" + (z - 1));
            Plot p12 = plots.get(x + ";" + (z + 1));
            Plot p21 = plots.get((x + 1) + ";" + z);
            Plot p00 = plots.get((x - 1) + ";" + (z - 1));
            Plot p02 = plots.get((x - 1) + ";" + (z + 1));
            Plot p20 = plots.get((x + 1) + ";" + (z - 1));
            Plot p22 = plots.get((x + 1) + ";" + (z + 1));

            if (p01 != null && p01.getOwner().equalsIgnoreCase(p11.getOwner())) {
                gm.fillroad(p01.getId(), p11.getId(), world);
            }

            if (p10 != null && p10.getOwner().equalsIgnoreCase(p11.getOwner())) {
                gm.fillroad(p10.getId(), p11.getId(), world);
            }

            if (p12 != null && p12.getOwner().equalsIgnoreCase(p11.getOwner())) {
                gm.fillroad(p12.getId(), p11.getId(), world);
            }

            if (p21 != null && p21.getOwner().equalsIgnoreCase(p11.getOwner())) {
                gm.fillroad(p21.getId(), p11.getId(), world);
            }

            if (p00 != null && p10 != null && p01 != null
                    && p00.getOwner().equalsIgnoreCase(p11.getOwner())
                    && p11.getOwner().equalsIgnoreCase(p10.getOwner())
                    && p10.getOwner().equalsIgnoreCase(p01.getOwner())) {
                gm.fillmiddleroad(p00.getId(), p11.getId(), world);
            }

            if (p10 != null && p20 != null && p21 != null
                    && p10.getOwner().equalsIgnoreCase(p11.getOwner())
                    && p11.getOwner().equalsIgnoreCase(p20.getOwner())
                    && p20.getOwner().equalsIgnoreCase(p21.getOwner())) {
                gm.fillmiddleroad(p20.getId(), p11.getId(), world);
            }

            if (p01 != null && p02 != null && p12 != null
                    && p01.getOwner().equalsIgnoreCase(p11.getOwner())
                    && p11.getOwner().equalsIgnoreCase(p02.getOwner())
                    && p02.getOwner().equalsIgnoreCase(p12.getOwner())) {
                gm.fillmiddleroad(p02.getId(), p11.getId(), world);
            }

            if (p12 != null && p21 != null && p22 != null
                    && p12.getOwner().equalsIgnoreCase(p11.getOwner())
                    && p11.getOwner().equalsIgnoreCase(p21.getOwner())
                    && p21.getOwner().equalsIgnoreCase(p22.getOwner())) {
                gm.fillmiddleroad(p22.getId(), p11.getId(), world);
            }

        }
    }

    public void setBiome(World w, Plot plot, Biome b) {
        String id = plot.getId();

        getGenMan(w).setBiome(w, id, b);

        plugin.getSqlManager().updatePlot(getIdX(id), getIdZ(id), plot.getWorld(), "biome", b.name());
    }

    public void clear(World w, Plot plot, CommandSender cs, ClearReason reason) {
        String id = plot.getId();

        plot.setForSale(false);
        plot.setProtect(false);
        plot.setAuctionned(false);
        plot.setAuctionnedDate(null);
        plot.setCurrentBid(0);
        plot.setCurrentBidder("");

        String world = w.getName();
        int idX = getIdX(id);
        int idZ = getIdZ(id);

        SqlManager sm = plugin.getSqlManager();

        sm.updatePlot(idX, idZ, world, "forsale", false);
        sm.updatePlot(idX, idZ, world, "protected", false);
        sm.updatePlot(idX, idZ, world, "auctionned", false);
        sm.updatePlot(idX, idZ, world, "auctionneddate", "");
        sm.updatePlot(idX, idZ, world, "currentbid", 0);
        sm.updatePlot(idX, idZ, world, "currentbidder", "");

        if (getMap(w).isUseProgressiveClear()) {
            plugin.addPlotToClear(new PlotToClear(world, id, cs, reason));
        } else {
            plugin.getGenManager(w).clear(w, id);
            RemoveLWC(w, id);
            cs.sendMessage(plugin.getUtil().C("MsgPlotCleared"));
        }
    }

    public boolean isPlotAvailable(String id, World world) {
        return isPlotAvailable(id, world.getName().toLowerCase());
    }

    public boolean isPlotAvailable(String id, Player p) {
        return isPlotAvailable(id, p.getWorld().getName().toLowerCase());
    }

    public boolean isPlotAvailable(String id, String world) {
        PlotMapInfo pmi = getMap(world);

        if (pmi != null) {
            return pmi.getPlot(id) == null;
        } else {
            return false;
        }
    }

    public String getPlotId(Location l) {
        IPlotMe_GeneratorManager gen = getGenMan(l);
        if (gen == null) {
            return "";
        } else {
            return gen.getPlotId(l);
        }


    }

    public String getPlotId(Player p) {
        IPlotMe_GeneratorManager gen = getGenMan(p.getLocation());
        if (gen == null) {
            return "";
        } else {
            return gen.getPlotId(p.getLocation());
        }


    }

    public IPlotMe_GeneratorManager getGenMan(World w) {
        return plugin.getGenManager(w);
    }

    public IPlotMe_GeneratorManager getGenMan(Location l) {
        return plugin.getGenManager(l.getWorld());
    }

    public IPlotMe_GeneratorManager getGenMan(String s) {
        return plugin.getGenManager(s);
    }

    public Location getPlotBottomLoc(World w, String id) {
        return getGenMan(w).getPlotBottomLoc(w, id);
    }

    public Location getPlotTopLoc(World w, String id) {
        return getGenMan(w).getPlotTopLoc(w, id);
    }

    public void adjustWall(Location l) {
        Plot plot = getPlotById(l);
        String id = getPlotId(l);
        World w = l.getWorld();

        if (plot == null) {
            getGenMan(w).adjustPlotFor(w, id, false, false, false, false);
        } else {
            getGenMan(w).adjustPlotFor(w, id, true, plot.isProtect(), plot.isAuctionned(), plot.isForSale());
        }
    }

    public void adjustWall(World w, Plot plot) {
        String id = plot.getId();
        getGenMan(w).adjustPlotFor(w, id, true, plot.isProtect(), plot.isAuctionned(), plot.isForSale());
    }

    public void removeOwnerSign(World w, String id) {
        getGenMan(w).removeOwnerDisplay(w, id);
    }

    public void removeSellSign(World w, String id) {
        getGenMan(w).removeSellerDisplay(w, id);
    }

    public void removeAuctionSign(World w, String id) {
        getGenMan(w).removeAuctionDisplay(w, id);
    }

    public boolean isValidId(World w, String id) {
        return getGenMan(w).isValidId(id);
    }

    public int bottomX(String id, World w) {
        return getGenMan(w).bottomX(id, w);
    }

    public int topX(String id, World w) {
        return getGenMan(w).topX(id, w);
    }

    public int bottomZ(String id, World w) {
        return getGenMan(w).bottomZ(id, w);
    }

    public int topZ(String id, World w) {
        return getGenMan(w).topZ(id, w);
    }

    public void setBiome(World w, String id, Biome biome) {
        getGenMan(w).setBiome(w, id, biome);
        plugin.getSqlManager().updatePlot(getIdX(id), getIdZ(id), w.getName(), "biome", biome.name());
    }

    public Location getPlotHome(World w, String id) {
        return getGenMan(w).getPlotHome(w, id);
    }

    public List<Player> getPlayersInPlot(World w, String id) {
        return getGenMan(w).getPlayersInPlot(w, id);
    }

    public void regen(World w, Plot plot, CommandSender sender) {
        getGenMan(w).regen(w, plot.getId(), sender);
    }

    public PlotToClear getPlotLockInfo(String w, String id) {
        return plugin.getPlotLocked(w, id);
    }

    public HashSet<UUID> getPlayersIgnoringWELimit() {
        return playersignoringwelimit;
    }

    public void setPlayersIgnoringWELimit(HashSet<UUID> playersignoringwelimit) {
        this.playersignoringwelimit = playersignoringwelimit;
    }

    public boolean addPlayerIgnoringWELimit(UUID uuid) {
        return playersignoringwelimit.add(uuid);
    }

    public boolean removePlayerIgnoringWELimit(UUID uuid) {
        return playersignoringwelimit.remove(uuid);
    }

    public boolean isPlayerIgnoringWELimit(UUID uuid) {
        return playersignoringwelimit.contains(uuid);
    }

    public Map<String, PlotMapInfo> getPlotMaps() {
        return plotmaps;
    }

    public void addPlotMap(String world, PlotMapInfo map) {
        plotmaps.put(world, map);
    }

    public void removePlotMap(String world) {
        plotmaps.remove(world);
    }

    private Util Util() {
        return plugin.getUtil();
    }

    public MultiWorldWrapper getMultiworld() {
        return multiworld;
    }

    public void setMultiworld(JavaPlugin multiworld) {
        this.multiworld = new MultiWorldWrapper(multiworld);
    }

    public MultiverseWrapper getMultiverse() {
        return multiverse;
    }

    public void setMultiverse(JavaPlugin multiverse) {
        this.multiverse = new MultiverseWrapper(multiverse);
    }
}