package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;

public class PlotProtectChangeEvent extends PlotEvent implements ICancellable, Event {

    private final IPlayer player;
    private final boolean _protected;
    private boolean canceled;

    public PlotProtectChangeEvent(Plot plot, IPlayer player, boolean protect) {
        super(plot);
        this.player = player;
        _protected = protect;
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

    public boolean isProtected() {
        return _protected;
    }
}
