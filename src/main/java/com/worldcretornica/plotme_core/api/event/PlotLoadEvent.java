package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;

public class PlotLoadEvent extends PlotEvent implements Event {

    public PlotLoadEvent(Plot plot) {
        super(plot);
    }
}
