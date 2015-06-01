package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;

public class PlotTeleportHomeEvent extends PlotTeleportEvent implements ICancellable, Event {

    public PlotTeleportHomeEvent(Plot plot, IPlayer player, ILocation location) {
        super(plot, player, location, plot.getId());
    }

    @Deprecated
    @Override
    public ILocation getHomeLocation() {
        return getLocation();
    }

    @Override
    public boolean isPlotClaimed() {
        return true;
    }
}
