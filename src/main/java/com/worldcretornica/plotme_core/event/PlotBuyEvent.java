package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotBuyEvent extends PlotEvent implements Cancellable {

    private boolean _canceled;
    private Player _buyer;
    private double _price;

    public PlotBuyEvent(PlotMe_Core instance, World world, Plot plot, Player buyer, double price) {
        super(instance, plot, world);
        _buyer = buyer;
        _price = price;
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
