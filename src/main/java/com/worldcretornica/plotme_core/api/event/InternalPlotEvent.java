package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InternalPlotEvent {

    protected PlotMe_Core plugin;
    protected Plot plot;
    protected IWorld world;

    public InternalPlotEvent(PlotMe_Core instance, Plot p, IWorld world) {
        this.plugin = instance;
        this.plot = p;
        this.world = world;
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
    public IWorld getWorld() {
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
    public ILocation getUpperBound() {
        if (plot != null && world != null) {
            return plugin.getPlotMeCoreManager().getPlotTopLoc(world, plot.getId());
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
    public ILocation getLowerBound() {
        if (plot != null && world != null) {
            return plugin.getPlotMeCoreManager().getPlotBottomLoc(world, plot.getId());
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
        if (plot != null) {
            return plot.allowed().getAllPlayers().keySet();
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
        if (plot != null) {
            return plot.allowed().getAllPlayers().values();
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
        if (plot != null) {
            return plot.denied().getAllPlayers().keySet();
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
        if (plot != null) {
            return plot.denied().getAllPlayers().values();
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
    public ILocation getHomeLocation() {
        if (plot != null && world != null) {
            return plugin.getPlotMeCoreManager().getPlotHome(world, plot.getId());
        } else {
            return null;
        }
    }
}
