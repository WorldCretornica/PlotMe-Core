package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotMoveEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotMoveEvent extends PlotEvent implements Cancellable {

    private final InternalPlotMoveEvent event;

    public PlotMoveEvent(PlotMe_Core instance, World world, PlotId fromId, PlotId toId, Player mover) {
        super(null, world);
        event = new InternalPlotMoveEvent(new BukkitWorld(world), fromId, toId, new BukkitPlayer(mover));
    }

    public PlotMoveEvent(PlotMe_Core instance, IWorld world, PlotId fromId, PlotId toId, IPlayer mover) {
        super(null, world);
        event = new InternalPlotMoveEvent(world, fromId, toId, mover);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    @Override
    public Plot getPlot() {
        return PlotMeCoreManager.getInstance().getPlotById(event.getId(), event.getWorld());
    }

    public Plot getPlotTo() {
        return PlotMeCoreManager.getInstance().getPlotById(event.getIdTo(), event.getWorld());
    }

    public Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public PlotId getId() {
        return event.getId();
    }

    public PlotId getIdTo() {
        return event.getIdTo();
    }

    @Override
    public Location getUpperBound() {
        return ((BukkitLocation) PlotMeCoreManager.getInstance().getPlotTopLoc(event.getWorld(), event.getId())).getLocation();
    }

    @Override
    public Location getLowerBound() {
        return ((BukkitLocation) PlotMeCoreManager.getInstance().getPlotBottomLoc(event.getWorld(), event.getId())).getLocation();
    }

    public Location getUpperBoundTo() {
        return ((BukkitLocation) PlotMeCoreManager.getInstance().getPlotTopLoc(event.getWorld(), event.getIdTo())).getLocation();
    }

    public Location getLowerBoundTo() {
        return ((BukkitLocation) PlotMeCoreManager.getInstance().getPlotBottomLoc(event.getWorld(), event.getIdTo())).getLocation();
    }

    public String getOwnerTo() {
        Plot plot = getPlotTo();
        if (plot != null) {
            return plot.getOwner();
        } else {
            return "";
        }
    }

    public InternalPlotMoveEvent getInternal() {
        return event;
    }
}
