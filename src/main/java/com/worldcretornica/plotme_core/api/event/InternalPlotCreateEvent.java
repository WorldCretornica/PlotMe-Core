package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;

public class InternalPlotCreateEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private String _plotId;
    private IPlayer _creator;

    public InternalPlotCreateEvent(PlotMe_Core instance, IWorld world, String plotId, IPlayer creator) {
        super(instance, null, world);
        _plotId = plotId;
        _creator = creator;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        _canceled = cancel;
    }

    public String getPlotId() {
        return _plotId;
    }

    public IPlayer getPlayer() {
        return _creator;
    }

    @Override
    public ILocation getUpperBound() {
        return plugin.getPlotMeCoreManager().getGenMan(world).getPlotTopLoc(world, _plotId);
    }

    @Override
    public ILocation getLowerBound() {
        return plugin.getPlotMeCoreManager().getGenMan(world).getPlotBottomLoc(world, _plotId);
    }
}
