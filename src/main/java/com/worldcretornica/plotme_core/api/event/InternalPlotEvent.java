package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotEvent implements Event {

    protected final IWorld world;
    private final Plot plot;

    public InternalPlotEvent(Plot plot, IWorld world) {
        this.plot = plot;
        this.world = world;
    }

    /**
     * Returns the plot used in the event
     *
     * @return plot used in the event
     */
    public Plot getPlot() {
        return plot;
    }

    /**
     * Returns the world used in the event
     *
     * @return world
     */
    public IWorld getWorld() {
        return world;
    }

    /**
     * Returns the owner of the plot used in the event
     *
     * @return owner of the plot
     */
    public String getOwner() {
        if (getPlot() != null) {
            return getPlot().getOwner();
        } else {
            return "";
        }
    }

    /**
     * Returns the location of the home of this plot. The function returns null
     * if the plot or world is null.
     *
     * @return home location
     */
    public ILocation getHomeLocation() {
        if (getPlot() != null) {
            return PlotMeCoreManager.getInstance().getPlotHome(getPlot().getId());
        } else {
            return null;
        }
    }
}
