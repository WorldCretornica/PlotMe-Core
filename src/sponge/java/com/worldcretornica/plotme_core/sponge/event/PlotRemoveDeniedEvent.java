package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotRemoveDeniedEvent;
import com.worldcretornica.plotme_core.sponge.api.SpongePlayer;
import org.bukkit.event.Cancellable;
import org.spongepowered.api.entity.player.Player;

public class PlotRemoveDeniedEvent extends PlotEvent implements Cancellable {

    private final InternalPlotRemoveDeniedEvent event;

    public PlotRemoveDeniedEvent(IWorld world, Plot plot, IPlayer player, String denied) {
        super(plot, world);
        event = new InternalPlotRemoveDeniedEvent(world, plot, player, denied);
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

    public String getRemovedDenied() {
        return event.getRemovedDenied();
    }

    public InternalPlotRemoveDeniedEvent getInternal() {
        return event;
    }
}
