package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;

public class InternalPlotDoneChangeEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private IPlayer _player;
    private boolean _done;

    public InternalPlotDoneChangeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, boolean done) {
        super(instance, plot, world);
        _player = player;
        _done = done;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        _canceled = cancel;
    }

    public IPlayer getPlayer() {
        return _player;
    }

    public boolean getDone() {
        return _done;
    }
}
