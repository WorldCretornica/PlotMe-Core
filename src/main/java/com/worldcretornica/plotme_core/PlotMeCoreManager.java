package com.worldcretornica.plotme_core;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBiome;
import com.worldcretornica.plotme_core.utils.Util;

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

    public static PlotMeCoreManager getInstance() {
        return INSTANCE;
    }
    
    void setPlugin(PlotMe_Core instance) {
        plugin = instance;
    }
    
    /**
     * Gets the X coordinate on the plot grid
     * Not to be confused with the X coordinate of a Block or Entity
     *
     * @param id PlotID
     * @return X Coordinate
     */
    public int getIdX(String id) {
        return Integer.parseInt(id.substring(0, id.indexOf(';')));
    }

    /**
     * Gets the Z coordinate on the plot grid
     * Not to be confused with the Z coordinate of a Block or Entity
     *
     * @param id PlotID
     * @return Z Coordinate
     */
    public int getIdZ(String id) {
        return Integer.parseInt(id.substring(id.indexOf(';') + 1));
    }

    /**
     * Gets the plot by the id and pmi
     *
     * @param id Plot ID
     * @param pmi PlotMapInfo
     * @return plot
     */
    public Plot getPlotById(String id, PlotMapInfo pmi) {
        if (pmi == null) {
            return null;
        }

        return pmi.getPlot(id);
    }

    /**
     * Gets the plot with the player and pmi. The player has its location checked to retrieve the plotID.
     *
     * @param player player standing in the plot
     * @param pmi PlotMapInfo
     * @return plot
     */
    public Plot getPlotById(IPlayer player, PlotMapInfo pmi) {
        String id = getPlotId(player);
        return getPlotById(id, pmi);
    }

    /**
     * Removes the plot from the plotmap
     *
     * @param pmi plotmap
     * @param id plot id
     */
    public void removePlot(PlotMapInfo pmi, String id) {
        if (pmi != null) {
            pmi.removePlot(id);
        }
    }

    /**
     * Sets the sign for the plot owner
     *
     * @param world plotworld
     * @param plot  plot to set sign on
     */
    public void setOwnerSign(IWorld world, Plot plot) {
        String id = plot.getId();
        String line1 = "ID: " + id;
        String line2 = "";
        String line3 = plot.getOwner();
        String line4 = "";
        getGenManager(world).setOwnerDisplay(world, plot.getId(), line1, line2, line3, line4);
    }

    public boolean isPlotAvailable(String id, PlotMapInfo pmi) {
        return pmi != null && pmi.getPlot(id) == null;
    }

    /**
     * Get the id of the plot based on the location
     *
     * @param location location in the plotworld
     * @return Plot ID or an empty string if not found
     */
    public String getPlotId(ILocation location) {
        if (getGenManager(location.getWorld()) == null) {
            return "";
        } else {
            return getGenManager(location.getWorld()).getPlotId(location);
        }

    }

    /**
     * Get the id of the plot the player is standing on
     *
     * @param player player in the plotworld
     * @return Plot ID or an empty string if not found
     */
    public String getPlotId(IPlayer player) {
        if (getGenManager(player.getWorld()) == null) {
            return "";
        } else {
            return getGenManager(player.getWorld()).getPlotId(player);
        }

    }

    public ILocation getPlotBottomLoc(IWorld world, String id) {
        return getGenManager(world).getPlotBottomLoc(world, id);
    }

    public ILocation getPlotTopLoc(IWorld world, String id) {
        return getGenManager(world).getPlotTopLoc(world, id);
    }

    /**
     * Removes the owner sign from the plot.
     *
     * @param world plotworld
     * @param id    plot id to remove the sign from
     */
    public void removeOwnerSign(IWorld world, String id) {
        getGenManager(world).removeOwnerDisplay(world, id);
    }

    /**
     * Remove the sell sign from the plot
     *
     * @param world plotworld
     * @param id    plot id to remove the sign from
     */
    public void removeSellSign(IWorld world, String id) {
        getGenManager(world).removeSellerDisplay(world, id);
    }

    /**
     * Remove the auction sign from the plot
     *
     * @param world plotworld
     * @param id    plot id to remove the sign from
     */
    @Deprecated
    public void removeAuctionSign(IWorld world, String id) {
        getGenManager(world).removeAuctionDisplay(world, id);
    }

    /**
     * Set the auction sign on the plot
     *
     * @param world plotworld
     * @param plot  plot to add sign to
     */
    @Deprecated
    public void setAuctionSign(IWorld world, Plot plot) {
        String line1 = Util().C("SignOnAuction");
        String line2;
        if (plot.getCurrentBidder() == null) {
            line2 = Util().C("SignMinimumBid");
        } else {
            line2 = Util().C("SignCurrentBid");
        }
        String line3 = String.valueOf(plot.getCurrentBid());
        String line4 = "/plotme " + Util().C("CommandBid") + " <x>";
        getGenManager(world).setAuctionDisplay(world, plot.getId(), line1, line2, line3, line4);
    }

    /**
     * Set the sell sign on the plot
     *
     * @param world plotworld
     * @param plot  plot to add sign to
     */
    public void setSellSign(IWorld world, Plot plot) {
        String line1 = Util().C("SignForSale");
        String line2 = Util().C("SignPrice");
        String line3 = String.valueOf(plot.getCustomPrice());
        String line4 = "/plotme buy";

        getGenManager(world).setSellerDisplay(world, plot.getId(), line1, line2, line3, line4);
    }

    /**
     * Check if the plot id is valid
     *
     * @param world plotworld
     * @param id    id value to be checked
     * @return true if the id is valid, false otherwise
     */
    public boolean isValidId(IWorld world, String id) {
        return getGenManager(world).isValidId(id);
    }

    /**
     * Get the x coordinate at the bottom of the plot
     *
     * @param id    plot id
     * @param world plotworld
     * @return bottom x coordinate of the plot
     */
    public int bottomX(String id, IWorld world) {
        return getGenManager(world).bottomX(id, world);
    }

    /**
     * Get the x coordinate at the top of the plot
     *
     * @param id    plot id
     * @param world plotworld
     * @return top x coordinate of the plot
     */
    public int topX(String id, IWorld world) {
        return getGenManager(world).topX(id, world);
    }

    /**
     * Get the z coordinate at the bottom of the plot
     *
     * @param id    plot id
     * @param world plotworld
     * @return bottom z coordinate of the plot
     */
    public int bottomZ(String id, IWorld world) {
        return getGenManager(world).bottomZ(id, world);
    }

    /**
     * Get the z coordinate at the top of the plot
     *
     * @param id    plot id
     * @param world plotworld
     * @return top z coordinate of the plot
     */
    public int topZ(String id, IWorld world) {
        return getGenManager(world).topZ(id, world);
    }

    /**
     * Get the plot home location of a plot
     *
     * @param world plotworld
     * @param id    plot id to get home of
     * @return an ILocation of the plot home location
     */
    public ILocation getPlotHome(IWorld world, String id) {
        return getGenManager(world).getPlotHome(world, id);
    }

    /**
     * Get the players in the Plot
     *
     * @param world plotworld
     * @param id    plot id
     * @return a list of players in the plot
     */
    public List<IPlayer> getPlayersInPlot(IWorld world, String id) {
        return getGenManager(world).getPlayersInPlot(id);
    }

    public IPlotMe_GeneratorManager getGenManager(IWorld world) {
        return plugin.getGenManager(world.getName());
    }

    /**
     * Get the number of plots the player owns
     *
     * @param uuid player UUID
     * @param world plotworld
     * @return number of plots the player owns
     */
    public int getNbOwnedPlot(UUID uuid, String world) {
        return plugin.getSqlManager().getPlotCount(world, uuid, null);
    }

    /**
     * Checks if the plotworld has economy features enabled
     *
     * @param world world name
     * @return true if economy enabled
     */
    private boolean isEconomyEnabled(String world) {
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
        if (!plugin.getServerBridge().getConfig().getBoolean("globalUseEconomy") || plugin.getServerBridge().getEconomy() == null) {
            return false;
        }
        if (pmi == null) {
            return false;
        } else {
            return pmi.isUseEconomy();
        }
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


    public PlotMapInfo getMap(IWorld world) {
        String worldName = world.getName();
        return getMap(worldName);
    }

    public PlotMapInfo getMap(String world) {
        return getPlotMaps().get(world.toLowerCase());
    }

    public PlotMapInfo getMap(ILocation location) {
        String worldName = location.getWorld().getName().toLowerCase();
        return getMap(worldName);
    }

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
     * @param world the world the plot is in
     * @return plot
     */
    public Plot getPlotById(String id, IWorld world) {
        return getPlotById(id, world.getName());
    }

    /**
     * Gets the plot with the given id in the given world as a string.
     *
     * @param id plot id
     * @param name the world the plot is in
     * @return plot
     */
    public Plot getPlotById(String id, String name) {
        PlotMapInfo pmi = getMap(name);

        if (pmi == null) {
            return null;
        }

        return pmi.getPlot(id);
    }

    /**
     * Gets the plot with the given id and location based on the given player.
     *
     * @param id plot id
     * @param player the player in the plot
     * @return plot
     */
    public Plot getPlotById(String id, IPlayer player) {
        return getPlotById(id, player.getWorld());
    }

    /**
     * Gets the plot with the given player which will have his location checked.
     *
     * @param player player standing in a plot
     * @return plot
     */
    public Plot getPlotById(IPlayer player) {
        PlotMapInfo pmi = getMap(player);
        String id = getPlotId(player);

        if (pmi == null || id.isEmpty()) {
            return null;
        }

        return pmi.getPlot(id);
    }

    /**
     * Plot to remove from plotmap.
     *
     * @param world world
     * @param id ID
     */
    public void removePlot(IWorld world, String id) {
        PlotMapInfo pmi = getMap(world);

        if (pmi != null) {
            pmi.removePlot(id);
        }
    }

    /**
     * Plot to add to loaded plotmap.
     *
     * @param world world
     * @param id ID
     * @param plot Plot to be added
     */
    public void addPlot(IWorld world, String id, Plot plot) {
        PlotMapInfo pmi = getMap(world);

        if (pmi != null) {
            pmi.addPlot(id, plot);
            plugin.getServerBridge().getEventFactory().callPlotLoadedEvent(plugin, world, plot);
        }
    }

    /**
     * Plot to add to loaded plotmap.
     *
     * @param world world
     * @param id ID
     * @param plot Plot to be added
     * @param pmi Plot Map to add the plot to
     */
    public void addPlot(IWorld world, String id, Plot plot, PlotMapInfo pmi) {
        if (pmi != null) {
            pmi.addPlot(id, plot);
            plugin.getServerBridge().getEventFactory().callPlotLoadedEvent(plugin, world, plot);
        }
    }

    /**
     * Get the first plotworld defined in config
     *
     * @return plotworld
     */

    public IWorld getFirstWorld() {
        String firstWorld;
        try {
            firstWorld = (String) getPlotMaps().keySet().toArray()[0];
        } catch (ArrayIndexOutOfBoundsException error) {
            plugin.getLogger().warning("Uh oh. You don't have any plotworlds so plotme isn't working properly.");
            return null;
        }
        return plugin.getServerBridge().getWorld(firstWorld);
    }

    /**
     * Checks if world is a PlotWorld
     *
     * @param world object to get the location from
     * @return true if world is plotworld, false otherwise
     */
    public boolean isPlotWorld(IWorld world) {
        if (getGenManager(world) == null) {
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
     * Checks if the block is in a plotworld
     *
     * @param block block to get the location from
     * @return true if world is plotworld, false otherwise
     */

    public boolean isPlotWorld(IBlock block) {
        return isPlotWorld(block.getWorld());
    }

    /**
     * Creates a new plot
     *
     * @param world plotworld
     * @param id    plot id
     * @param owner owner name
     * @param uuid  owner uuid
     * @param pmi   plotmap to add the plot to
     * @return the new plot created
     */
    public Plot createPlot(IWorld world, String id, String owner, UUID uuid, PlotMapInfo pmi) {
        if (isPlotAvailable(id, pmi) && !id.isEmpty()) {
            Plot plot = new Plot(plugin, owner, uuid, world, id, pmi.getDaysToExpiration());

            setOwnerSign(world, plot);
            addPlot(world, id, plot, pmi);
            adjustWall(world, id, true);

            plugin.getSqlManager().addPlot(plot, getIdX(id), getIdZ(id), world);
            return plot;
        } else {
            return null;
        }
    }

    /**
     * Move a plot from one location to another
     *
     * @param world  plotworld
     * @param idFrom the id of the plot to be moved
     * @param idTo   the id the plot will be moved to
     * @return true if successful, false otherwise
     */
    public boolean movePlot(IWorld world, String idFrom, String idTo) {
        
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
                plugin.getSqlManager().addPlot(plot2, idX, idZ, topX(idFrom, world), 
                        bottomX(idFrom, world), topZ(idFrom, world), bottomZ(idFrom, world));
                addPlot(world, idFrom, plot2);

                idX = getIdX(idTo);
                idZ = getIdZ(idTo);
                plot1.setId(idTo);
                plugin.getSqlManager().addPlot(plot1, idX, idZ, topX(idTo, world), 
                        bottomX(idTo, world), topZ(idTo, world), bottomZ(idTo, world));
                addPlot(world, idTo, plot1);
                
                setOwnerSign(world, plot1);
                removeSellSign(world, plot1.getId());
                removeAuctionSign(world, plot1.getId());
                setOwnerSign(world, plot2);
                removeSellSign(world, plot2.getId());
                removeAuctionSign(world, plot2.getId());

            } else {
                movePlotToEmpty(world, plot1, idTo);
            }
        } else if (plot2 != null) {
            movePlotToEmpty(world, plot2, idFrom);
        }

        return true;
    }

    /**
     * Move a plot to an spot where there is no plot existing
     *
     * @param world         plotworld
     * @param filledPlot    the Plot to be moved
     * @param idDestination the id the plot will be moved to
     */
    private void movePlotToEmpty(IWorld world, Plot filledPlot, String idDestination) {
        String idFrom = filledPlot.getId();
        int idX = getIdX(idFrom);
        int idZ = getIdZ(idFrom);
        plugin.getSqlManager().deletePlot(idX, idZ, world.getName());
        removePlot(world, idFrom);

        idX = getIdX(idDestination);
        idZ = getIdZ(idDestination);
        filledPlot.setId(idDestination);
        plugin.getSqlManager().addPlot(filledPlot, idX, idZ, topX(idDestination, world), bottomX(idDestination, world), topZ(idDestination, world), bottomZ(idDestination, world));
        addPlot(world, idDestination, filledPlot);

        setOwnerSign(world, filledPlot);
        setSellSign(world, filledPlot);
        removeOwnerSign(world, idFrom);
        removeSellSign(world, idFrom);
        removeAuctionSign(world, idFrom);
    }

    /**
     * Remove any LWC Data that may be on the plot.
     *
     * @param world Plotworld
     * @param id Plot ID
     */
    public void removeLWC(IWorld world, String id) {
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

    /**
     * Clears a plot
     *
     * @param world  plotworld
     * @param plot   the plot to be cleared
     * @param sender the sender of the command
     * @param reason The reason they will be cleared. The cause can be: EXPIRED, RESET, CLEAR
     */
    public void clear(IWorld world, Plot plot, ICommandSender sender, ClearReason reason) {
        String id = plot.getId();

        String worldName = world.getName().toLowerCase();

        ILocation bottom = getGenManager(world).getBottom(world, id);
        ILocation top = getGenManager(world).getTop(world, id);
        if(reason.equals(ClearReason.Clear)) {
            adjustWall(world, plot.getId(), true);
        } else {
            adjustWall(world, plot.getId(), false);
        }
        if (getMap(worldName).isUseProgressiveClear()) {
            PlotToClear ptc = plugin.getPlotLocked(worldName, plot.getId());
            if (ptc != null) {
                switch (ptc.getReason()) {
                    case Clear:
                        sender.sendMessage(Util().C("MsgPlotLockedClear"));
                        break;
                    case Reset:
                        sender.sendMessage(Util().C("MsgPlotLockedReset"));
                        break;
                    case Expired:
                        sender.sendMessage(Util().C("MsgPlotLockedExpired"));
                        break;
                }
            } else {
                plugin.addPlotToClear(new PlotToClear(worldName, id, reason, sender));
            }
        } else {
            getGenManager(world).clear(bottom, top);
            if (plugin.getServerBridge().getUsingLwc()) {
                removeLWC(world, id);
            }
            sender.sendMessage(Util().C("MsgPlotCleared"));
        }
    }

    /**
     * Checks if the plot is claimed or not
     *
     * @param id    the plot id to be checked
     * @param world plotworld
     * @return true if the plot is unclaimed, false otherwise
     */
    public boolean isPlotAvailable(String id, IWorld world) {
        return isPlotAvailable(id, world.getName());
    }

    /**
     * Checks if the plot is claimed or not
     *
     * @param id     the plot id to be checked
     * @param player the player which will have it's world checked
     * @return true if the plot is unclaimed, false otherwise
     */
    public boolean isPlotAvailable(String id, IPlayer player) {
        return isPlotAvailable(id, player.getWorld());
    }

    /**
     * Checks if the plot is claimed or not
     *
     * @param id    the plot id to be checked
     * @param world plotworld to be checked as a string
     * @return true if the plot is unclaimed, false otherwise
     */
    private boolean isPlotAvailable(String id, String world) {
        PlotMapInfo pmi = getMap(world);

        return pmi != null && pmi.getPlot(id) == null;
    }

    @Deprecated
    public IPlotMe_GeneratorManager getGenMan(String name) {
        return plugin.getGenManager(name);
    }

    /**
     * Updates the blocks on the plot border
     *
     * @param player the player in the plot
     */
    public void adjustWall(IPlayer player) {
        IWorld world = player.getWorld();
        String id = getPlotId(player);
        Plot plot = getPlotById(id, world);

        getGenManager(world).adjustPlotFor(world, id, true, plot.isProtect(), false, plot.isForSale());
    }

    /**
     * Updates the blocks on the plot border
     *
     * @param world   plotworld
     * @param id      plot id
     * @param claimed is the plot claimed
     */
    public void adjustWall(IWorld world, String id, boolean claimed) {
        Plot plot = getPlotById(id, world);

        getGenManager(world).adjustPlotFor(world, id, claimed, plot.isProtect(), false, plot.isForSale());
    }

    public void setBiome(IWorld world, String id, IBiome biome) {
        getGenManager(world).setBiome(world, id, biome);
        plugin.getSqlManager().updatePlot(getIdX(id), getIdZ(id), world.getName(), "biome", ((BukkitBiome) biome).getBiome().name());
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
     * @deprecated Use {@link #isPlayerIgnoringWELimit(IPlayer)} instead
     */
    @Deprecated
    public boolean isPlayerIgnoringWELimit(UUID uuid) {
        if (plugin.getServerBridge().getConfig().getBoolean("defaultWEAnywhere")) {
            return getPlayersIgnoringWELimit().contains(uuid);
        }
        return getPlayersIgnoringWELimit().contains(uuid);
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


    private Util Util() {
        return plugin.getUtil();
    }

    public boolean isPlayerIgnoringWELimit(IPlayer player) {
        if (plugin.getServerBridge().getConfig().getBoolean("defaultWEAnywhere") && player.hasPermission(PermissionNames.ADMIN_WEANYWHERE)) {
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
    public ILocation getPlotMiddle(IWorld world, String id) {
        /*ILocation bottom = getPlotBottomLoc(world, id);
        ILocation top = getPlotTopLoc(world, id);
        
        ILocation middle = bottom.clone().add(top.getX() - bottom.getX(), 0, top.getZ() - bottom.getZ());
        middle.setY(getGenManager(world).getRoadHeight(world.getName()) + 1);*/
        
        return getGenManager(world).getPlotMiddle(world, id);
    }
    
    public void UpdatePlayerNameFromId(final UUID uuid, final String name) {
        plugin.getSqlManager().updatePlotsNewUUID(uuid, name);
        
        plugin.getServerBridge().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                for (PlotMapInfo pmi : plotmaps.values()) {
                    for(Plot plot : pmi.getLoadedPlots().values()) {

                        //Owner
                        if(plot.getOwnerId() != null && plot.getOwnerId().equals(uuid)) {
                            plot.setOwner(name);
                        }
                        
                        //Allowed
                        plot.allowed().replace(name, name, uuid);
                        
                        //Denied
                        plot.denied().replace(name, name, uuid);
                    }
                }
            }
        });
    }
    
    /**
     * Gets the value of that plot property
     *
     * @param id PlotID
     * @param world World the plot is in
     * @param pluginname Name of the plugin owning this property
     * @param property Name of the property to get the value of
     * @return Value of the property
     */
    public String getPlotProperty(String id, String world, String pluginname, String property) {
        Plot plot = getPlotById(id, world);
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
     * @param world World the plot is in
     * @param pluginname Name of the plugin owning this property
     * @param property Name of the property
     * @param value Value of the property
     * @return If the property was set successfully
     */
    public boolean setPlotProperty(String id, String world, String pluginname, String property, String value) {
        Plot plot = getPlotById(id, world);
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
