package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotProtectChangeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotProtectChangeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotProtectChangeEvent event;

    public PlotProtectChangeEvent(World world, Plot plot, Player player, boolean protect) {
        super(plot, world);
        event = new InternalPlotProtectChangeEvent(new BukkitWorld(world), plot, new BukkitPlayer(player), protect);
    }

    public PlotProtectChangeEvent(IWorld world, Plot plot, IPlayer player, boolean protect) {
        super(plot, world);
        event = new InternalPlotProtectChangeEvent(world, plot, player, protect);
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

    public boolean isProtected() {
        return event.isProtected();
    }

    public InternalPlotProtectChangeEvent getInternal() {
        return event;
    }
}
