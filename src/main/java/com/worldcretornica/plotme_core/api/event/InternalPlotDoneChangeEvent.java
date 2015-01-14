package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotDoneChangeEvent extends InternalPlotEvent implements ICancellable {

    private final Player player;
    private final boolean done;
    private boolean canceled;

    public InternalPlotDoneChangeEvent(PlotMe_Core instance, World world, Plot plot, Player player, boolean done) {
        super(instance, plot, world);
        this.player = player;
        this.done = done;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean getDone() {
        return done;
    }
}
