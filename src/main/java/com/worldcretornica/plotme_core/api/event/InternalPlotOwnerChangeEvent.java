package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotOwnerChangeEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer player;
    private final String newowner;
    private boolean canceled;

    public InternalPlotOwnerChangeEvent(PlotMe_Core instance, World world, Plot plot, IPlayer player, String newowner) {
        super(instance, plot, world);
        this.player = player;
        this.newowner = newowner;
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
        return newowner;
    }
}
