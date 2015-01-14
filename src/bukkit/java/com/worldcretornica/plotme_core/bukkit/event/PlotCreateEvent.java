package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.api.event.InternalPlotCreateEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;

public class PlotCreateEvent extends PlotEvent implements Cancellable {

    private final InternalPlotCreateEvent event;

    public PlotCreateEvent(PlotMe_Core instance, org.bukkit.World world, String plotId, org.bukkit.entity.Player creator) {
        super(instance, null, world);
        event = new InternalPlotCreateEvent(instance, new BukkitWorld(world), plotId, new BukkitPlayer(creator));
    }

    public PlotCreateEvent(PlotMe_Core instance, World world, String plotId, Player creator) {
        super(instance, null, world);
        event = new InternalPlotCreateEvent(instance, world, plotId, creator);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    public String getPlotId() {
        return event.getPlotId();
    }

    public org.bukkit.entity.Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    @Override
    public Location getUpperBound() {
        return ((BukkitLocation) PlotMeCoreManager.getPlotTopLoc(event.getWorld(), event.getPlotId())).getLocation();
    }

    @Override
    public Location getLowerBound() {
        return ((BukkitLocation) PlotMeCoreManager.getPlotBottomLoc(event.getWorld(), event.getPlotId())).getLocation();
    }

    public InternalPlotCreateEvent getInternal() {
        return event;
    }
}
