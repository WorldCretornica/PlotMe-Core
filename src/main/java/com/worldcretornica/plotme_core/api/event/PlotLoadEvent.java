package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotLoadEvent extends PlotEvent implements Event {

    public PlotLoadEvent(IWorld world, Plot plot) {
        super(plot, world);
    }
}
