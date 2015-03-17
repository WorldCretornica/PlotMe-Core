package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.api.event.InternalPlotWorldLoadEvent;
import org.spongepowered.api.event.AbstractEvent;

public class PlotWorldLoadEvent extends AbstractEvent {

    private final InternalPlotWorldLoadEvent event;

    public PlotWorldLoadEvent(String world, int nbPlots) {
        event = new InternalPlotWorldLoadEvent(world, nbPlots);
    }

    /**
     * Returns the world used in the event
     *
     * @return world
     */
    public String getWorldName() {
        return event.getWorldName();
    }

    /**
     * Returns the number of plot in the world loaded
     *
     * @return number of plots
     */
    public int getNbPlots() {
        return event.getNbPlots();
    }

    public InternalPlotWorldLoadEvent getInternal() {
        return event;
    }
}
