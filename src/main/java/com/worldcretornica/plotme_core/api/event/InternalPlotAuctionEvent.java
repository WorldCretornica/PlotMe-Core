package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotAuctionEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer player;
    private boolean canceled;
    private double minimumBid;

    public InternalPlotAuctionEvent(PlotMe_Core instance, World world, Plot plot, IPlayer player, double minimumbid) {
        super(instance, plot, world);
        this.player = player;
        minimumBid = minimumbid;
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

    public double getMinimumBid() {
        return minimumBid;
    }

    public void setMinimumBid(double minimumbid) {
        minimumBid = minimumbid;
    }
}
