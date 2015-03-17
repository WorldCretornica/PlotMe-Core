package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddDeniedEvent;
import com.worldcretornica.plotme_core.sponge.api.SpongePlayer;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.util.event.Cancellable;

public class PlotAddDeniedEvent extends PlotEvent implements Cancellable {

    private final InternalPlotAddDeniedEvent event;

    public PlotAddDeniedEvent(IWorld world, Plot plot, IPlayer player, String denied) {
        super(plot, world);
        event = new InternalPlotAddDeniedEvent(world, plot, player, denied);
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

    public String getNewDenied() {
        return event.getNewDenied();
    }

    public InternalPlotAddDeniedEvent getInternal() {
        return event;
    }
}
