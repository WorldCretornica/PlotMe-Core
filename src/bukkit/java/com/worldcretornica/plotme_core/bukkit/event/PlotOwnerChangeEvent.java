package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotOwnerChangeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotOwnerChangeEvent extends PlotEvent implements Cancellable {

    private InternalPlotOwnerChangeEvent event;

    public PlotOwnerChangeEvent(PlotMe_Core instance, World world, Plot plot, Player player, String newowner) {
        super(instance, plot, world);
        event = new InternalPlotOwnerChangeEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(player), newowner);
    }

    public PlotOwnerChangeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, String newowner) {
        super(instance, plot, world);
        event = new InternalPlotOwnerChangeEvent(instance, world, plot, player, newowner);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCancelled(cancel);
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
