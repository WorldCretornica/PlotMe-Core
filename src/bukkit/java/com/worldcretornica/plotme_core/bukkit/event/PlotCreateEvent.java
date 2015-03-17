package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotCreateEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotCreateEvent extends PlotEvent implements Cancellable {

    private final InternalPlotCreateEvent event;

    public PlotCreateEvent(IWorld world, PlotId plotId, IPlayer creator) {
        super(null, world);
        event = new InternalPlotCreateEvent(world, plotId, creator);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    public PlotId getPlotId() {
        return event.getPlotId();
    }

    public Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    @Override
    public Location getUpperBound() {
        return ((BukkitLocation) PlotMeCoreManager.getInstance().getPlotTopLoc(event.getWorld(), event.getPlotId())).getLocation();
    }

    @Override
    public Location getLowerBound() {
        return ((BukkitLocation) PlotMeCoreManager.getInstance().getPlotBottomLoc(event.getWorld(), event.getPlotId())).getLocation();
    }

    public InternalPlotCreateEvent getInternal() {
        return event;
    }
}
