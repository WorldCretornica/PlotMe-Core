package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotEvent;
import com.worldcretornica.plotme_core.bukkit.api.*;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class PlotEvent extends Event {

    protected static final HandlerList handlers = new HandlerList();
    private InternalPlotEvent event;
    protected PlotMe_Core plugin;

    public PlotEvent(PlotMe_Core instance, Plot p, World world) {
        this.event = new InternalPlotEvent(instance, p, new BukkitWorld(world));
        this.plugin = instance;
    }
    
    public PlotEvent(PlotMe_Core instance, Plot p, IWorld world) {
        this.event = new InternalPlotEvent(instance, p, world);
        this.plugin = instance;
    }
    
    public PlotEvent(PlotMe_Core instance, InternalPlotEvent event) {
        this.event = event;
        this.plugin = instance;
    }

    /**
     * Returns the plot used in the event
     *
     * @return plot used in the event
     *
     */
    public Plot getPlot() {
        return event.getPlot();
    }

    /**
     * Returns the world used in the event
     *
     * @return world
     *
     */
    public World getWorld() {
        return ((BukkitWorld) event.getWorld()).getWorld();
    }

    /**
     * Returns the owner of the plot used in the event
     *
     * @return owner of the plot
     *
     */
    public String getOwner() {
        if (event.getPlot() != null) {
            return event.getPlot().getOwner();
        } else {
            return "";
        }
    }

    /**
     * Returns the location of the upper corner of the plot used in the event
     *
     * @return location of the upper corner
     *
     */
    public Location getUpperBound() {
        if (event.getPlot() != null && event.getWorld() != null) {
            return ((BukkitLocation) plugin.getPlotMeCoreManager().getGenMan(event.getWorld()).getPlotTopLoc(event.getWorld(), event.getPlot().getId())).getLocation();
        } else {
            return null;
        }
    }

    /**
     * Returns the location of the lower corner of the plot used in the event
     *
     * @return location of the lower corner
     *
     */
    public Location getLowerBound() {
        if (event.getPlot() != null && event.getWorld() != null) {
            return ((BukkitLocation) plugin.getPlotMeCoreManager().getGenMan(event.getWorld()).getPlotBottomLoc(event.getWorld(), event.getPlot().getId())).getLocation();
        } else {
            return null;
        }
    }

    /**
     * Returns the list of people allowed to build on this plot. The function
     * returns an empty Set if the plot is null.
     *
     * @return list of people allowed
     *
     */
    public Set<String> getAllAllowed() {
        if (event.getPlot() != null) {
            return event.getPlot().allowed().getAllPlayers().keySet();
        } else {
            return new HashSet<>();
        }
    }

    /**
     * Returns the collection of people allowed to build on this plot. The function
     * returns an empty Collection if the plot is null.
     *
     * @return list of people allowed
     *
     */
    public Collection<UUID> getAllAllowedUUID() {
        if (event.getPlot() != null) {
            return event.getPlot().allowed().getAllPlayers().values();
        } else {
            return new HashSet<>();
        }
    }

    /**
     * Returns the list of people denied from building on this plot. The
     * function returns an empty Set if the plot is null.
     *
     * @return list of people denied
     *
     */
    public Set<String> getAllDenied() {
        if (event.getPlot() != null) {
            return event.getPlot().denied().getAllPlayers().keySet();
        } else {
            return new HashSet<>();
        }
    }

    /**
     * Returns the list of people denied from building on this plot. The
     * function returns an empty Set if the plot is null.
     *
     * @return list of people denied
     *
     */
    public Collection<UUID> getAllDeniedUUID() {
        if (event.getPlot() != null) {
            return event.getPlot().denied().getAllPlayers().values();
        } else {
            return new HashSet<>();
        }
    }

    /**
     * Returns the location of the home of this plot. The function returns null
     * if the plot or world is null.
     *
     * @return home location
     *
     */
    public Location getHomeLocation() {
        if (event.getPlot() != null && event.getWorld() != null) {
            return ((BukkitLocation) plugin.getPlotMeCoreManager().getPlotHome(event.getWorld(), event.getPlot().getId())).getLocation();
        } else {
            return null;
        }
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
