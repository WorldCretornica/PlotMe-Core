package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotTeleportHomeEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer player;
    private boolean canceled;
    private ILocation location;

    public InternalPlotTeleportHomeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player) {
        super(instance, plot, world);
        this.player = player;
        location = null;
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

    @Override
    public ILocation getHomeLocation() {
        if (location == null) {
            return super.getHomeLocation();
        } else {
            return location;
        }
    }

    public void setHomeLocation(ILocation homelocation) {
        location = homelocation;
    }
}
