package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotSellChangeEvent extends PlotEvent implements Cancellable {

    private boolean _canceled;
    private Player _seller;
    private double _price;
    private boolean _soldToBank;
    private boolean _isForSale;

    public PlotSellChangeEvent(PlotMe_Core instance, World world, Plot plot, Player seller, double price, boolean soldToBank, boolean isForSale) {
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

    public Player getPlayer() {
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
