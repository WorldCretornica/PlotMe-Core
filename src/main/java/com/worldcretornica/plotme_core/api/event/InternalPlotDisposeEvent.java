package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotDisposeEvent extends InternalPlotEvent implements ICancellable {

    private final Player disposer;
    private boolean canceled;

    public InternalPlotDisposeEvent(PlotMe_Core instance, World world, Plot plot, Player disposer) {
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

    public Player getPlayer() {
        return disposer;
    }
}
