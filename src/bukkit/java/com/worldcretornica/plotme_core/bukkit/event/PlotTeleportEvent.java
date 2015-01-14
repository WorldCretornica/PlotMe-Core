package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.event.Cancellable;

public class PlotTeleportEvent extends PlotEvent implements Cancellable {

    private final InternalPlotTeleportEvent event;

    public PlotTeleportEvent(PlotMe_Core instance, World world, Plot plot, Player player, Location loc, String plotId) {
        super(instance, plot, world);
        event = new InternalPlotTeleportEvent(instance, world, plot, player, loc, plotId);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    public org.bukkit.entity.Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public org.bukkit.Location getLocation() {
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
