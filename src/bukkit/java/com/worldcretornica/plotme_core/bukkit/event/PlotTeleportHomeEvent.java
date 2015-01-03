package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportHomeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotTeleportHomeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotTeleportHomeEvent event;

    public PlotTeleportHomeEvent(PlotMe_Core instance, World world, Plot plot, Player player) {
        super(instance, plot, world);
        event = new InternalPlotTeleportHomeEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(player));
    }

    public PlotTeleportHomeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player) {
        super(instance, plot, world);
        event = new InternalPlotTeleportHomeEvent(instance, world, plot, player);
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

    @Override
    public Location getHomeLocation() {
        if (event.getHomeLocation() == null) {
            return super.getHomeLocation();
        } else {
            return ((BukkitLocation) event.getHomeLocation()).getLocation();
        }
    }

    public void setHomeLocation(Location homelocation) {
        event.setHomeLocation(new BukkitLocation(homelocation));
    }

    public InternalPlotTeleportHomeEvent getInternal() {
        return event;
    }
}
