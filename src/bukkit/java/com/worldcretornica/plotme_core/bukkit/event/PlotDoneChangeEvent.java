package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotDoneChangeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotDoneChangeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotDoneChangeEvent event;

    public PlotDoneChangeEvent(IWorld world, Plot plot, IPlayer player, boolean done) {
        super(plot, world);
        event = new InternalPlotDoneChangeEvent(world, plot, player, done);
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

    public boolean isDone() {
        return event.isDone();
    }

    public InternalPlotDoneChangeEvent getInternal() {
        return event;
    }
}
