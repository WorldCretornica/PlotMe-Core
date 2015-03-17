package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotRemoveAllowedEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotRemoveAllowedEvent extends PlotEvent implements Cancellable {

    private final InternalPlotRemoveAllowedEvent event;

    public PlotRemoveAllowedEvent(IWorld world, Plot plot, IPlayer player, String removed) {
        super(plot, world);
        event = new InternalPlotRemoveAllowedEvent(world, plot, player, removed);
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

    public String getRemovedAllowed() {
        return event.getRemovedAllowed();
    }

    public InternalPlotRemoveAllowedEvent getInternal() {
        return event;
    }
}
