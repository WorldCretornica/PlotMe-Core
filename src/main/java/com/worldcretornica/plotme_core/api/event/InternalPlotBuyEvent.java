package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotBuyEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private IPlayer _buyer;
    private double _price;

    public InternalPlotBuyEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer buyer, double price) {
        super(instance, plot, world);
        _buyer = buyer;
        _price = price;
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
        return _buyer;
    }

    public double getPrice() {
        return _price;
    }

    public String getPreviousOwner() {
        return plot.getOwner();
    }

    @Override
    public String getOwner() {
        return _buyer.getName();
    }
}
