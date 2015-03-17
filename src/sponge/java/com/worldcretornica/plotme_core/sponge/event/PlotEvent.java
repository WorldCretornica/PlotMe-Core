package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotEvent;
import com.worldcretornica.plotme_core.sponge.api.SpongeLocation;
import com.worldcretornica.plotme_core.sponge.api.SpongeWorld;
import org.spongepowered.api.event.AbstractEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class PlotEvent extends AbstractEvent {

    private final InternalPlotEvent event;

    public PlotEvent(Plot plot, World world) {
        event = new InternalPlotEvent(plot, new SpongeWorld(world));
    }

    public PlotEvent(Plot plot, IWorld world) {
        event = new InternalPlotEvent(plot, world);
    }

    public PlotEvent(InternalPlotEvent event) {
        this.event = event;
    }

    /**
     * Returns the plot used in the event
     *
     * @return plot used in the event
     */
    public Plot getPlot() {
        return event.getPlot();
    }

    /**
     * Returns the world the event is occuring in
     *
     * @return world
     */
    public World getWorld() {
        return ((SpongeWorld) event.getWorld()).getWorld();
    }

    /**
     * Returns the owner of the plot used in the event
     *
     * @return owner of the plot
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
     */
    public Location getUpperBound() {
        if (event.getPlot() != null) {
            return ((SpongeLocation) PlotMeCoreManager.getInstance().getPlotTopLoc(event.getWorld(), event.getPlot().getId())).getLocation();
        } else {
            return null;
        }
    }

    /**
     * Returns the location of the lower corner of the plot used in the event
     *
     * @return location of the lower corner
     */
    public Location getLowerBound() {
        if (event.getPlot() != null) {
            return ((SpongeLocation) PlotMeCoreManager.getInstance().getPlotBottomLoc(event.getWorld(), event.getPlot().getId())).getLocation();
        } else {
            return null;
        }
    }

    /**
     * Returns the list of people allowed to build on this plot. The function
     * returns an empty Set if the plot is null.
     *
     * @return list of people allowed
     */
    public List<String> getAllAllowed() {
        if (event.getPlot() != null) {
            return event.getPlot().allowed().getAllPlayers();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Returns the list of people denied from building on this plot. The
     * function returns an empty Set if the plot is null.
     *
     * @return list of people denied
     */
    public List<String> getAllDenied() {
        if (event.getPlot() != null) {
            return event.getPlot().denied().getAllPlayers();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Returns the location of the home of this plot. The function returns null
     * if the plot or world is null.
     *
     * @return home location
     */
    public Location getHomeLocation() {
        if (event.getPlot() != null) {
            return ((SpongeLocation) PlotMeCoreManager.getInstance().getPlotHome(event.getWorld(), event.getPlot().getId())).getLocation();
        } else {
            return null;
        }
    }

}
