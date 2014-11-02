package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotBidEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private IPlayer _bidder;
    private double _bid;

    public InternalPlotBidEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer bidder, double bid) {
        super(instance, plot, world);
        _bidder = bidder;
        _bid = bid;
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
        return _bidder;
    }

    public double getBid() {
        return _bid;
    }

    public double getPreviousBid() {
        return plot.getCurrentBid();
    }

    public String getPreviousBidder() {
        return plot.getCurrentBidder();
    }
}
