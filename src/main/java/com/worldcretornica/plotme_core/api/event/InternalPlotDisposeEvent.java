package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotDisposeEvent extends InternalPlotEvent implements ICancellable {

    private boolean canceled;
    private final IPlayer disposer;

    public InternalPlotDisposeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer disposer) {
        super(instance, plot, world);
        this.disposer = disposer;
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
        return disposer;
    }
}
