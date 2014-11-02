package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotTeleportEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private IPlayer _player;
    private String _plotid;
    private ILocation _loc;

    public InternalPlotTeleportEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, ILocation loc, String plotId) {
        super(instance, plot, world);
        _player = player;
        _loc = loc;
        _plotid = plotId;
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

    public ILocation getLocation() {
        return _loc;
    }

    public String getPlotId() {
        return _plotid;
    }

    public boolean getIsPlotClaimed() {
        return (plot != null);
    }
}
