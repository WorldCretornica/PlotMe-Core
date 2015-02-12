package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotAuctionEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotAuctionEvent extends PlotEvent implements Cancellable {

    private final InternalPlotAuctionEvent event;

    public PlotAuctionEvent(PlotMe_Core instance, World world, Plot plot, Player player, double minimumBid) {
        super(plot, world);
        event = new InternalPlotAuctionEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(player), minimumBid);
    }

    public PlotAuctionEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, double minimumBid) {
        super(plot, world);
        event = new InternalPlotAuctionEvent(instance, world, plot, player, minimumBid);
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
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public double getMinimumBid() {
        return event.getMinimumBid();
    }

    public void setMinimumBid(double minimumBid) {
        event.setMinimumBid(minimumBid);
    }

    public InternalPlotAuctionEvent getInternal() {
        return event;
    }
}
