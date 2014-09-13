package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashSet;

public abstract class PlotEvent extends Event {

    protected static final HandlerList handlers = new HandlerList();
    protected PlotMe_Core plugin;
    protected Plot plot;
    protected World world;

    public PlotEvent(PlotMe_Core instance, Plot p, World w) {
        plugin = instance;
        plot = p;
        world = w;
    }

    /**
     * Returns the plot used in the event
     *
     * @return plot used in the event
     *
     */
    public Plot getPlot() {
        return plot;
    }

    /**
     * Returns the world used in the event
     *
     * @return world
     *
     */
    public World getWorld() {
        return world;
    }

    /**
     * Returns the owner of the plot used in the event
     *
     * @return owner of the plot
     *
     */
    public String getOwner() {
        if (plot != null) {
            return plot.getOwner();
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
        if (plot != null && world != null) {
            return plugin.getPlotMeCoreManager().getGenMan(world).getPlotTopLoc(world, plot.getId());
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
        if (plot != null && world != null) {
            return plugin.getPlotMeCoreManager().getGenMan(world).getPlotBottomLoc(world, plot.getId());
        } else {
            return null;
        }
    }

    /**
     * Returns the list of people allowed to build on this plot. The function
     * returns an empty HashSet if the plot is null.
     *
     * @return list of people allowed
     *
     */
    public HashSet<String> getAllAllowed() {
        if (plot != null) {
            return plot.allowed();
        } else {
            return new HashSet<>();
        }
    }

    /**
     * Returns the list of people denied from building on this plot. The
     * function returns an empty HashSet if the plot is null.
     *
     * @return list of people denied
     *
     */
    public HashSet<String> getAllDenied() {
        if (plot != null) {
            return plot.denied();
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
        if (plot != null && world != null) {
            return plugin.getPlotMeCoreManager().getPlotHome(world, plot.getId());
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
