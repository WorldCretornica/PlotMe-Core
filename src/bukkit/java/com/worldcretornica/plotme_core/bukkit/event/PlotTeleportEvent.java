package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
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

    public PlotTeleportEvent(IWorld world, Plot plot, IPlayer player, ILocation loc, String plotId) {
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

    public Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public Location getLocation() {
        return ((BukkitLocation) event.getLocation()).getLocation();
    }

    public String getPlotId() {
        return event.getPlotId();
    }

    public boolean getIsPlotClaimed() {
        return (event.getPlot() != null);
    }

    public InternalPlotTeleportEvent getInternal() {
        return event;
    }
}
