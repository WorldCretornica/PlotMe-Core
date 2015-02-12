package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotBidEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer player;
    private final double bid;
    private boolean canceled;

    public InternalPlotBidEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer bidder, double bid) {
        super(plot, world);
        player = bidder;
        this.bid = bid;
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

    public double getBid() {
        return bid;
    }

    public double getPreviousBid() {
        return getPlot().getCurrentBid();
    }

    public String getPreviousBidder() {
        return getPlot().getCurrentBidder();
    }
}
