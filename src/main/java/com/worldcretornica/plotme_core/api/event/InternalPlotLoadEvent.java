package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotLoadEvent extends InternalPlotEvent implements Event {

    public InternalPlotLoadEvent(IWorld world, Plot plot) {
        super(plot, world);
    }
}
