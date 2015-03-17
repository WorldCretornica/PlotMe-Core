package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddAllowedEvent;
import com.worldcretornica.plotme_core.sponge.api.SpongePlayer;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.util.event.Cancellable;

public class PlotAddAllowedEvent extends PlotEvent implements Cancellable {

    private final InternalPlotAddAllowedEvent event;

    public PlotAddAllowedEvent(IWorld world, Plot plot, IPlayer player, String allowed) {
        super(plot, world);
        event = new InternalPlotAddAllowedEvent(world, plot, player, allowed);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean val) {
        event.setCanceled(val);
    }

    public Player getPlayer() {
        return ((SpongePlayer) event.getPlayer()).getPlayer();
    }

    public String getNewAllowed() {
        return event.getNewAllowed();
    }

    public InternalPlotAddAllowedEvent getInternal() {
        return event;
    }
}
