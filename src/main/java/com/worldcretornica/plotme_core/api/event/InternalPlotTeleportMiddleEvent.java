package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotTeleportMiddleEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer player;
    private boolean canceled;
    private ILocation location;

    public InternalPlotTeleportMiddleEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, ILocation middlelocation) {
        super(instance, plot, world);
        this.player = player;
        location = middlelocation;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    public IPlayer getPlayer() {
        return player;
    }

    public ILocation getMiddleLocation() {
        return location;
    }

    public void setMiddleLocation(ILocation middleLocation) {
        location = middleLocation;
    }
}
