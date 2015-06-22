package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;

public class PlotClearEvent extends PlotEvent implements ICancellable, Event {

    private final IPlayer clearer;
    private boolean canceled;

    public PlotClearEvent(Plot plot, IPlayer clearer) {
        super(plot);
        this.clearer = clearer;
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
        return clearer;
    }
}
