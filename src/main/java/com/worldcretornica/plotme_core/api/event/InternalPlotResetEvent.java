package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotResetEvent extends InternalPlotEvent implements ICancellable {

    private final ICommandSender reseter;
    private boolean canceled;

    public InternalPlotResetEvent(PlotMe_Core instance, IWorld world, Plot plot, ICommandSender reseter) {
        super(instance, plot, world);
        this.reseter = reseter;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    public ICommandSender getReseter() {
        return reseter;
    }
}
