package com.worldcretornica.plotme_core;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.api.event.PlotLoadEvent;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.inject.Singleton;

@Singleton
public class PlotMeCoreManager {

    private static final PlotMeCoreManager INSTANCE = new PlotMeCoreManager();
    private final HashMap<IWorld, PlotMapInfo> plotmaps = new HashMap<>();
    private final HashSet<UUID> playersignoringwelimit = new HashSet<>();
    private PlotMe_Core plugin;

    private PlotMeCoreManager() {
    }

    /**
     * This is the hook into retrieving the {@link #PlotMeCoreManager()}
     * @return instance of {@link #PlotMeCoreManager()}
     */
    public static PlotMeCoreManager getInstance() {
        return INSTANCE;
    }

    void setPlugin(PlotMe_Core instance) {
        plugin = instance;
    }

    /**
     * Removes the plot from the plotmap
     * @param plot plot id
     */
    public boolean deletePlot(Plot plot) {
        removeSellSign(plot);
        removeOwnerSign(plot);
        return plugin.getSqlManager().deletePlot(plot);
    }

    /**
     * Sets the sign for the plot owner
     *
     * @param plot  plot to set sign on
     */
    public void setOwnerSign(Plot plot) {
        PlotId id = plot.getId();
        String line1 = "ID: " + id.toString();
        String line2 = "";
        String line3 = plot.getOwner();
        String line4 = "";
        getGenManager(plot.getWorld()).setOwnerDisplay(id, line1, line2, line3, line4);
    }

    /**
     * Get the id of the plot based on the location
     *
     * @param location location in the plotworld
     * @return Plot ID or an empty string if not found
     */
    public PlotId getPlotId(Location location) {
        if (getGenManager(location.getWorld()) == null) {
            return null;
        }
        return getGenManager(location.getWorld()).getPlotId(location.getVector());

    }

    /**
     * Get the id of the plot the player is standing on
     *
     * @param player player in the plotworld
     * @return Plot ID or an empty string if not found
     */
    public PlotId getPlotId(IPlayer player) {
        if (getGenManager(player.getWorld()) == null) {
            return null;
        }
        return getGenManager(player.getWorld()).getPlotId(player);

    }

    /**
     * Removes the owner sign from the plot.
     * @param plot    plot to remove the sign from
     */
    public void removeOwnerSign(Plot plot) {
        getGenManager(plot.getWorld()).removeOwnerDisplay(plot.getId());
    }

    /**
     * Remove the sell sign from the plot
     * @param plot    plot id to remove the sign from
     */
    public void removeSellSign(Plot plot) {
        getGenManager(plot.getWorld()).removeSellerDisplay(plot.getId());
    }

    /**
     * Set the sell sign on the plot
     *
     * @param plot  plot to add sign to
     */
    public void setSellSign(Plot plot) {
        String line1 = plugin.C("SignForSale");
        String line2 = plugin.C("SignPrice", plot.getPrice());
        String line4 = "/plotme buy";

        getGenManager(plot.getWorld()).setSellerDisplay(plot.getId(), line1, line2, "", line4);
    }

    /**
     * Gets the bottom corner of the plot
     *
     * @param world
     * @param id PlotID
     * @return bottom location of the plot
     */
    @Deprecated
    public Vector getPlotBottomLoc(IWorld world, PlotId id) {
        return getGenManager(world).getPlotBottomLoc(id);
    }

    /**
     * Gets the top corner of the plot
     *
     * @param world
     * @param id PlotID
     * @return top location of the plot
     */
    @Deprecated
    public Vector getPlotTopLoc(IWorld world, PlotId id) {
        return getGenManager(world).getPlotTopLoc(id);
    }

    /**
     * Get the x coordinate at the bottom of the plot
     *
     * @param id    plot id
     * @param world
     * @return bottom x coordinate of the plot
     */
    @Deprecated
    public int bottomX(PlotId id, IWorld world) {
        return getGenManager(world).bottomX(id);
    }

    /**
     * Get the x coordinate at the top of the plot
     *
     * @param id    plot id
     * @param world
     * @return top x coordinate of the plot
     */
    @Deprecated
    public int topX(PlotId id, IWorld world) {
        return getGenManager(world).topX(id);
    }

    /**
     * Get the z coordinate at the bottom of the plot
     *
     * @param id    plot id
     * @param world
     * @return bottom z coordinate of the plot
     */
    @Deprecated
    public int bottomZ(PlotId id, IWorld world) {
        return getGenManager(world).bottomZ(id);
    }

    /**
     * Get the z coordinate at the top of the plot
     *
     * @param id    plot id
     * @param world
     * @return top z coordinate of the plot
     */
    @Deprecated
    public int topZ(PlotId id, IWorld world) {
        return getGenManager(world).topZ(id);
    }

    /**
     * Get the plot home location of a plot
     *
     * @param id    plot id to get home of
     * @param world
     * @return an ILocation of the plot home location
     */
    public Location getPlotHome(PlotId id, IWorld world) {
        Location plotHome = getGenManager(world).getPlotHome(id);
        plugin.getLogger().info(plotHome.toString());
        return plotHome;
    }

    /**
     * Get the players in the Plot
     *
     * @param id    plot id
     * @param world
     * @return a list of players in the plot
     */
    public List<IPlayer> getPlayersInPlot(PlotId id, IWorld world) {
        return getGenManager(world).getPlayersInPlot(id);
    }

    public IPlotMe_GeneratorManager getGenManager(IWorld world) {
        return plugin.getGenManager(world);
    }

    /**
     * Get the number of plots the player owns
     *
     * @param uuid player UUID
     * @param world plotworld
     * @return number of plots the player owns
     */
    public int getOwnedPlotCount(UUID uuid, IWorld world) {
        return plugin.getSqlManager().getPlotCount(world, uuid);
    }

    /**
     * Checks if the plotworld has economy features enabled
     *
     * @param pmi plotmapinfo
     * @return true if economy enabled
     */
    public boolean isEconomyEnabled(PlotMapInfo pmi) {
        if (plugin.getConfig().getBoolean("globalUseEconomy") && plugin.getServerBridge().getEconomy().isPresent()) {
            return pmi.canUseEconomy();
        }
        return false;
    }

    /**
     * Checks if the plotworld has economy features enabled
     *
     * @param world world
     * @return true if economy enabled
     */

    public boolean isEconomyEnabled(IWorld world) {
        PlotMapInfo pmi = getMap(world);
        return isEconomyEnabled(pmi);
    }

    /**
     * Get the PlotMap based on the world given
     * @param world plotworld
     * @return PlotMapInfo for the plotworld, if the world doesn't exist then it will return null
     */
    public PlotMapInfo getMap(IWorld world) {
        return getPlotMaps().get(world);
    }

    /**
     * Get the PlotMap based on the world given
     * @param location the location in a plotworld
     * @return PlotMapInfo for the plotworld, if the world doesn't exist then it will return null
     */
    public PlotMapInfo getMap(Location location) {
        return getMap(location.getWorld());
    }

    /**
     * Get the PlotMap based on the world given
     * @param player a player in a plotworld
     * @return PlotMapInfo for the plotworld, if the world doesn't exist then it will return null
     */
    public PlotMapInfo getMap(IEntity player) {
        return getMap(player.getWorld());
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
                genMan.fillRoad(p01.getId(), p11.getId(), world);
            }

            if (p10 != null && p10.getOwner().equalsIgnoreCase(p11.getOwner())) {
                genMan.fillRoad(p10.getId(), p11.getId(), world);
            }

            if (p12 != null && p12.getOwner().equalsIgnoreCase(p11.getOwner())) {
                genMan.fillRoad(p12.getId(), p11.getId(), world);
            }

            if (p21 != null && p21.getOwner().equalsIgnoreCase(p11.getOwner())) {
                genMan.fillRoad(p21.getId(), p11.getId(), world);
            }

            if (p00 != null && p10 != null && p01 != null
                        && p00.getOwner().equalsIgnoreCase(p11.getOwner())
                        && p11.getOwner().equalsIgnoreCase(p10.getOwner())
                        && p10.getOwner().equalsIgnoreCase(p01.getOwner())) {
                genMan.fillMiddleRoad(p00.getId(), p11.getId(), world);
            }

            if (p10 != null && p20 != null && p21 != null
                        && p10.getOwner().equalsIgnoreCase(p11.getOwner())
                        && p11.getOwner().equalsIgnoreCase(p20.getOwner())
                        && p20.getOwner().equalsIgnoreCase(p21.getOwner())) {
                genMan.fillMiddleRoad(p20.getId(), p11.getId(), world);
            }

            if (p01 != null && p02 != null && p12 != null
                        && p01.getOwner().equalsIgnoreCase(p11.getOwner())
                        && p11.getOwner().equalsIgnoreCase(p02.getOwner())
                        && p02.getOwner().equalsIgnoreCase(p12.getOwner())) {
                genMan.fillMiddleRoad(p02.getId(), p11.getId(), world);
            }

            if (p12 != null && p21 != null && p22 != null
                        && p12.getOwner().equalsIgnoreCase(p11.getOwner())
                        && p11.getOwner().equalsIgnoreCase(p21.getOwner())
                        && p21.getOwner().equalsIgnoreCase(p22.getOwner())) {
                genMan.fillMiddleRoad(p22.getId(), p11.getId(), world);
            }

        }
    }
*/


    /**
     * Gets the plot with the given id in the given world.
     *
     * @param id plot id
     * @param world
     * @return plot
     */
    public Plot getPlotById(PlotId id, IWorld world) {
        if (world == null || id == null) {
            return null;
        }
        return plugin.getSqlManager().getPlot(id, world);
    }

    /**
     * Plot to add to loaded plotmap.
     *  @param plot Plot to be added
     */
    public void loadPlot(Plot plot) {
        PlotLoadEvent event = new PlotLoadEvent(plot);
        plugin.getEventBus().post(event);

    }

    /**
     * Get the first plotworld defined in config
     *
     * @return plotworld
     */
    public IWorld getFirstWorld() {
        return (IWorld) getPlotMaps().keySet().toArray()[0];
    }

    /**
     * Checks if world is a PlotWorld
     *
     * @param world object to get the location from
     * @return true if world is plotworld, false otherwise
     */
    public boolean isPlotWorld(IWorld world) {
        return getPlotMaps().containsKey(world);
    }

    /**
     * Checks if location is a PlotWorld
     *
     * @param location location to be checked
     * @return true if world is plotworld, false otherwise
     */
    public boolean isPlotWorld(Location location) {
        return isPlotWorld(location.getWorld());
    }

    /**
     * Checks if the entity is in a plotworld
     *
     * @param entity entity to get the location from
     * @return true if world is plotworld, false otherwise
     */
    public boolean isPlotWorld(IEntity entity) {
        return isPlotWorld(entity.getWorld());
    }

    /**
     * Creates a new plot
     *
     * @param id    plot id
     * @param world
     * @param owner owner name
     * @param uuid  owner uuid
     * @param pmi   plotmap to add the plot to    @return the new plot created
     *
     * @throws NullPointerException If the <code>id</code> argument is <code>null</code>
     */
    public Plot createPlot(PlotId id, IWorld world, String owner, UUID uuid, PlotMapInfo pmi) {

        Plot plot = new Plot(owner, uuid, world, id, this.getPlotTopLoc(world, id), this.getPlotBottomLoc(world, id));
        if (pmi.getDaysToExpiration() == 0) {
            plot.setExpiredDate(null);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, pmi.getDaysToExpiration());
            java.util.Date utlDate = cal.getTime();
            plot.setExpiredDate(new Date(utlDate.getTime()));
        }


        setOwnerSign(plot);
        loadPlot(plot);
        adjustWall(plot, true);

        plugin.getSqlManager().addPlot(plot);
        return plot;
    }

    /**
     * Move a plot from one location to another
     *
     *
     * @param world
     * @param idFrom the id of the plot to be moved
     * @param idTo   the id the plot will be moved to
     * @return true if successful, false otherwise
     */
    public boolean movePlot(IWorld world, PlotId idFrom, PlotId idTo) {

        if (!getGenManager(world).movePlot(idFrom, idTo)) {
            return false;
        }

        Plot plotFrom = getPlotById(idFrom, world);
        Plot plotTo = getPlotById(idTo, world);

        if (plotFrom != null) {
            if (plotTo != null) {
                deletePlot(plotFrom);
                deletePlot(plotTo);
                plotTo.setId(idFrom);
                plugin.getSqlManager().addPlot(plotTo);
                loadPlot(plotTo);

                plotFrom.setId(idTo);
                plugin.getSqlManager().addPlot(plotFrom);
                loadPlot(plotFrom);

                setOwnerSign(plotFrom);
                setOwnerSign(plotTo);
            } else {
                movePlotToEmpty(plotFrom, idTo);
            }
        } else if (plotTo != null) {
            movePlotToEmpty(plotTo, idFrom);
        }

        return true;
    }

    /**
     * Move a plot to an spot where there is no plot existing.
     */
    private void movePlotToEmpty(Plot filledPlot, PlotId idDestination) {
        deletePlot(filledPlot);

        filledPlot.setId(idDestination);
        plugin.getSqlManager().addPlot(filledPlot);
        loadPlot(filledPlot);

        setOwnerSign(filledPlot);
        setSellSign(filledPlot);
    }

    /**
     * Remove any LWC Data that may be on the plot.
     */
    public void removeLWC(final Plot plot) {
        final int x1 = plot.getBottomX();
        final int z1 = plot.getBottomZ();
        final int x2 = plot.getTopX();
        final int z2 = plot.getBottomZ();

        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                LWC lwc = LWC.getInstance();
                List<Protection> protections = lwc.getPhysicalDatabase().loadProtections(plot.getWorld().getName(), x1, x2, 0, 256, z1, z2);

                for (Protection protection : protections) {
                    protection.remove();
                }
            }
        });
    }

    /**
     * Clears a plot
     *  @param plot   the plot to be cleared
     * @param sender the sender of the command
     * @param reason The reason they will be cleared. The cause can be: EXPIRED, RESET, CLEAR
     */
    public void clear(Plot plot, ICommandSender sender, ClearReason reason) {
        if (plugin.getServerBridge().isUsingLwc()) {
            removeLWC(plot);
        }
        getGenManager(plot.getWorld()).clearEntities(plot.getPlotBottomLoc(), plot.getPlotTopLoc());
        if (reason.equals(ClearReason.Clear)) {
            adjustWall(plot, true);
        } else {
            adjustWall(plot, false);
        }
        plugin.addPlotToClear(plot, reason, sender);
    }

    /**
     * Checks if the plot is claimed or not
     *
     * @param id    the plot id to be checked
     * @param world
     * @return true if the plot is unclaimed, false otherwise
     */
    public boolean isPlotAvailable(PlotId id, IWorld world) {
        for (Plot plot : plugin.getSqlManager().getPlots()) {
            if (plot.getId().equals(id) && plot.getWorld().equals(world)) {
                return false;
            }
        }
        if (getPlotTopLoc(world, id).getX() > world.getWorldBorder().minX()) {
            if (getPlotBottomLoc(world, id).getX() < world.getWorldBorder().maxX()) {
                if (getPlotTopLoc(world, id).getZ() > world.getWorldBorder().minZ()) {
                    if (getPlotBottomLoc(world, id).getZ() < world.getWorldBorder().maxZ()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Updates the blocks on the plot border
     *
     * @param player the player in the plot
     */
    public void adjustWall(IPlayer player) {
        Plot plot = getPlot(player);
        if (plot == null) {
            player.sendMessage(plugin.C("NoPlotFound"));
        } else {
            getGenManager(player.getWorld()).adjustPlotFor(plot, true, plot.isProtected(), plot.isForSale());
        }
    }

    /**
     * Updates the blocks on the plot border
     * @param plot      plot id
     * @param claimed is the plot claimed
     */
    public void adjustWall(Plot plot, boolean claimed) {
        getGenManager(plot.getWorld()).adjustPlotFor(plot, claimed, plot.isProtected(), plot.isForSale());
    }

    public void setBiome(Plot plot) {
        getGenManager(plot.getWorld()).setBiome(plot.getId(), plot.getBiome());
    }


    /**
     * Gets all the players that can use WorldEdit Anywhere in plotworld
     *
     * @return a list of the uuid's of players able to WorldEdit Anywhere
     */
    public HashSet<UUID> getPlayersIgnoringWELimit() {
        return playersignoringwelimit;
    }

    /**
     * Gives a user the ability to use WorldEdit anywhere in plotworld
     *
     * @param uuid uuid of the player
     */
    public void addPlayerIgnoringWELimit(UUID uuid) {
        getPlayersIgnoringWELimit().add(uuid);
    }

    /**
     * Removes the ability for a user to use WorldEdit anywhere in plotworld
     *
     * @param uuid uuid of the player
     */
    public void removePlayerIgnoringWELimit(UUID uuid) {
        getPlayersIgnoringWELimit().remove(uuid);
    }


    /**
     * Gets the active plotworlds
     *
     * @return the active plotworlds
     */
    public HashMap<IWorld, PlotMapInfo> getPlotMaps() {
        return plotmaps;
    }

    /**
     * Register the plotworld the plotmap
     *  @param world name of a plotworld
     * @param map   {@link PlotMapInfo} information
     */
    public void addPlotMap(IWorld world, PlotMapInfo map) {
        getPlotMaps().put(world, map);
    }


    public boolean isPlayerIgnoringWELimit(IPlayer player) {
        if (plugin.getConfig().getBoolean("defaultWEAnywhere") && player.hasPermission(PermissionNames.ADMIN_WEANYWHERE)) {
            return !getPlayersIgnoringWELimit().contains(player.getUniqueId());
        } else {
            return getPlayersIgnoringWELimit().contains(player.getUniqueId());
        }
    }

    /**
     * Gets the location of the middle of the plot
     *
     * @param world plotworld
     * @param id    plot id
     * @return location as an ILocation
     */
    public Vector getPlotMiddle(IWorld world, PlotId id) {
        return getGenManager(world).getPlotMiddle(id);
    }

    public void UpdatePlayerNameFromId(final UUID uuid, final String name) {
        for (final Plot plot : plugin.getSqlManager().getPlayerPlots(uuid)) {
            setOwnerSign(plot);
        }
    }


    public IOfflinePlayer getPlayer(String name) {
        return plugin.getServerBridge().getPlayer(name);
    }

    public Plot getPlot(Location location) {
        PlotId id = getPlotId(location);
        if (id == null) {
            return null;
        }
        return getPlotById(id, location.getWorld());
    }

    public Plot getPlot(IPlayer player) {
        PlotId id = getPlotId(player);
        if (id == null) {
            return null;
        }
        return getPlotById(id, player.getWorld());
    }

    public IWorld getWorld(String world) {
        for (IWorld iw : getPlotMaps().keySet()) {
            if (iw.getName().equalsIgnoreCase(world)) {
                return iw;
            }
        }
        return null;
    }


}