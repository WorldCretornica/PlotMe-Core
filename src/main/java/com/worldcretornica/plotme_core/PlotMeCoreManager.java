package com.worldcretornica.plotme_core;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotLoadEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.inject.Singleton;

@Singleton
public class PlotMeCoreManager {

    private static final PlotMeCoreManager INSTANCE = new PlotMeCoreManager();
    private final HashMap<String, PlotMapInfo> plotmaps;
    private PlotMe_Core plugin;
    private HashSet<UUID> playersignoringwelimit;

    private PlotMeCoreManager() {
        setPlayersIgnoringWELimit(new HashSet<UUID>());
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

    /**
     * Removes the plot from the plotmap
     *  @param pmi plotmap
     * @param id plot id
     */
    public void removePlot(PlotMapInfo pmi, PlotId id) {
        pmi.removePlot(id);
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
        getGenManager(id).setOwnerDisplay(plot.getId(), line1, line2, line3, line4);
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
        return getGenManager(location.getWorld()).getPlotId(location);

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
     *  @param id    plot id to remove the sign from
     */
    public void removeOwnerSign(PlotId id) {
        getGenManager(id).removeOwnerDisplay(id);
    }

    /**
     * Remove the sell sign from the plot
     *  @param id    plot id to remove the sign from
     */
    public void removeSellSign(PlotId id) {
        getGenManager(id).removeSellerDisplay(id);
    }

    /**
     * Set the sell sign on the plot
     *
     * @param plot  plot to add sign to
     */
    public void setSellSign(Plot plot) {
        String line1 = plugin.C("SignForSale");
        String line2 = plugin.C("SignPrice");
        String line3 = String.valueOf(plot.getPrice());
        String line4 = "/plotme buy";

        getGenManager(plot.getId()).setSellerDisplay(plot.getId(), line1, line2, line3, line4);
    }

    /**
     * Gets the bottom corner of the plot
     * @param id PlotID
     * @return bottom location of the plot
     */
    public ILocation getPlotBottomLoc(PlotId id) {
        return getGenManager(id).getPlotBottomLoc(id);
    }

    /**
     * Gets the top corner of the plot
     * @param id PlotID
     * @return top location of the plot
     */
    public ILocation getPlotTopLoc(PlotId id) {
        return getGenManager(id).getPlotTopLoc(id);
    }

    /**
     * Get the x coordinate at the bottom of the plot
     *
     * @param id    plot id
     * @return bottom x coordinate of the plot
     */
    public int bottomX(PlotId id) {
        return getGenManager(id).bottomX(id);
    }

    /**
     * Get the x coordinate at the top of the plot
     *
     * @param id    plot id
     * @return top x coordinate of the plot
     */
    public int topX(PlotId id) {
        return getGenManager(id).topX(id);
    }

    /**
     * Get the z coordinate at the bottom of the plot
     *
     * @param id    plot id
     * @return bottom z coordinate of the plot
     */
    public int bottomZ(PlotId id) {
        return getGenManager(id).bottomZ(id);
    }

    /**
     * Get the z coordinate at the top of the plot
     *
     * @param id    plot id
     * @return top z coordinate of the plot
     */
    public int topZ(PlotId id) {
        return getGenManager(id).topZ(id);
    }

    /**
     * Get the plot home location of a plot
     *
     * @param id    plot id to get home of
     * @return an ILocation of the plot home location
     */
    public ILocation getPlotHome(PlotId id) {
        return getGenManager(id).getPlotHome(id);
    }

    /**
     * Get the players in the Plot
     *
     * @param id    plot id
     * @return a list of players in the plot
     */
    public List<IPlayer> getPlayersInPlot(PlotId id) {
        return getGenManager(id).getPlayersInPlot(id);
    }

    public IPlotMe_GeneratorManager getGenManager(IWorld world) {
        return plugin.getGenManager(world.getName());
    }

    public IPlotMe_GeneratorManager getGenManager(PlotId id) {
        return getGenManager(id.getWorld());
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
     * @param world world name
     * @return true if economy enabled
     */
    public boolean isEconomyEnabled(String world) {
        PlotMapInfo pmi = getMap(world);
        return isEconomyEnabled(pmi);
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
        return pmi != null && pmi.canUseEconomy();
    }

    /**
     * Checks if the plotworld has economy features enabled
     *
     * @param world world
     * @return true if economy enabled
     */

    public boolean isEconomyEnabled(IWorld world) {
        return isEconomyEnabled(world.getName().toLowerCase());
    }

    /**
     * Get the PlotMap based on the world given
     * @param world plotworld
     * @return PlotMapInfo for the plotworld, if the world doesn't exist then it will return null
     */
    public PlotMapInfo getMap(IWorld world) {
        String worldName = world.getName().toLowerCase();
        return getPlotMaps().get(worldName);
    }

    /**
     * Get the PlotMap based on the world given
     * @param world plotworld
     * @return PlotMapInfo for the plotworld, if the world doesn't exist then it will return null
     */
    public PlotMapInfo getMap(String world) {
        return getPlotMaps().get(world.toLowerCase());
    }

    /**
     * Get the PlotMap based on the world given
     * @param location the location in a plotworld
     * @return PlotMapInfo for the plotworld, if the world doesn't exist then it will return null
     */
    public PlotMapInfo getMap(ILocation location) {
        String worldName = location.getWorld().getName().toLowerCase();
        return getMap(worldName);
    }

    /**
     * Get the PlotMap based on the world given
     * @param player a player in a plotworld
     * @return PlotMapInfo for the plotworld, if the world doesn't exist then it will return null
     */
    public PlotMapInfo getMap(IEntity player) {
        String world = player.getWorld().getName();
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
     * @return plot
     */
    public Plot getPlotById(PlotId id) {
        PlotMapInfo pmi = getMap(id.getWorld());

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
        return pmi.getPlot(id);
    }

    /**
     * Plot to remove from plotmap.
     *  @param id ID
     */
    public void removePlot(PlotId id) {
        PlotMapInfo pmi = getMap(id.getWorld());

        if (pmi != null) {
            pmi.removePlot(id);
        }
    }

    /**
     * Plot to add to loaded plotmap.
     *
     * @param id ID
     * @param plot Plot to be added
     */
    public void addPlot(PlotId id, Plot plot) {
        PlotMapInfo pmi = getMap(id.getWorld());
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
            InternalPlotLoadEvent event = new InternalPlotLoadEvent(id.getWorld(), plot);
            plugin.getServerBridge().getEventBus().post(event);

        }
    }

    /**
     * Get the first plotworld defined in config
     *
     * @return plotworld
     */
    public IWorld getFirstWorld() {
        String firstWorld = (String) getPlotMaps().keySet().toArray()[0];
        return plugin.getServerBridge().getWorld(firstWorld);
    }

    /**
     * Checks if world is a PlotWorld
     *
     * @param world object to get the location from
     * @return true if world is plotworld, false otherwise
     */
    public boolean isPlotWorld(IWorld world) {
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
     * @param owner owner name
     * @param uuid  owner uuid
     * @param pmi   plotmap to add the plot to
     * @return the new plot created
     *
     * @throws NullPointerException If the <code>id</code> argument is <code>null</code>
     */
    public Plot createPlot(PlotId id, String owner, UUID uuid, PlotMapInfo pmi) {
        Plot plot = new Plot(plugin, owner, uuid, id.getWorld(), id, pmi.getDaysToExpiration());

        setOwnerSign(plot);
        addPlot(id, plot, pmi);
        adjustWall(id, true);

        plugin.getSqlManager().addPlot(plot, id, getPlotTopLoc(id), getPlotBottomLoc(id));
        return plot;
    }

    /**
     * Move a plot from one location to another
     *
     * @param idFrom the id of the plot to be moved
     * @param idTo   the id the plot will be moved to
     * @return true if successful, false otherwise
     */
    public boolean movePlot(PlotId idFrom, PlotId idTo) {

        if (!getGenManager(idFrom).movePlot(idFrom, idTo)) {
            return false;
        }

        Plot plotFrom = getPlotById(idFrom);
        Plot plotTo = getPlotById(idTo);

        if (plotFrom != null) {
            if (plotTo != null) {
                plugin.getSqlManager().deletePlot(plotTo.getInternalID());
                removePlot(idFrom);
                removePlot(idTo);
                plugin.getSqlManager().deletePlot(plotFrom.getInternalID());

                plotTo.setId(idFrom);
                plugin.getSqlManager().addPlot(plotTo, idFrom, getPlotTopLoc(idFrom), getPlotBottomLoc(idFrom));
                addPlot(idFrom, plotTo);

                plotFrom.setId(idTo);
                plugin.getSqlManager().addPlot(plotFrom, idTo, getPlotTopLoc(idTo), getPlotBottomLoc(idTo));
                addPlot(idTo, plotFrom);

                setOwnerSign(plotFrom);
                removeSellSign(plotFrom.getId());
                setOwnerSign(plotTo);
                removeSellSign(plotTo.getId());

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
        PlotId idFrom = filledPlot.getId();
        plugin.getSqlManager().deletePlot(filledPlot.getInternalID());
        removePlot(idFrom);

        filledPlot.setId(idDestination);
        plugin.getSqlManager()
                .addPlot(filledPlot, idDestination, getPlotTopLoc(idDestination), getPlotBottomLoc(idDestination));
        addPlot(idDestination, filledPlot);

        setOwnerSign(filledPlot);
        setSellSign(filledPlot);
        removeOwnerSign(idFrom);
        removeSellSign(idFrom);
    }

    /**
     * Remove any LWC Data that may be on the plot.
     *  @param id Plot ID
     */
    public void removeLWC(PlotId id) {
        ILocation bottom = getGenManager(id).getBottom(id);
        ILocation top = getGenManager(id).getTop(id);
        final int x1 = bottom.getBlockX();
        final int y1 = bottom.getBlockY();
        final int z1 = bottom.getBlockZ();
        final int x2 = top.getBlockX();
        final int y2 = top.getBlockY();
        final int z2 = top.getBlockZ();
        final String worldName = id.getWorld().getName();

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

    /**
     * Clears a plot
     *
     * @param plot   the plot to be cleared
     * @param sender the sender of the command
     * @param reason The reason they will be cleared. The cause can be: EXPIRED, RESET, CLEAR
     */
    public void clear(Plot plot, ICommandSender sender, ClearReason reason) {
        PlotId id = plot.getId();

        if (reason.equals(ClearReason.Clear)) {
            adjustWall(plot.getId(), true);
        } else {
            adjustWall(plot.getId(), false);
        }
        plugin.addPlotToClear(new PlotToClear(id, reason, sender));
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
     * @return true if the plot is unclaimed, false otherwise
     */
    public boolean isPlotAvailable(PlotId id) {
        PlotMapInfo pmi = getMap(id.getWorld().getName());

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
            Plot plot = getPlotById(id);

            getGenManager(id).adjustPlotFor(id, true, plot.isProtected(), plot.isForSale());
        }
    }

    /**
     * Updates the blocks on the plot border
     *
     * @param id      plot id
     * @param claimed is the plot claimed
     */
    public void adjustWall(PlotId id, boolean claimed) {
        Plot plot = getPlotById(id);

        getGenManager(id).adjustPlotFor(id, claimed, plot.isProtected(), plot.isForSale());
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
    public HashMap<String, PlotMapInfo> getPlotMaps() {
        return plotmaps;
    }

    /**
     * Register the plotworld the plotmap
     *
     * @param world name of a plotworld
     * @param map   {@link PlotMapInfo} information
     */
    public void addPlotMap(String world, PlotMapInfo map) {
        getPlotMaps().put(world.toLowerCase(), map);
    }

    /**
     * Remove a plotmap.
     * Likely to be used if a world is deleted.
     *
     * @param world name of a plotworld
     */
    public void removePlotMap(String world) {
        getPlotMaps().remove(world.toLowerCase());
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
    public ILocation getPlotMiddle(IWorld world, PlotId id) {
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
    public String getPlotProperty(PlotId id, String pluginname, String property) {
        Plot plot = getPlotById(id);
        return getPlotProperty(plot, pluginname, property);
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
    public boolean setPlotProperty(PlotId id, String pluginname, String property, String value) {
        Plot plot = getPlotById(id);
        return plot.setPlotProperty(pluginname, property, value);
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
}