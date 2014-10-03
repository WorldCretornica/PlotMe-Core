package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;

public class InternalPlotAddAllowedEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private IPlayer _player;
    private String _allowed;

    public InternalPlotAddAllowedEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, String allowed) {
        super(instance, plot, world);
        _player = player;
        _allowed = allowed;
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

    public String getNewAllowed() {
        return _allowed;
    }
}
