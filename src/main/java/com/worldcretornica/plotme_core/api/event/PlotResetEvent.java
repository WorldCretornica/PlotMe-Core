package com.worldcretornica.plotme_core.api.event;

import com.google.common.base.Optional;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.ICommandSender;

public class PlotResetEvent extends PlotEvent implements ICancellable, Event {

    private final Optional<ICommandSender> reseter;
    private boolean canceled;

    public PlotResetEvent(Plot plot, ICommandSender reseter) {
        super(plot);
        this.reseter = Optional.fromNullable(reseter);
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    public Optional<ICommandSender> getReseter() {
        return reseter;
    }
}
