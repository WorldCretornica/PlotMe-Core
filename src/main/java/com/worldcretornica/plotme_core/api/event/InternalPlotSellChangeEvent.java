package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotSellChangeEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer seller;
    private final double price;
    private final boolean isForSale;
    private boolean canceled;

    public InternalPlotSellChangeEvent(PlotMe_Core instance, World world, Plot plot, IPlayer seller, double price, boolean isForSale) {
        super(instance, plot, world);
        this.seller = seller;
        this.price = price;
        this.isForSale = isForSale;
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
        return seller;
    }

    public double getPrice() {
        return price;
    }

    public boolean isForSale() {
        return isForSale;
    }
}
