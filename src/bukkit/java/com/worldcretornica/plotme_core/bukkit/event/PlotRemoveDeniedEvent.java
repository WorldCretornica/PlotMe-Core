package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotRemoveDeniedEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotRemoveDeniedEvent extends PlotEvent implements Cancellable {

    private final InternalPlotRemoveDeniedEvent event;

    public PlotRemoveDeniedEvent(PlotMe_Core instance, World world, Plot plot, Player player, String denied) {
        super(instance, plot, world);
        event = new InternalPlotRemoveDeniedEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(player), denied);
    }

    public PlotRemoveDeniedEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, String denied) {
        super(instance, plot, world);
        event = new InternalPlotRemoveDeniedEvent(instance, world, plot, player, denied);
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

    public String getRemovedDenied() {
        return event.getRemovedDenied();
    }

    public InternalPlotRemoveDeniedEvent getInternal() {
        return event;
    }
}
