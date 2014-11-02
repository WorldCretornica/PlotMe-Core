package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
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

    private InternalPlotMoveEvent event;

    public PlotMoveEvent(PlotMe_Core instance, World fromworld, World toworld, String fromId, String toId, Player mover) {
        super(instance, null, fromworld);
        event = new InternalPlotMoveEvent(instance, new BukkitWorld(fromworld), new BukkitWorld(toworld), fromId, toId, new BukkitPlayer(mover));
    }

    public PlotMoveEvent(PlotMe_Core instance, IWorld fromworld, IWorld toworld, String fromId, String toId, IPlayer mover) {
        super(instance, null, fromworld);
        event = new InternalPlotMoveEvent(instance, fromworld, toworld, fromId, toId, mover);
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
        return plugin.getPlotMeCoreManager().getPlotById(event.getWorld(), event.getId());
    }

    public Plot getPlotTo() {
        return plugin.getPlotMeCoreManager().getPlotById(event.getWorldTo(), event.getIdTo());
    }

    public World getWorldTo() {
        return ((BukkitWorld) event.getWorldTo()).getWorld();
    }

    public Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public String getId() {
        return event.getId();
    }

    public String getIdTo() {
        return event.getIdTo();
    }

    @Override
    public Location getUpperBound() {
        return ((BukkitLocation) plugin.getPlotMeCoreManager().getPlotTopLoc(event.getWorld(), event.getId())).getLocation();
    }

    @Override
    public Location getLowerBound() {
        return ((BukkitLocation) plugin.getPlotMeCoreManager().getPlotBottomLoc(event.getWorld(), event.getId())).getLocation();
    }

    public Location getUpperBoundTo() {
        return ((BukkitLocation) plugin.getPlotMeCoreManager().getPlotTopLoc(event.getWorldTo(), event.getIdTo())).getLocation();
    }

    public Location getLowerBoundTo() {
        return ((BukkitLocation) plugin.getPlotMeCoreManager().getPlotBottomLoc(event.getWorldTo(), event.getIdTo())).getLocation();
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
