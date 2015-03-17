package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotOwnerChangeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotOwnerChangeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotOwnerChangeEvent event;

    public PlotOwnerChangeEvent(IWorld world, Plot plot, IPlayer player, String newOwner) {
        super(plot, world);
        event = new InternalPlotOwnerChangeEvent(world, plot, player, newOwner);
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

    public String getNewOwner() {
        return event.getNewOwner();
    }

    public InternalPlotOwnerChangeEvent getInternal() {
        return event;
    }
}
