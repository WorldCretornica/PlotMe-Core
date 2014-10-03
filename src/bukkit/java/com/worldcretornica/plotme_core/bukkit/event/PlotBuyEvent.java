package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.api.event.InternalPlotBuyEvent;
import com.worldcretornica.plotme_core.bukkit.api.*;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotBuyEvent extends PlotEvent implements Cancellable {

    private InternalPlotBuyEvent event;

    public PlotBuyEvent(PlotMe_Core instance, World world, Plot plot, Player buyer, double price) {
        super(instance, plot, world);
        event = new InternalPlotBuyEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(buyer), price);
    }
    
    public PlotBuyEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer buyer, double price) {
        super(instance, plot, world);
        event = new InternalPlotBuyEvent(instance, world, plot, buyer, price);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCancelled(cancel);
    }

    public Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public double getPrice() {
        return event.getPrice();
    }

    public String getPreviousOwner() {
        return event.getPlot().getOwner();
    }

    @Override
    public String getOwner() {
        return event.getOwner();
    }
    
    public InternalPlotBuyEvent getInternal() {
        return event;
    }
}
