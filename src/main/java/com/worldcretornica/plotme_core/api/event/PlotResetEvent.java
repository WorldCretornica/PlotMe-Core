package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotResetEvent extends PlotEvent implements ICancellable, Event {

    private final ICommandSender reseter;
    private boolean canceled;

    public PlotResetEvent(IWorld world, Plot plot, ICommandSender reseter) {
        super(plot, world);
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
