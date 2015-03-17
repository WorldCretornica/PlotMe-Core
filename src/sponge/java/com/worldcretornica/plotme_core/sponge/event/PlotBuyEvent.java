package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotBuyEvent;
import com.worldcretornica.plotme_core.sponge.api.SpongePlayer;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.util.event.Cancellable;

public class PlotBuyEvent extends PlotEvent implements Cancellable {

    private final InternalPlotBuyEvent event;

    public PlotBuyEvent(IWorld world, Plot plot, IPlayer buyer, double price) {
        super(plot, world);
        event = new InternalPlotBuyEvent(world, plot, buyer, price);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    public Player getPlayer() {
        return ((SpongePlayer) event.getPlayer()).getPlayer();
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
