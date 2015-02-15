package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotClearEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer clearer;
    private boolean canceled;

    public InternalPlotClearEvent(IWorld world, Plot plot, IPlayer clearer) {
        super(plot, world);
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
