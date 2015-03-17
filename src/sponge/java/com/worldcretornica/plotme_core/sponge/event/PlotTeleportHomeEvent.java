package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportHomeEvent;
import org.spongepowered.api.world.Location;

public class PlotTeleportHomeEvent extends PlotTeleportEvent {

    private final InternalPlotTeleportHomeEvent event;

    public PlotTeleportHomeEvent(IWorld world, Plot plot, IPlayer player, ILocation location) {
        super(world, plot, player, location, plot.getId());
        event = new InternalPlotTeleportHomeEvent(world, plot, player, location);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    @Deprecated
    @Override
    public Location getHomeLocation() {
        return getLocation();
    }

    @Override
    public InternalPlotTeleportHomeEvent getInternal() {
        return event;
    }

    /**
     * Checks if the plot is claimed. This will always return true.
     * @return true
     */
    @Override
    public boolean isPlotClaimed() {
        return true;
    }
}
