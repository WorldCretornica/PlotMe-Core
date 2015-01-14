package com.worldcretornica.plotme_core;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBiome;
import com.worldcretornica.plotme_core.utils.Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class PlotMeCoreManager {

    private final PlotMe_Core plugin;
    private HashSet<UUID> playersignoringwelimit;
    private HashMap<String, PlotMapInfo> plotmaps;

    public PlotMeCoreManager(PlotMe_Core instance) {
        plugin = instance;
        setPlayersIgnoringWELimit(new HashSet<UUID>());
        plotmaps = new HashMap<>();
    }

    /**
     * Gets the X coordinate on the plot grid
     * Not to be confused with the X coordinate of a Block or Entity
     * @param id PlotID
     * @return X Coordinate
     */
    public static int getIdX(String id) {
        return Integer.parseInt(id.substring(0, id.indexOf(";")));
    }

    /**
     * Gets the Z coordinate on the plot grid
     * Not to be confused with the Z coordinate of a Block or Entity
     * @param id PlotID
     * @return Z Coordinate
     */
    public static int getIdZ(String id) {
        return Integer.parseInt(id.substring(id.indexOf(";") + 1));
    }

    /**
     * Gets the plot by the id and pmi
     * @param id Plot ID
     * @param pmi PlotMapInfo
     * @return
     */
    public static Plot getPlotById(String id, PlotMapInfo pmi) {
        if (pmi == null) {
            return null;
        }

        return pmi.getPlot(id);
    }

    /**
     * Gets the plot with the player and pmi. The player has its location checked to retrieve the plotID.
     * @param player player standing in the plot
     * @param pmi PlotMapInfo
     * @return
     */

    public static Plot getPlotById(IPlayer player, PlotMapInfo pmi) {
        String id = getPlotId(player);
        return getPlotById(id, pmi);
    }

    public static void removePlot(PlotMapInfo pmi, String id) {
        if (pmi != null) {
            pmi.removePlot(id);
        }
    }

    public static void setOwnerSign(World world, Plot plot) {
        String id = plot.getId();
        String line1 = "ID: " + id;
        String line2 = "";
        String line3 = plot.getOwner();
        String line4 = "";
        getGenManager(world).setOwnerDisplay(world, plot.getId(), line1, line2, line3, line4);
    }

    public static boolean isPlotAvailable(String id, PlotMapInfo pmi) {

        return pmi != null && pmi.getPlot(id) == null;
    }

    public static String getPlotId(ILocation location) {
        if (getGenManager(location.getWorld()) == null) {
            return "";
        } else {
            return getGenManager(location.getWorld()).getPlotId(location);
        }

    }

    public static String getPlotId(IPlayer player) {
        if (getGenManager(player.getWorld()) == null) {
            return "";
        } else {
            return getGenManager(player.getWorld()).getPlotId(player);
        }

    }

    public static ILocation getPlotBottomLoc(World world, String id) {
        return getGenManager(world).getPlotBottomLoc(world, id);
    }

    public static ILocation getPlotTopLoc(World world, String id) {
        return getGenManager(world).getPlotTopLoc(world, id);
    }

    public static void removeOwnerSign(World world, String id) {
        getGenManager(world).removeOwnerDisplay(world, id);
    }

    public static void removeSellSign(World world, String id) {
        getGenManager(world).removeSellerDisplay(world, id);
    }

    public static void removeAuctionSign(World world, String id) {
        getGenManager(world).removeAuctionDisplay(world, id);
    }

    public static boolean isValidId(World world, String id) {
        return getGenManager(world).isValidId(id);
    }

    public static int bottomX(String id, World world) {
        return getGenManager(world).bottomX(id, world);
    }

    public static int topX(String id, World world) {
        return getGenManager(world).topX(id, world);
    }

    public static int bottomZ(String id, World world) {
        return getGenManager(world).bottomZ(id, world);
    }

    public static int topZ(String id, World world) {
        return getGenManager(world).topZ(id, world);
    }

    public static ILocation getPlotHome(World world, String id) {
        return getGenManager(world).getPlotHome(world, id);
    }

    public static List<IPlayer> getPlayersInPlot(World world, String id) {
        return getGenManager(world).getPlayersInPlot(id);
    }

    public static IPlotMe_GeneratorManager getGenManager(World world) {
        if (world.isPlotMeGenerator()) {
            return world.getGenerator().getManager();
        } else {
            return null;
        }
    }

    @Deprecated
    public short getNbOwnedPlot(UUID uuid, String name, String world) {
        return plugin.getSqlManager().getPlotCount(world, uuid, name);
    }

    /**
     * Checks if the plotworld has economy features enabled
     * @param world worldname
     * @return true if economy enabled
     */
    private boolean isEconomyEnabled(String world) {
        PlotMapInfo pmi = getMap(world.toLowerCase());
        return isEconomyEnabled(pmi);
    }

    /**
     * Checks if the plotworld has economy features enabled
     *
     * @param pmi plotmapinfo
     * @return true if economy enabled
     */
    public boolean isEconomyEnabled(PlotMapInfo pmi) {
        if (!plugin.getServerBridge().getConfig().getBoolean("globalUseEconomy") || plugin.getServerBridge().getEconomy() == null) {
            return false;
        }
        if (pmi == null) {
            return false;
        } else if (pmi.isUseEconomy() && plugin.getServerBridge().getConfig().getBoolean("globalUseEconomy")
                   && plugin.getServerBridge().getEconomy() != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the plotworld has economy features enabled
     * @param world world
     * @return true if economy enabled
     */

    public boolean isEconomyEnabled(World world) {
        return isEconomyEnabled(world.getName().toLowerCase());
    }

    public PlotMapInfo getMap(World world) {
        if (world == null) {
            return null;
        } else {
            String worldname = world.getName().toLowerCase();
            return getMap(worldname);
        }
    }

    public PlotMapInfo getMap(String world) {
        return getPlotMaps().get(world.toLowerCase());
    }

    public PlotMapInfo getMap(ILocation location) {
        String worldname = location.getWorld().getName().toLowerCase();
        return getMap(worldname);
    }

    public PlotMapInfo getMap(IPlayer player) {
        String world = player.getWorld().getName().toLowerCase();
        return getMap(world);
    }

/*
    public static void adjustLinkedPlots(String id, IWorld world) {
        //TODO
        Map<String, Plot> plots = new HashMap<>(); //getPlots(world);

        IPlotMe_GeneratorManager genMan = getGenMan(world);

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
                genMan.fillroad(p01.getId(), p11.getId(), world);
            }

            if (p10 != null && p10.getOwner().equalsIgnoreCase(p11.getOwner())) {
                genMan.fillroad(p10.getId(), p11.getId(), world);
            }

            if (p12 != null && p12.getOwner().equalsIgnoreCase(p11.getOwner())) {
                genMan.fillroad(p12.getId(), p11.getId(), world);
            }

            if (p21 != null && p21.getOwner().equalsIgnoreCase(p11.getOwner())) {
                genMan.fillroad(p21.getId(), p11.getId(), world);
            }

            if (p00 != null && p10 != null && p01 != null
                        && p00.getOwner().equalsIgnoreCase(p11.getOwner())
                        && p11.getOwner().equalsIgnoreCase(p10.getOwner())
                        && p10.getOwner().equalsIgnoreCase(p01.getOwner())) {
                genMan.fillmiddleroad(p00.getId(), p11.getId(), world);
            }

            if (p10 != null && p20 != null && p21 != null
                        && p10.getOwner().equalsIgnoreCase(p11.getOwner())
                        && p11.getOwner().equalsIgnoreCase(p20.getOwner())
                        && p20.getOwner().equalsIgnoreCase(p21.getOwner())) {
                genMan.fillmiddleroad(p20.getId(), p11.getId(), world);
            }

            if (p01 != null && p02 != null && p12 != null
                        && p01.getOwner().equalsIgnoreCase(p11.getOwner())
                        && p11.getOwner().equalsIgnoreCase(p02.getOwner())
                        && p02.getOwner().equalsIgnoreCase(p12.getOwner())) {
                genMan.fillmiddleroad(p02.getId(), p11.getId(), world);
            }

            if (p12 != null && p21 != null && p22 != null
                        && p12.getOwner().equalsIgnoreCase(p11.getOwner())
                        && p11.getOwner().equalsIgnoreCase(p21.getOwner())
                        && p21.getOwner().equalsIgnoreCase(p22.getOwner())) {
                genMan.fillmiddleroad(p22.getId(), p11.getId(), world);
            }

        }
    }
*/

    public Plot getPlotById(String id, World world) {
        return getPlotById(id, world.getName());
    }

    public Plot getPlotById(String id, String name) {
        PlotMapInfo pmi = getMap(name.toLowerCase());

        if (pmi == null) {
            return null;
        }

        return pmi.getPlot(id);
    }

    public Plot getPlotById(String id, IPlayer player) {
        PlotMapInfo pmi = getMap(player.getWorld());

        if (pmi == null) {
            return null;
        }

        return pmi.getPlot(id);
    }

    public Plot getPlotById(IPlayer player) {
        PlotMapInfo pmi = getMap(player);
        String id = getPlotId(player);

        if (pmi == null || id.isEmpty()) {
            return null;
        }

        return pmi.getPlot(id);
    }

    public void removePlot(World world, String id) {
        PlotMapInfo pmi = getMap(world);

        if (pmi != null) {
            pmi.removePlot(id);
        }
    }

    /**
     * Plot to add to loaded plotmap.
     * @param world world
     * @param id ID
     * @param plot Plot to be added
     */
    public void addPlot(World world, String id, Plot plot) {
        PlotMapInfo pmi = getMap(world);

        if (pmi != null) {
            pmi.addPlot(id, plot);
            plugin.getServerBridge().getEventFactory().callPlotLoadedEvent(plugin, world, plot);
        }
    }

    public void addPlot(World world, String id, Plot plot, PlotMapInfo pmi) {
        if (pmi != null) {
            pmi.addPlot(id, plot);
            plugin.getServerBridge().getEventFactory().callPlotLoadedEvent(plugin, world, plot);
        }
    }

    /**
     * Get the first plotworld defined in config
     * @return plotworld
     */
    public World getFirstWorld() {
        String firstWorld = null;
        try {
            firstWorld = (String) getPlotMaps().keySet().toArray()[0];
        } catch (ArrayIndexOutOfBoundsException error) {
            plugin.getLogger().warning("Uh oh. You don't have any plotworlds so there is gonna be a bunch of errors now.");
        }
        return plugin.getServerBridge().getWorld(firstWorld);
    }

    /**
     * Checks if world is a PlotWorld
     *
     * @param world world to be checked
     * @return true if world is plotworld, false otherwise
     */
    public boolean isPlotWorld(World world) {
        if (world == null || getGenManager(world) == null) {
            return false;
        }
        return getPlotMaps().containsKey(world.getName().toLowerCase());
    }

    /**
     * Checks if location is a PlotWorld
     *
     * @param location location to be checked
     * @return true if world is plotworld, false otherwise
     */
    public boolean isPlotWorld(ILocation location) {
        return isPlotWorld(location.getWorld());
    }

    public boolean isPlotWorld(IPlayer player) {
        return isPlotWorld(player.getWorld());
    }

    public boolean isPlotWorld(IBlock block) {
        return isPlotWorld(block.getWorld());
    }

    public Plot createPlot(World world, String id, String owner, UUID uuid, PlotMapInfo pmi) {
        if (isPlotAvailable(id, pmi) && !id.isEmpty()) {
            Plot plot = new Plot(plugin, owner, uuid, world, id, pmi.getDaysToExpiration());

            setOwnerSign(world, plot);

            addPlot(world, id, plot, pmi);

            plugin.getSqlManager().addPlot(plot, getIdX(id), getIdZ(id), world);
            return plot;
        } else {
            return null;
        }
    }

    public boolean movePlot(World world, String idFrom, String idTo) {

        if (!getGenManager(world).movePlot(world, idFrom, idTo)) {
            return false;
        }

        Plot plot1 = getPlotById(idFrom, world);
        Plot plot2 = getPlotById(idTo, world);

        if (plot1 != null) {
            if (plot2 != null) {
                int idX = getIdX(idTo);
                int idZ = getIdZ(idTo);
                plugin.getSqlManager().deletePlot(idX, idZ, world.getName());
                removePlot(world, idFrom);
                removePlot(world, idTo);
                idX = getIdX(idFrom);
                idZ = getIdZ(idFrom);
                plugin.getSqlManager().deletePlot(idX, idZ, world.getName());

                plot2.setId(idFrom);
                plugin.getSqlManager()
                        .addPlot(plot2, idX, idZ, topX(idFrom, world), bottomX(idFrom, world), topZ(idFrom, world), bottomZ(idFrom, world));
                addPlot(world, idFrom, plot2);

                HashMap<String, UUID> allowed = plot2.allowed().getAllPlayers();
                for (String player : allowed.keySet()) {
                    plugin.getSqlManager().addPlotAllowed(player, allowed.get(player), idX, idZ, world.getName());
                }

                HashMap<String, UUID> denied = plot2.denied().getAllPlayers();
                for (String player : denied.keySet()) {
                    plugin.getSqlManager().addPlotDenied(player, denied.get(player), idX, idZ, world.getName());
                }

                idX = getIdX(idTo);
                idZ = getIdZ(idTo);
                plot1.setId(idTo);
                plugin.getSqlManager().addPlot(plot1, idX, idZ, topX(idTo, world), bottomX(idTo, world), topZ(idTo, world), bottomZ(idTo, world));
                addPlot(world, idTo, plot1);

                allowed = plot1.allowed().getAllPlayers();
                for (String player : allowed.keySet()) {
                    plugin.getSqlManager().addPlotAllowed(player, allowed.get(player), idX, idZ, world.getName());
                }

                denied = plot1.denied().getAllPlayers();
                for (String player : denied.keySet()) {
                    plugin.getSqlManager().addPlotDenied(player, denied.get(player), idX, idZ, world.getName());
                }

                setOwnerSign(world, plot1);
                setSellSign(world, plot1);
                setOwnerSign(world, plot2);
                setSellSign(world, plot2);

            }
        } else if (plot2 != null) {
            int idX = getIdX(idTo);
            int idZ = getIdZ(idTo);
            plugin.getSqlManager().deletePlot(idX, idZ, world.getName());
            removePlot(world, idTo);

            idX = getIdX(idFrom);
            idZ = getIdZ(idFrom);
            plot2.setId(idFrom);
            plugin.getSqlManager().addPlot(plot2, idX, idZ, topX(idFrom, world), bottomX(idFrom, world), topZ(idFrom, world), bottomZ(idFrom, world));
            addPlot(world, idFrom, plot2);

            HashMap<String, UUID> allowed = plot2.allowed().getAllPlayers();
            for (String player : allowed.keySet()) {
                plugin.getSqlManager().addPlotAllowed(player, allowed.get(player), idX, idZ, world.getName());
            }

            HashMap<String, UUID> denied = plot2.denied().getAllPlayers();
            for (String player : denied.keySet()) {
                plugin.getSqlManager().addPlotDenied(player, denied.get(player), idX, idZ, world.getName());
            }

            setOwnerSign(world, plot2);
            setSellSign(world, plot2);
            removeOwnerSign(world, idTo);
            getGenManager(world).removeSellerDisplay(world, idTo);
        }

        return true;
    }

    /**
     * Remove any LWC Data that may be on the plot.
     * @param world Plotworld
     * @param id Plot ID
     */
    public void removeLWC(World world, String id) {
        ILocation bottom = getGenManager(world).getBottom(world, id);
        ILocation top = getGenManager(world).getTop(world, id);
        final int x1 = bottom.getBlockX();
        final int y1 = bottom.getBlockY();
        final int z1 = bottom.getBlockZ();
        final int x2 = top.getBlockX();
        final int y2 = top.getBlockY();
        final int z2 = top.getBlockZ();
        final String worldName = world.getName();

        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                LWC lwc = LWC.getInstance();
                List<Protection> protections = lwc.getPhysicalDatabase().loadProtections(worldName, x1, x2, y1, y2, z1, z2);

                for (Protection protection : protections) {
                    protection.remove();
                }
            }
        });
    }

    public void setSellSign(World world, Plot plot) {
        String id = plot.getId();

        getGenManager(world).removeSellerDisplay(world, id);

        if (plot.isForSale() || plot.isAuctioned()) {
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

            getGenManager(world).setSellerDisplay(world, plot.getId(), line1, line2, line3, line4);

            line1 = "";
            line2 = "";
            line3 = "";
            line4 = "";

            if (plot.isAuctioned()) {
                line1 = Util().C("SignOnAuction");
                if (plot.getCurrentBidder() == null) {
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

            getGenManager(world).setAuctionDisplay(world, plot.getId(), line1, line2, line3, line4);
        }
    }

    public void clear(World world, Plot plot, ICommandSender sender, ClearReason reason) {
        String id = plot.getId();

        String worldName = world.getName().toLowerCase();

        ILocation bottom = getGenManager(world).getBottom(world, id);
        ILocation top = getGenManager(world).getTop(world, id);

        if (getMap(worldName).isUseProgressiveClear()) {
            plugin.addPlotToClear(new PlotToClear(worldName, id, reason));
        } else {
            getGenManager(world).clear(bottom, top);
            if (plugin.getServerBridge().getUsinglwc()) {
                removeLWC(world, id);
            }
            sender.sendMessage(Util().C("MsgPlotCleared"));
        }
    }

    public boolean isPlotAvailable(String id, World world) {
        return isPlotAvailable(id, world.getName().toLowerCase());
    }

    public boolean isPlotAvailable(String id, IPlayer player) {
        return isPlotAvailable(id, player.getWorld());
    }

    private boolean isPlotAvailable(String id, String world) {
        PlotMapInfo pmi = getMap(world);

        return pmi != null && pmi.getPlot(id) == null;
    }

    public IPlotMe_GeneratorManager getGenMan(String name) {
        return plugin.getGenManager(name);
    }

    public void adjustWall(IPlayer player) {
        World world = player.getWorld();
        String id = getPlotId(player);
        Plot plot = getPlotById(id, world);

        getGenManager(world).adjustPlotFor(world, id, true, plot.isProtect(), plot.isAuctioned(), plot.isForSale());
    }

    public void setBiome(World world, String id, IBiome biome) {
        getGenManager(world).setBiome(world, id, biome);
        plugin.getSqlManager().updatePlot(getIdX(id), getIdZ(id), world.getName(), "biome", ((BukkitBiome) biome).getBiome().name());
    }

    public HashSet<UUID> getPlayersIgnoringWELimit() {
        return playersignoringwelimit;
    }

    public void setPlayersIgnoringWELimit(HashSet<UUID> playersignoringwelimit) {
        this.playersignoringwelimit = playersignoringwelimit;
    }

    public void addPlayerIgnoringWELimit(UUID uuid) {
        getPlayersIgnoringWELimit().add(uuid);
    }

    public void removePlayerIgnoringWELimit(UUID uuid) {
        getPlayersIgnoringWELimit().remove(uuid);
    }

    /**
     * @deprecated Use {@link #isPlayerIgnoringWELimit(IPlayer)} instead
     */
    @Deprecated
    @SuppressWarnings("UnusedDeclaration")
    public boolean isPlayerIgnoringWELimit(UUID uuid) {
        if (plugin.getServerBridge().getConfig().getBoolean("defaultWEAnywhere")) {
            return getPlayersIgnoringWELimit().contains(uuid);
        }
        return getPlayersIgnoringWELimit().contains(uuid);
    }

    public HashMap<String, PlotMapInfo> getPlotMaps() {
        return plotmaps;
    }

    public void addPlotMap(String world, PlotMapInfo map) {
        getPlotMaps().put(world.toLowerCase(), map);
    }

    public void removePlotMap(String world) {
        getPlotMaps().remove(world.toLowerCase());
    }

    private Util Util() {
        return plugin.getUtil();
    }

    public boolean isPlayerIgnoringWELimit(IPlayer player) {
        if (plugin.getServerBridge().getConfig().getBoolean("defaultWEAnywhere")) {
            if (player.hasPermission(PermissionNames.ADMIN_WEANYWHERE)) {
                return !getPlayersIgnoringWELimit().contains(player.getUniqueId());
            } else {
                return getPlayersIgnoringWELimit().contains(player.getUniqueId());
            }
        } else {
            return getPlayersIgnoringWELimit().contains(player.getUniqueId());
        }
    }
}
