package com.worldcretornica.plotme_core;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.utils.Util;

import java.util.*;

public class PlotMeCoreManager {

    private PlotMe_Core plugin;
    private HashSet<UUID> playersignoringwelimit;
    private HashMap<String, PlotMapInfo> plotmaps;

    public PlotMeCoreManager(PlotMe_Core instance) {
        plugin = instance;
        setPlayersIgnoringWELimit(new HashSet<UUID>());
        plotmaps = new HashMap<>();
    }

    public int getIdX(String id) {
        return Integer.parseInt(id.substring(0, id.indexOf(";")));
    }

    public int getIdZ(String id) {
        return Integer.parseInt(id.substring(id.indexOf(";") + 1));
    }

    public int getNbOwnedPlot(IPlayer p) {
        return getNbOwnedPlot(p.getUniqueId(), p.getName(), p.getWorld());
    }

    public int getNbOwnedPlot(IPlayer p, IWorld w) {
        return getNbOwnedPlot(p.getUniqueId(), p.getName(), w);
    }

    public int getNbOwnedPlot(UUID uuid, String name, IWorld w) {
        return plugin.getSqlManager().getPlotCount(w.getName(), uuid, name);
    }

    public boolean isEconomyEnabled(String worldname) {
        if (plugin.getServerObjectBuilder().getConfig().getBoolean("globalUseEconomy") || plugin.getServerObjectBuilder().getEconomy() != null) {
            return false;
        }
        PlotMapInfo pmi = getMap(worldname);

        if (pmi == null) {
            return false;
        } else {
            return pmi.isUseEconomy() && plugin.getServerObjectBuilder().getConfig().getBoolean("globalUseEconomy") && plugin.getServerObjectBuilder().getEconomy() != null;
        }
    }

    public boolean isEconomyEnabled(IWorld w) {
        return isEconomyEnabled(w.getName());
    }

    public boolean isEconomyEnabled(IPlayer p) {
        return isEconomyEnabled(p.getWorld().getName());
    }

    public PlotMapInfo getMap(IWorld w) {
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

    public PlotMapInfo getMap(ILocation l) {
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

    public PlotMapInfo getMap(IPlayer p) {
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

    public PlotMapInfo getMap(IBlock b) {
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

    public Plot getPlotById(IWorld w, String id) {
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

    public Plot getPlotById(IPlayer p, String id) {
        PlotMapInfo pmi = getMap(p);

        if (pmi == null) {
            return null;
        }

        return pmi.getPlot(id);
    }

    public Plot getPlotById(ILocation l) {
        PlotMapInfo pmi = getMap(l);
        String id = getPlotId(l);

        if (pmi == null || id.isEmpty()) {
            return null;
        }

        return pmi.getPlot(id);
    }

    public Plot getPlotById(IBlock b, String id) {
        PlotMapInfo pmi = getMap(b);

        if (pmi == null) {
            return null;
        }

        return pmi.getPlot(id);
    }


    public void removePlot(IWorld w, String id) {
        PlotMapInfo pmi = getMap(w);

        if (pmi == null) {
            return;
        }

        pmi.removePlot(id);
    }

    public void addPlot(IWorld w, String id, Plot plot) {
        PlotMapInfo pmi = getMap(w);

        if (pmi == null) {
            return;
        }

        pmi.addPlot(id, plot);
        plugin.getServerObjectBuilder().getEventFactory().callPlotLoadedEvent(plugin, w, plot);
    }

    public IWorld getFirstWorld() {
        if (plotmaps != null) {
	        if (plotmaps.keySet().toArray().length > 0) {
	            return plugin.getServerObjectBuilder().getWorld((String) plotmaps.keySet().toArray()[0]);
	        }
        }
        return null;
    }

    public IWorld getFirstWorld(String player) {
        String world = plugin.getSqlManager().getFirstWorld(player);

        if (!world.isEmpty()) {
            return plugin.getServerObjectBuilder().getWorld(world);
        } else {
            return null;
        }
    }

    public boolean isPlotWorld(IWorld w) {
        return !(w == null || getGenMan(w) == null) && plotmaps.containsKey(w.getName().toLowerCase());
    }

    public boolean isPlotWorld(String name) {
        if (getGenMan(name) == null) {
            return false;
        } else {
            return plotmaps.containsKey(name.toLowerCase());
        }
    }

    public boolean isPlotWorld(ILocation l) {
        if (l == null || getGenMan(l) == null) {
            return false;
        } else {
            return plotmaps.containsKey(l.getWorld().getName().toLowerCase());
        }
    }

    public boolean isPlotWorld(IPlayer p) {
        if (p == null || getGenMan(p.getWorld()) == null) {
            return false;
        } else {
            return plotmaps.containsKey(p.getWorld().getName().toLowerCase());
        }
    }

    public boolean isPlotWorld(IBlock b) {
        return getGenMan(b.getWorld()) != null && plotmaps.containsKey(b.getWorld().getName().toLowerCase());
    }

    public boolean isPlotWorld(IBlockState b) {
        if (b == null || getGenMan(b.getWorld()) == null) {
            return false;
        } else {
            return plotmaps.containsKey(b.getWorld().getName().toLowerCase());
        }
    }

    public Plot createPlot(IWorld w, String id, String owner, UUID uuid) {
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

    public boolean movePlot(IWorld w, String idFrom, String idTo) {

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

    public void RemoveLWC(IWorld w, String id) {
        if (plugin.getServerObjectBuilder().getUsinglwc()) {
            ILocation bottom = getGenMan(w).getBottom(w, id);
            ILocation top = getGenMan(w).getTop(w, id);
            final int x1 = bottom.getBlockX();
            final int y1 = bottom.getBlockY();
            final int z1 = bottom.getBlockZ();
            final int x2 = top.getBlockX();
            final int y2 = top.getBlockY();
            final int z2 = top.getBlockZ();
            final String wname = w.getName();

            plugin.getServerObjectBuilder().runTaskAsynchronously(new Runnable() {
                @Override
                public void run() {
                    LWC lwc = LWC.getInstance();
                    List<Protection> protections = lwc.getPhysicalDatabase().loadProtections(wname, x1, x2, y1, y2, z1, z2);

                    for (Protection protection : protections) {
                        protection.remove();
                    }
                }
            });
        }
    }

    public void setOwnerSign(IWorld w, Plot plot) {
        String line1;
        String line2 = "";
        String id = plot.getId();

        if ((Util().C("SignId") + id).length() > 16) {
            line1 = (Util().C("SignId") + id).substring(0, 16);
            if ((Util().C("SignId") + id).length() > 32) {
                line2 = (Util().C("SignId") + id).substring(16, 32);
            } else {
                line2 = (Util().C("SignId") + id).substring(16);
            }
        } else {
            line1 = Util().C("SignId") + id;
        }
        String line3 = plot.getOwner();
        String line4 = "";

        getGenMan(w).setOwnerDisplay(w, plot.getId(), line1, line2, line3, line4);
    }

    public void setSellSign(IWorld w, Plot plot) {
        String id = plot.getId();

        getGenMan(w).removeSellerDisplay(w, id);

        if (plot.isForSale() || plot.isAuctionned()) {
            String line1 = "";
            String line2 = "";
            String line3 = "";
            String line4 = "";
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

            getGenMan(w).setAuctionDisplay(w, plot.getId(), line1, line2, line3, line4);
        }
    }

    public void adjustLinkedPlots(String id, IWorld world) {
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

    public void setBiome(IWorld w, Plot plot, IBiome b) {
        String id = plot.getId();

        getGenMan(w).setBiome(w, id, b);

        plugin.getSqlManager().updatePlot(getIdX(id), getIdZ(id), plot.getWorld(), "biome", b.name());
    }

    public void clear(IWorld w, Plot plot, ICommandSender cs, ClearReason reason) {
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

    public boolean isPlotAvailable(String id, IWorld world) {
        return isPlotAvailable(id, world.getName().toLowerCase());
    }

    public boolean isPlotAvailable(String id, IPlayer p) {
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

    public String getPlotId(ILocation l) {
        if (getGenMan(l) == null) {
            return "";
        }

        IPlotMe_GeneratorManager gen = getGenMan(l);

        if (gen == null) {
            return "";
        } else {
            return gen.getPlotId(l);
        }
    }

    public IPlotMe_GeneratorManager getGenMan(IWorld w) {
        return plugin.getGenManager(w);
    }

    public IPlotMe_GeneratorManager getGenMan(ILocation l) {
        return plugin.getGenManager(l.getWorld());
    }

    public IPlotMe_GeneratorManager getGenMan(String s) {
        return plugin.getGenManager(s);
    }

    public ILocation getPlotBottomLoc(IWorld w, String id) {
        return getGenMan(w).getPlotBottomLoc(w, id);
    }

    public ILocation getPlotTopLoc(IWorld w, String id) {
        return getGenMan(w).getPlotTopLoc(w, id);
    }

    public void adjustWall(ILocation l) {
        Plot plot = getPlotById(l);
        String id = getPlotId(l);
        IWorld w = l.getWorld();

        if (plot == null) {
            getGenMan(w).adjustPlotFor(w, id, false, false, false, false);
        } else {
            getGenMan(w).adjustPlotFor(w, id, true, plot.isProtect(), plot.isAuctionned(), plot.isForSale());
        }
    }

    public void adjustWall(IWorld w, Plot plot) {
        String id = plot.getId();
        getGenMan(w).adjustPlotFor(w, id, true, plot.isProtect(), plot.isAuctionned(), plot.isForSale());
    }

    public void removeOwnerSign(IWorld w, String id) {
        getGenMan(w).removeOwnerDisplay(w, id);
    }

    public void removeSellSign(IWorld w, String id) {
        getGenMan(w).removeSellerDisplay(w, id);
    }

    public void removeAuctionSign(IWorld w, String id) {
        getGenMan(w).removeAuctionDisplay(w, id);
    }

    public boolean isValidId(IWorld w, String id) {
        return getGenMan(w).isValidId(id);
    }

    public int bottomX(String id, IWorld w) {
        return getGenMan(w).bottomX(id, w);
    }

    public int topX(String id, IWorld w) {
        return getGenMan(w).topX(id, w);
    }

    public int bottomZ(String id, IWorld w) {
        return getGenMan(w).bottomZ(id, w);
    }

    public int topZ(String id, IWorld w) {
        return getGenMan(w).topZ(id, w);
    }

    public void setBiome(IWorld w, String id, IBiome biome) {
        getGenMan(w).setBiome(w, id, biome);
        plugin.getSqlManager().updatePlot(getIdX(id), getIdZ(id), w.getName(), "biome", biome.name());
    }

    public ILocation getPlotHome(IWorld w, String id) {
        return getGenMan(w).getPlotHome(w, id);
    }

    public List<IPlayer> getPlayersInPlot(IWorld w, String id) {
        return getGenMan(w).getPlayersInPlot(w, id);
    }

    public void regen(IWorld w, Plot plot, ICommandSender sender) {
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
        return this.playersignoringwelimit.add(uuid);
    }

    public boolean removePlayerIgnoringWELimit(UUID uuid) {
        return this.playersignoringwelimit.remove(uuid);
    }

    public boolean isPlayerIgnoringWELimit(UUID uuid) {
        return this.playersignoringwelimit.contains(uuid);
    }

    public Map<String, PlotMapInfo> getPlotMaps() {
        return plotmaps;
    }

    public void addPlotMap(String world, PlotMapInfo map) {
        this.plotmaps.put(world, map);
    }

    public void removePlotMap(String world) {
        this.plotmaps.remove(world);
    }

    private Util Util() {
        return plugin.getUtil();
    }
}
