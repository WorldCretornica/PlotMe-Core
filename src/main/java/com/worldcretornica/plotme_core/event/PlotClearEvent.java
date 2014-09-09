package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotClearEvent extends PlotEvent implements Cancellable {

    private boolean _canceled;
    private Player _clearer;

    public PlotClearEvent(PlotMe_Core instance, World world, Plot plot, Player clearer) {
        super(instance, plot, world);
        _clearer = clearer;
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
        return _clearer;
    }
}
