package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotRemoveDeniedEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private IPlayer _player;
    private String _denied;

    public InternalPlotRemoveDeniedEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, String denied) {
        super(instance, plot, world);
        _player = player;
        _denied = denied;
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

    public String getRemovedDenied() {
        return _denied;
    }
}
