package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.api.IWorld;

public class PlotWorldLoadEvent implements Event {

    private final IWorld world;
    private final int nbPlots;

    public PlotWorldLoadEvent(IWorld world, int nbPlots) {
        this.world = world;
        this.nbPlots = nbPlots;
    }

    /**
     * Returns the world used in the event
     *
     * @return world
     */
    public IWorld getWorldName() {
        return world;
    }

    /**
     * Returns the number of plot in the world loaded
     *
     * @return number of plots
     */
    public int getNbPlots() {
        return nbPlots;
    }
}
