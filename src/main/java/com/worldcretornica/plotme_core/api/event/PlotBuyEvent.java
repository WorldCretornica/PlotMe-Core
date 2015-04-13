package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotBuyEvent extends PlotEvent implements ICancellable, Event {

    private final IPlayer buyer;
    private final double price;
    private boolean canceled;

    public PlotBuyEvent(IWorld world, Plot plot, IPlayer buyer, double price) {
        super(plot, world);
        this.buyer = buyer;
        this.price = price;
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
        return buyer;
    }

    public double getPrice() {
        return price;
    }

    public String getPreviousOwner() {
        return getPlot().getOwner();
    }

    @Override
    public String getOwner() {
        return buyer.getName();
    }
}
