package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotRemoveAllowedEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotRemoveAllowedEvent extends PlotEvent implements Cancellable {

    private final InternalPlotRemoveAllowedEvent event;

    public PlotRemoveAllowedEvent(PlotMe_Core instance, World world, Plot plot, Player player, String removed) {
        super(plot, world);
        event = new InternalPlotRemoveAllowedEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(player), removed);
    }

    public PlotRemoveAllowedEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, String removed) {
        super(plot, world);
        event = new InternalPlotRemoveAllowedEvent(instance, world, plot, player, removed);
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

    public String getRemovedAllowed() {
        return event.getRemovedAllowed();
    }

    public InternalPlotRemoveAllowedEvent getInternal() {
        return event;
    }
}
