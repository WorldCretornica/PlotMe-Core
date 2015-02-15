package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotOwnerChangeEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer player;
    private final String newOwner;
    private boolean canceled;

    public InternalPlotOwnerChangeEvent(IWorld world, Plot plot, IPlayer player, String newOwner) {
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

    public String getNewOwner() {
        return newOwner;
    }
}
