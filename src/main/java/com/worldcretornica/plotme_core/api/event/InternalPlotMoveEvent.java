package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotMoveEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private String _fromId;
    private String _toId;
    private IWorld _toworld;
    private IPlayer _mover;

    public InternalPlotMoveEvent(PlotMe_Core instance, IWorld fromworld, IWorld toworld, String fromId, String toId, IPlayer mover) {
        super(instance, null, fromworld);
        _fromId = fromId;
        _toId = toId;
        _toworld = toworld;
        _mover = mover;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        _canceled = cancel;
    }

    @Override
    public Plot getPlot() {
        return plugin.getPlotMeCoreManager().getPlotById(world, _fromId);
    }

    public Plot getPlotTo() {
        return plugin.getPlotMeCoreManager().getPlotById(_toworld, _toId);
    }

    public IWorld getWorldTo() {
        return _toworld;
    }

    public IPlayer getPlayer() {
        return _mover;
    }

    public String getId() {
        return _fromId;
    }

    public String getIdTo() {
        return _toId;
    }

    @Override
    public ILocation getUpperBound() {
        return plugin.getPlotMeCoreManager().getPlotTopLoc(world, _fromId);
    }

    @Override
    public ILocation getLowerBound() {
        return plugin.getPlotMeCoreManager().getPlotBottomLoc(world, _fromId);
    }

    public ILocation getUpperBoundTo() {
        return plugin.getPlotMeCoreManager().getPlotTopLoc(_toworld, _toId);
    }

    public ILocation getLowerBoundTo() {
        return plugin.getPlotMeCoreManager().getPlotBottomLoc(_toworld, _toId);
    }

    public String getOwnerTo() {
        Plot plot = getPlotTo();
        if (plot != null) {
            return plot.getOwner();
        } else {
            return "";
        }
    }
}
