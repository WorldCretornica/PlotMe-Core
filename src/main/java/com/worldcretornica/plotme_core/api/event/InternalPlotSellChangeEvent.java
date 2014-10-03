package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;

public class InternalPlotSellChangeEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private IPlayer _seller;
    private double _price;
    private boolean _soldToBank;
    private boolean _isForSale;

    public InternalPlotSellChangeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer seller, double price, boolean soldToBank, boolean isForSale) {
        super(instance, plot, world);
        _seller = seller;
        _price = price;
        _isForSale = isForSale;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        _canceled = cancel;
    }

    public IPlayer getPlayer() {
        return _seller;
    }

    public double getPrice() {
        return _price;
    }

    public boolean isSoldToBank() {
        return _soldToBank;
    }

    public boolean isForSale() {
        return _isForSale;
    }
}
