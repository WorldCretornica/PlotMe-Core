package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotTeleportMiddleEvent extends InternalPlotTeleportEvent implements ICancellable, Event {

    public InternalPlotTeleportMiddleEvent(IWorld world, Plot plot, IPlayer player, ILocation middlelocation) {
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
