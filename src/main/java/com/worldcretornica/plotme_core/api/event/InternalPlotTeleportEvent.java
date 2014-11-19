package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotTeleportEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private final IPlayer player;
    private final String plotId;
    private final ILocation loc;

    public InternalPlotTeleportEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, ILocation loc, String plotId) {
        super(instance, plot, world);
        this.player = player;
        this.loc = loc;
        this.plotId = plotId;
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
        return player;
    }

    public ILocation getLocation() {
        return loc;
    }

    public String getPlotId() {
        return plotId;
    }

    public boolean getIsPlotClaimed() {
        return (plot != null);
    }
}
