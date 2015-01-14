package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.api.event.InternalPlotDoneChangeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.event.Cancellable;

public class PlotDoneChangeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotDoneChangeEvent event;

    public PlotDoneChangeEvent(PlotMe_Core instance, World world, Plot plot, Player player, boolean done) {
        super(instance, plot, world);
        event = new InternalPlotDoneChangeEvent(instance, world, plot, player, done);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    public org.bukkit.entity.Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public boolean getDone() {
        return event.getDone();
    }

    public InternalPlotDoneChangeEvent getInternal() {
        return event;
    }
}
