package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotOwnerChangeEvent extends PlotEvent implements ICancellable, Event {

    private final IPlayer player;
    private final IPlayer newOwner;
    private boolean canceled;

    public PlotOwnerChangeEvent(IWorld world, Plot plot, IPlayer player, IPlayer newOwner) {
        super(plot, world);
        this.player = player;
        this.newOwner = newOwner;
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

    public IPlayer getNewOwner() {
        return newOwner;
    }
}
