package com.worldcretornica.plotme_core;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.api.event.PlotLoadEvent;
import com.worldcretornica.plotme_core.storage.Database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.inject.Singleton;

@Singleton
public class PlotMeCoreManager {

    private static final PlotMeCoreManager INSTANCE = new PlotMeCoreManager();
    private final HashMap<IWorld, PlotMapInfo> plotmaps;
    private PlotMe_Core plugin;
    private HashSet<UUID> playersignoringwelimit = new HashSet<>();

    private PlotMeCoreManager() {
        plotmaps = new HashMap<>();
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

    public Database getSQLManager() {
        return plugin.getSqlManager();
    }

    /**
     * Removes the plot from the plotmap
     * @param pmi plotmap
     * @param plot plot id
     */
    public void deletePlot(PlotMapInfo pmi, Plot plot) {
        removeSellSign(plot, pmi.getWorld());
        removeOwnerSign(plot, pmi.getWorld());

        pmi.removePlot(plot.getId());
    }

    /**
     * Sets the sign for the plot owner
     *
     * @param world
     * @param plot  plot to set sign on
     */
    public void setOwnerSign(IWorld world, Plot plot) {
        PlotId id = plot.getId();
        String line1 = "ID: " + id.toString();
        String line2 = "";
        String line3 = plot.getOwner();
        String line4 = "";
        getGenManager(world).setOwnerDisplay(id, line1, line2, line3, line4);
    }

    /**
     * Get the id of the plot based on the location
     *
     * @param location location in the plotworld
     * @return Plot ID or an empty string if not found
     */
    public PlotId getPlotId(ILocation location) {
        if (getGenManager(location.getWorld()) == null) {
            return null;
        }
        return getGenManager(location.getWorld()).getPlotId(location.getPos());

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
     * @param id    plot id to remove the sign from
     */
    public void removeOwnerSign(Plot plot, IWorld world) {
        getGenManager(world).removeOwnerDisplay(plot.getId());
    }

    /**
     * Remove the sell sign from the plot
     * @param plot    plot id to remove the sign from
     * @param world
     */
    public void removeSellSign(Plot plot, IWorld world) {
        getGenManager(world).removeSellerDisplay(plot.getId());
    }

    /**
     * Set the sell sign on the plot
     *
     * @param plot  plot to add sign to
     * @param world
     */
    public void setSellSign(Plot plot, IWorld world) {
        String line1 = plugin.C("SignForSale");
        String line2 = plugin.C("SignPrice");
        String line3 = String.valueOf(plot.getPrice());
        String line4 = "/plotme buy";

        getGenManager(world).setSellerDisplay(plot.getId(), line1, line2, line3, line4);
    }

    /**
     * Gets the bottom corner of the plot
     *
     * @param world
     * @param id PlotID
     * @return bottom location of the plot
     */
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
    public ILocation getPlotHome(PlotId id, IWorld world) {
        return getGenManager(world).getPlotHome(id);
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
    public int getOwnedPlotCount(UUID uuid, String world) {
        return plugin.getSqlManager().getPlotCount(world, uuid);
    }

    /**
     * Checks if the plotworld has economy features enabled
     *
     * @param pmi plotmapinfo
     * @return true if economy enabled
     */
    public boolean isEconomyEnabled(PlotMapInfo pmi) {
        if (!plugin.getConfig().getBoolean("globalUseEconomy") || plugin.getServerBridge().getEconomy() == null) {
            return false;
        }
        return pmi.canUseEconomy();
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
    public PlotMapInfo getMap(ILocation location) {
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
        PlotMapInfo pmi = getMap(world);

        if (pmi == null) {
            return null;
        }

        return pmi.getPlot(id);
    }

    /**
     * Gets the plot by the id and pmi
     *
     * @param id Plot ID
     * @param pmi PlotMapInfo
     * @return plot
     */
    public Plot getPlotById(PlotId id, PlotMapInfo pmi) {
        if (pmi != null) {
            return pmi.getPlot(id);
        } else {
            return null;
        }
    }

    /**
     * Plot to remove from plotmap.
     *  @param id ID
     */
    public void deletePlot(PlotId id) {
        PlotMapInfo pmi = getMap(id.getWorld());

        if (pmi != null) {
            pmi.removePlot(id);
        }
    }

    /**
     * Plot to add to loaded plotmap.
     *  @param id ID
     * @param plot Plot to be added
     * @param world
     */
    public void addPlot(PlotId id, Plot plot, IWorld world) {
        PlotMapInfo pmi = getMap(world);
        addPlot(id, plot, pmi);
    }

    /**
     * Plot to add to loaded plotmap.
     *
     * @param id ID
     * @param plot Plot to be added
     * @param pmi Plot Map to add the plot to
     */
    public void addPlot(PlotId id, Plot plot, PlotMapInfo pmi) {
        if (pmi != null) {
            pmi.addPlot(id, plot);
            PlotLoadEvent event = new PlotLoadEvent(world, plot);
            plugin.getServerBridge().getEventBus().post(event);

        }
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
    public boolean isPlotWorld(ILocation location) {
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
     *@param owner owner name
     * @param uuid  owner uuid
     * @param pmi   plotmap to add the plot to    @return the new plot created
     *
     * @throws NullPointerException If the <code>id</code> argument is <code>null</code>
     */
    public Plot createPlot(PlotId id, IWorld world, String owner, UUID uuid, PlotMapInfo pmi) {
        Plot plot = new Plot(owner, uuid, world, id, pmi.getDaysToExpiration());

        setOwnerSign(world, plot);
        addPlot(id, plot, pmi);
        adjustWall(id, world, true);

        plugin.getSqlManager().addPlot(plot, getPlotTopLoc(world, id), getPlotBottomLoc(world, id));
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
                plugin.getSqlManager().deletePlot(plotTo.getInternalID());
                deletePlot(idFrom);
                deletePlot(idTo);
                plugin.getSqlManager().deletePlot(plotFrom.getInternalID());

                plotTo.setId(idFrom);
                plugin.getSqlManager().addPlot(plotTo, getPlotTopLoc(world, idFrom), getPlotBottomLoc(world, idFrom));
                addPlot(idFrom, plotTo, world);

                plotFrom.setId(idTo);
                plugin.getSqlManager().addPlot(plotFrom, getPlotTopLoc(world, idTo), getPlotBottomLoc(world, idTo));
                addPlot(idTo, plotFrom, world);

                setOwnerSign(world, plotFrom);
                removeSellSign(plotFrom.getId(), world);
                setOwnerSign(world, plotTo);
                removeSellSign(plotTo.getId(), world);

            } else {
                movePlotToEmpty(world, plotFrom, idTo);
            }
        } else if (plotTo != null) {
            movePlotToEmpty(world, plotTo, idFrom);
        }

        return true;
    }

    /**
     * Move a plot to an spot where there is no plot existing.
     */
    private void movePlotToEmpty(IWorld world, Plot filledPlot, PlotId idDestination) {
        PlotId idFrom = filledPlot.getId();
        plugin.getSqlManager().deletePlot(filledPlot.getInternalID());
        deletePlot(idFrom);

        filledPlot.setId(idDestination);
        plugin.getSqlManager()
                .addPlot(filledPlot, getPlotTopLoc(world, idDestination), getPlotBottomLoc(world, idDestination));
        addPlot(idDestination, filledPlot, world);

        setOwnerSign(world, filledPlot);
        setSellSign(filledPlot, world);
        removeOwnerSign(idFrom, world);
        removeSellSign(idFrom, world);
    }

    /**
     * Remove any LWC Data that may be on the plot.
     * @param world
     * @param id Plot ID
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
                List<Protection> protections = lwc.getPhysicalDatabase().loadProtections(plot.getWorld(), x1, x2, 0, 256, z1, z2);

                for (Protection protection : protections) {
                    protection.remove();
                }
            }
        });
    }

    /**
     * Clears a plot
     *  @param plot   the plot to be cleared
     * @param world
     * @param sender the sender of the command
     * @param reason The reason they will be cleared. The cause can be: EXPIRED, RESET, CLEAR
     */
    public void clear(Plot plot, IWorld world, ICommandSender sender, ClearReason reason) {
        PlotId id = plot.getId();
        if (plugin.getServerBridge().isUsingLwc()) {
            removeLWC(plot);
        }
        if (reason.equals(ClearReason.Clear)) {
            adjustWall(plot, world, true);
        } else {
            adjustWall(plot, world, false);
        }
        plugin.addPlotToClear(new PlotToClear(plot, id, world, reason, sender));
    }

    /**
     * Checks if the plot is claimed or not
     *
     * @param id    the plot id to be checked
     * @param pmi plotmap of the plotworld
     * @return true if the plot is unclaimed, false otherwise
     */

    public boolean isPlotAvailable(PlotId id, PlotMapInfo pmi) {
        if (pmi != null) {
            if (pmi.getPlot(id) == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the plot is claimed or not
     *
     * @param id    the plot id to be checked
     * @param world
     * @return true if the plot is unclaimed, false otherwise
     */
    public boolean isPlotAvailable(PlotId id, IWorld world) {
        PlotMapInfo pmi = getMap(world);

        return pmi != null && pmi.getPlot(id) == null;
    }

    /**
     * Updates the blocks on the plot border
     *
     * @param player the player in the plot
     */
    public void adjustWall(IPlayer player) {
        PlotId id = getPlotId(player);
        if (id == null) {
            player.sendMessage(plugin.C("MsgNoPlotFound"));
        } else {
            Plot plot = getPlotById(id, player.getWorld());

            getGenManager(player.getWorld()).adjustPlotFor(id, true, plot.isProtected(), plot.isForSale());
        }
    }

    /**
     * Updates the blocks on the plot border
     * @param plot      plot id
     * @param world
     * @param claimed is the plot claimed
     */
    public void adjustWall(Plot plot, IWorld world, boolean claimed) {
        getGenManager(world).adjustPlotFor(plot.getId(), claimed, plot.isProtected(), plot.isForSale());
    }
    public void setBiome(PlotId id, String biome) {
        plugin.getSqlManager().updatePlot(id, id.getWorld().getName(), "biome", biome.toUpperCase());
    }


    /**
     * Gets all the players that can use WorldEdit Anywhere in plotworld
     *
     * @return a list of the uuid's of players able to WorldEdit Anywhere
     */
    public HashSet<UUID> getPlayersIgnoringWELimit() {
        return playersignoringwelimit;
    }

    public void setPlayersIgnoringWELimit(HashSet<UUID> playersignoringwelimit) {
        this.playersignoringwelimit = playersignoringwelimit;
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
        plugin.getSqlManager().updatePlotsNewUUID(uuid, name);

        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                for (PlotMapInfo pmi : plotmaps.values()) {
                    for (Plot plot : pmi.getLoadedPlots().values()) {

                        //Owner
                        if (plot.getOwnerId().equals(uuid)) {
                            plot.setOwner(name);
                        }
                    }
                }
            }
        });
    }

    /**
     * Gets the value of that plot property
     *
     * @param id PlotID
     * @param pluginname Name of the plugin owning this property
     * @param property Name of the property to get the value of
     * @return Value of the property
     */
    public String getPlotProperty(PlotId id, IWorld world, String pluginname, String property) {
        Plot plot = getPlotById(id, world);
        if (plot == null) {
            return null;
        } else {
            return getPlotProperty(plot, pluginname, property);
        }
    }

    /**
     * Gets the value of that plot property
     *
     * @param plot Plot to get the property from
     * @param pluginname Name of the plugin owning this property
     * @param property Name of the property to get the value of
     * @return Value of the property
     */
    public String getPlotProperty(Plot plot, String pluginname, String property) {
        return plot.getPlotProperty(pluginname, property);
    }

    /**
     * Sets the value of that plot property
     *
     * @param id PlotID
     * @param pluginname Name of the plugin owning this property
     * @param property Name of the property
     * @param value Value of the property
     * @return If the property was set successfully
     */
    public boolean setPlotProperty(PlotId id, IWorld world, String pluginname, String property, String value) {
        Plot plot = getPlotById(id, world);
        return plot != null && plot.setPlotProperty(pluginname, property, value);
    }

    /**
     * Sets the value of that plot property
     *
     * @param plot Plot to set the property
     * @param pluginname Name of the plugin owning this property
     * @param property Name of the property
     * @param value Value of the property
     * @return If the property was set successfully
     */
    public boolean setPlotProperty(Plot plot, String pluginname, String property, String value) {
        return plot.setPlotProperty(pluginname, property, value);
    }

    public IOfflinePlayer getOfflinePlayer(String name) {
        return plugin.getServerBridge().getOfflinePlayer(name);
    }

    public Plot getPlot(ILocation location) {
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

    public boolean isPlotAvailable(ILocation location) {
        PlotId id = getPlotId(location);
        if (id != null) {
            if (getPlotById(id, location) == null) {
                return true;
            }
        }
        return false;
    }

    private Plot getPlotById(PlotId id, ILocation location) {
        return getPlotById(id, location.getWorld());
    }
}