package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotLoadEvent extends InternalPlotEvent {

    public InternalPlotLoadEvent(PlotMe_Core instance, IWorld world, Plot plot) {
        super(instance, plot, world);
    }
}
