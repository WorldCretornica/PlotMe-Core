package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotTeleportEvent extends PlotEvent implements Cancellable {

    private final InternalPlotTeleportEvent event;

    public PlotTeleportEvent(IWorld world, Plot plot, IPlayer player, ILocation loc, PlotId plotId) {
        super(plot, world);
        event = new InternalPlotTeleportEvent(world, plot, player, loc, plotId);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    /**
     * Get the {@link Player} that executed the command.
     * @return player that executed the event
     */
    public Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    /**
     * Get the home {@link Location} of the plot
     * @return home location of the plot
     */
    public Location getLocation() {
        return ((BukkitLocation) event.getLocation()).getLocation();
    }

    /**
     * The {@link PlotId} of the plot teleported to
     * @return plot id of the plot
     */
    public PlotId getPlotId() {
        return event.getPlotId();
    }

    /**
     * Checks if the plot is claimed.
     * @return if the plot is claimed
     */
    public boolean isPlotClaimed() {
        return event.getPlot() != null;
    }

    public InternalPlotTeleportEvent getInternal() {
        return event;
    }
}
