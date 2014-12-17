package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotRemoveAllowedEvent extends InternalPlotEvent implements ICancellable {

    private boolean canceled;
    private final IPlayer player;
    private final String removed;

    public InternalPlotRemoveAllowedEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, String removed) {
        super(instance, plot, world);
        this.player = player;
        this.removed = removed;
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
        return player;
    }

    public String getRemovedAllowed() {
        return removed;
    }
}
