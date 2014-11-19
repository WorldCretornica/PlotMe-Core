package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotTeleportHomeEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private final IPlayer _player;
    private ILocation loc;

    public InternalPlotTeleportHomeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player) {
        super(instance, plot, world);
        _player = player;
        loc = null;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        _canceled = cancel;
    }

    public IPlayer getPlayer() {
        return _player;
    }

    public void setHomeLocation(ILocation homelocation) {
        loc = homelocation;
    }

    @Override
    public ILocation getHomeLocation() {
        if (loc == null) {
            return super.getHomeLocation();
        } else {
            return loc;
        }
    }
}
