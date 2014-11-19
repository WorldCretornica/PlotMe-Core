package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotOwnerChangeEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private final IPlayer player;
    private final String newowner;

    public InternalPlotOwnerChangeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, String newowner) {
        super(instance, plot, world);
        this.player = player;
        this.newowner = newowner;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        _canceled = cancel;
    }

    public IPlayer getPlayer() {
        return player;
    }

    public String getNewOwner() {
        return newowner;
    }
}
