package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotRemoveAllowedEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer player;
    private final String removed;
    private boolean canceled;

    public InternalPlotRemoveAllowedEvent(PlotMe_Core instance, World world, Plot plot, IPlayer player, String removed) {
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
