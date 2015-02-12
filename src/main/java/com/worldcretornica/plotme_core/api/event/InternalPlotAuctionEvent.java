package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotAuctionEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer player;
    private boolean canceled;
    private double minimumBid;

    public InternalPlotAuctionEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, double minimumBid) {
        super(plot, world);
        this.player = player;
        this.minimumBid = minimumBid;
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

    public void setMinimumBid(double minimumBid) {
        this.minimumBid = minimumBid;
    }
}
