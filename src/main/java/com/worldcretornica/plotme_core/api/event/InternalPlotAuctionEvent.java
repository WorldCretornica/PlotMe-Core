package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotAuctionEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private IPlayer _player;
    private double _minimumbid;

    public InternalPlotAuctionEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, double minimumbid) {
        super(instance, plot, world);
        _player = player;
        _minimumbid = minimumbid;
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

    public double getMinimumBid() {
        return _minimumbid;
    }
    
    public void setMinimumBid(double minimumbid) {
        _minimumbid = minimumbid;
    }
}
