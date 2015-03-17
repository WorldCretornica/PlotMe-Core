package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotLoadEvent;

public class PlotLoadEvent extends PlotEvent {

    private final InternalPlotLoadEvent event;

    public PlotLoadEvent(IWorld world, Plot plot) {
        super(plot, world);
        event = new InternalPlotLoadEvent(world, plot);
    }

    public InternalPlotLoadEvent getInternal() {
        return event;
    }
}
