package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.CommandSender;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotResetEvent extends InternalPlotEvent implements ICancellable {

    private final CommandSender reseter;
    private boolean canceled;

    public InternalPlotResetEvent(PlotMe_Core instance, World world, Plot plot, CommandSender reseter) {
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

    public CommandSender getReseter() {
        return reseter;
    }
}
