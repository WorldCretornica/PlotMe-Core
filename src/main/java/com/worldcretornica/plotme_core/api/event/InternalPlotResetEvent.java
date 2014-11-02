package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotResetEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private ICommandSender _reseter;

    public InternalPlotResetEvent(PlotMe_Core instance, IWorld world, Plot plot, ICommandSender reseter) {
        super(instance, plot, world);
        _reseter = reseter;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        _canceled = cancel;
    }

    public ICommandSender getReseter() {
        return _reseter;
    }
}
