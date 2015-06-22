package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;

public class PlotRemoveDeniedEvent extends PlotEvent implements ICancellable, Event {

    private final IPlayer player;
    private final String denied;
    private boolean canceled;

    public PlotRemoveDeniedEvent(Plot plot, IPlayer player, String denied) {
        super(plot);
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
