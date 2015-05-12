package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;

public class PlotTeleportMiddleEvent extends PlotTeleportEvent implements ICancellable, Event {

    public PlotTeleportMiddleEvent(IWorld world, Plot plot, IPlayer player, Vector middlelocation) {
        super(world, plot, player, middlelocation, plot.getId());
    }

    @Deprecated
    public ILocation getMiddleLocation() {
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
