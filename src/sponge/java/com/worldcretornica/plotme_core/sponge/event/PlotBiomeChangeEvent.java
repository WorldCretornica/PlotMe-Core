package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotBiomeChangeEvent;
import com.worldcretornica.plotme_core.sponge.api.SpongePlayer;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.util.event.Cancellable;

public class PlotBiomeChangeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotBiomeChangeEvent event;

    public PlotBiomeChangeEvent(IWorld world, Plot plot, IPlayer player, IBiome biome) {
        super(plot, world);
        event = new InternalPlotBiomeChangeEvent(world, plot, player, biome);
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

    public InternalPlotBiomeChangeEvent getInternal() {
        return event;
    }
}
