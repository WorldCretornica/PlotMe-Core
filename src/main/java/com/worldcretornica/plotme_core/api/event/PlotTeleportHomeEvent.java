package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.Location;

public class PlotTeleportHomeEvent extends PlotTeleportEvent implements ICancellable, Event {

    public PlotTeleportHomeEvent(Plot plot, IPlayer player, Location location) {
        super(plot, player, location, plot.getId());
    }

    @Deprecated
    @Override
    public Location getHomeLocation() {
        return getLocation();
    }

    @Override
    public boolean isPlotClaimed() {
        return true;
    }
}
