package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotTeleportEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer player;
    private final String plotId;
    private final ILocation location;
    private boolean canceled;

    public InternalPlotTeleportEvent(IWorld world, Plot plot, IPlayer player, ILocation location, String plotId) {
        super(plot, world);
        this.player = player;
        this.location = location;
        this.plotId = plotId;
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

    public ILocation getLocation() {
        return location;
    }

    public String getPlotId() {
        return plotId;
    }

    public boolean getIsPlotClaimed() {
        return (getPlot() != null);
    }
}
