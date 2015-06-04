package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.Location;

public class PlotTeleportMiddleEvent extends PlotTeleportEvent implements ICancellable, Event {

    public PlotTeleportMiddleEvent(Plot plot, IPlayer player, Location middlelocation) {
        super(plot, player, middlelocation, plot.getId());
    }

    @Deprecated
    public Location getMiddleLocation() {
        return getLocation();
    }

    /**
     * Checks if the plot is claimed. This will always return true.
     * @return true
     */
    @Override
    public boolean isPlotClaimed() {
        return true;
    }
}
