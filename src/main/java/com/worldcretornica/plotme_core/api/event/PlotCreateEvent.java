package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotCreateEvent extends PlotEvent implements ICancellable, Event {

    private final PlotId plotId;
    private final IPlayer creator;
    private boolean canceled;

    public PlotCreateEvent(IWorld world, PlotId plotId, IPlayer creator) {
        super(null, world);
        this.plotId = plotId;
        this.creator = creator;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    public PlotId getPlotId() {
        return plotId;
    }

    public IPlayer getPlayer() {
        return creator;
    }

}
