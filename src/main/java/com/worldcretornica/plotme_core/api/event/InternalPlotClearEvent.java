package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotClearEvent extends InternalPlotEvent implements ICancellable {

    private final Player clearer;
    private boolean canceled;

    public InternalPlotClearEvent(PlotMe_Core instance, World world, Plot plot, Player clearer) {
        super(instance, plot, world);
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

    public Player getPlayer() {
        return clearer;
    }
}
