package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotBidEvent extends InternalPlotEvent implements ICancellable {

    private final Player player;
    private final double bid;
    private boolean canceled;

    public InternalPlotBidEvent(PlotMe_Core instance, World world, Plot plot, Player bidder, double bid) {
        super(instance, plot, world);
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

    public Player getPlayer() {
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
