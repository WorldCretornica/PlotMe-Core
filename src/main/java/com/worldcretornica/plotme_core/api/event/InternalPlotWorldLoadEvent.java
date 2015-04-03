package com.worldcretornica.plotme_core.api.event;

public class InternalPlotWorldLoadEvent implements Event {

    private final String world;
    private final int nbPlots;

    public InternalPlotWorldLoadEvent(String world, int nbPlots) {
        this.world = world;
        this.nbPlots = nbPlots;
    }

    /**
     * Returns the world used in the event
     *
     * @return world
     */
    public String getWorldName() {
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
