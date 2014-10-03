package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;

public class InternalPlotClearEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private IPlayer _clearer;

    public InternalPlotClearEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer clearer) {
        super(instance, plot, world);
        _clearer = clearer;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        _canceled = cancel;
    }

    public IPlayer getPlayer() {
        return _clearer;
    }
}
