package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotMoveEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.sponge.api.SpongeLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.spongepowered.api.world.Location;

public class PlotMoveEvent extends PlotEvent implements Cancellable {

    private final InternalPlotMoveEvent event;

    public PlotMoveEvent(IWorld world, PlotId fromId, PlotId toId, IPlayer mover) {
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
        return ((SpongeLocation) PlotMeCoreManager.getInstance().getPlotTopLoc(event.getWorld(), event.getId())).getLocation();
    }

    @Override
    public Location getLowerBound() {
        return ((SpongeLocation) PlotMeCoreManager.getInstance().getPlotBottomLoc(event.getWorld(), event.getId())).getLocation();
    }

    public Location getUpperBoundTo() {
        return ((SpongeLocation) PlotMeCoreManager.getInstance().getPlotTopLoc(event.getWorld(), event.getIdTo())).getLocation();
    }

    public Location getLowerBoundTo() {
        return ((SpongeLocation) PlotMeCoreManager.getInstance().getPlotBottomLoc(event.getWorld(), event.getIdTo())).getLocation();
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
