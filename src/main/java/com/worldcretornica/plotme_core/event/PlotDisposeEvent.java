package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotDisposeEvent extends PlotEvent implements Cancellable {

    private boolean _canceled;
    private Player _disposer;

    public PlotDisposeEvent(PlotMe_Core instance, World world, Plot plot, Player disposer) {
        super(instance, plot, world);
        _disposer = disposer;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        _canceled = cancel;
    }

    public Player getPlayer() {
        return _disposer;
    }
}
