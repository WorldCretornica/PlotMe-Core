package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotProtectChangeEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private IPlayer _player;
    private boolean _protected;

    public InternalPlotProtectChangeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, boolean protect) {
        super(instance, plot, world);
        _player = player;
        _protected = protect;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        _canceled = cancel;
    }

    public IPlayer getPlayer() {
        return _player;
    }

    public boolean getProtected() {
        return _protected;
    }
}
