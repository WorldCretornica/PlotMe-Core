package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotOwnerChangeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotOwnerChangeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotOwnerChangeEvent event;

    public PlotOwnerChangeEvent(World world, Plot plot, Player player, String newOwner) {
        super(plot, world);
        event = new InternalPlotOwnerChangeEvent(new BukkitWorld(world), plot, new BukkitPlayer(player), newOwner);
    }

    public PlotOwnerChangeEvent(IWorld world, Plot plot, IPlayer player, String newOwner) {
        super(plot, world);
        event = new InternalPlotOwnerChangeEvent(world, plot, player, newOwner);
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

    public String getNewOwner() {
        return event.getNewOwner();
    }

    public InternalPlotOwnerChangeEvent getInternal() {
        return event;
    }
}
