package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.api.event.InternalPlotProtectChangeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.event.Cancellable;

public class PlotProtectChangeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotProtectChangeEvent event;

    public PlotProtectChangeEvent(PlotMe_Core instance, org.bukkit.World world, Plot plot, org.bukkit.entity.Player player, boolean protect) {
        super(instance, plot, world);
        event = new InternalPlotProtectChangeEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(player), protect);
    }

    public PlotProtectChangeEvent(PlotMe_Core instance, World world, Plot plot, Player player, boolean protect) {
        super(instance, plot, world);
        event = new InternalPlotProtectChangeEvent(instance, world, plot, player, protect);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    public org.bukkit.entity.Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public boolean getProtected() {
        return event.getProtected();
    }

    public InternalPlotProtectChangeEvent getInternal() {
        return event;
    }
}
