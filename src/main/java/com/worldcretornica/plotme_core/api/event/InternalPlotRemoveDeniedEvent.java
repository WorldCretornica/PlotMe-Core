package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotRemoveDeniedEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer player;
    private final String denied;
    private boolean canceled;

    public InternalPlotRemoveDeniedEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, String denied) {
        super(instance, plot, world);
        this.player = player;
        this.denied = denied;
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

    public String getRemovedDenied() {
        return denied;
    }
}
