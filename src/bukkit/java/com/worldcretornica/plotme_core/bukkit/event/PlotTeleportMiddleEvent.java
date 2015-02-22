package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportMiddleEvent;
import org.bukkit.Location;

public class PlotTeleportMiddleEvent extends PlotTeleportEvent {

    private final InternalPlotTeleportMiddleEvent event;

    public PlotTeleportMiddleEvent(IWorld world, Plot plot, IPlayer player, ILocation location) {
        super(world, plot, player, location, plot.getId());
        event = new InternalPlotTeleportMiddleEvent(world, plot, player, location);
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
    public Location getMiddleLocation() {
        return getLocation();
    }

    @Override
    public InternalPlotTeleportMiddleEvent getInternal() {
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
