package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.api.event.InternalPlotCreateEvent;
import com.worldcretornica.plotme_core.bukkit.api.*;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotCreateEvent extends PlotEvent implements Cancellable {

    private InternalPlotCreateEvent event;
    private PlotMe_Core plugin;

    public PlotCreateEvent(PlotMe_Core instance, World world, String plotId, Player creator) {
        super(instance, null, world);
        plugin = instance;
        event = new InternalPlotCreateEvent(instance, new BukkitWorld(world), plotId, new BukkitPlayer(creator));
    }
    
    public PlotCreateEvent(PlotMe_Core instance, IWorld world, String plotId, IPlayer creator) {
        super(instance, null, world);
        event = new InternalPlotCreateEvent(instance, world, plotId, creator);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCancelled(cancel);
    }

    public String getPlotId() {
        return event.getPlotId();
    }

    public Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    @Override
    public Location getUpperBound() {
        return ((BukkitLocation) plugin.getPlotMeCoreManager().getGenMan(event.getWorld()).getPlotTopLoc(event.getWorld(), event.getPlotId())).getLocation();
    }

    @Override
    public Location getLowerBound() {
        return ((BukkitLocation) plugin.getPlotMeCoreManager().getGenMan(event.getWorld()).getPlotBottomLoc(event.getWorld(), event.getPlotId())).getLocation();
    }
    
    public InternalPlotCreateEvent getInternal() {
        return event;
    }
}
