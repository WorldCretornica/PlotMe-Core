package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.ICancellable;
import com.worldcretornica.plotme_core.api.event.InternalPlotCreateEvent;

public class PlotCreateEvent extends PlotEvent implements ICancellable {

    private final InternalPlotCreateEvent event;

    public PlotCreateEvent(IWorld world, PlotId plotId, IPlayer creator) {
        event = new InternalPlotCreateEvent(world, plotId, creator);
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCanceled(boolean cancel) {
        event.setCanceled(cancel);
    }
}
