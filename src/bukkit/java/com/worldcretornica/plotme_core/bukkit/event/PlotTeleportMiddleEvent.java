package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportMiddleEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotTeleportMiddleEvent extends PlotEvent implements Cancellable {

    private final InternalPlotTeleportMiddleEvent event;

    public PlotTeleportMiddleEvent(PlotMe_Core instance, World world, Plot plot, Player player, Location location) {
        super(plot, world);
        event = new InternalPlotTeleportMiddleEvent(new BukkitWorld(world), plot, new BukkitPlayer(player), new BukkitLocation(location));
    }

    public PlotTeleportMiddleEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, ILocation location) {
        super(plot, world);
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

    public Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public Location getMiddleLocation() {
        return ((BukkitLocation) event.getMiddleLocation()).getLocation();
    }

    public void setMiddleLocation(Location homeLocation) {
        event.setMiddleLocation(new BukkitLocation(homeLocation));
    }

    public InternalPlotTeleportMiddleEvent getInternal() {
        return event;
    }
}
