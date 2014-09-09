package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotBidEvent extends PlotEvent implements Cancellable {

    private boolean _canceled;
    private Player _bidder;
    private double _bid;

    public PlotBidEvent(PlotMe_Core instance, World world, Plot plot, Player bidder, double bid) {
        super(instance, plot, world);
        _bidder = bidder;
        _bid = bid;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        _canceled = cancel;
    }

    public Player getPlayer() {
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
