package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotMoveEvent extends InternalPlotEvent implements ICancellable, Event {

    private final PlotId fromId;
    private final PlotId toId;
    private final IWorld world;
    private final IPlayer mover;
    private boolean canceled;

    public InternalPlotMoveEvent(IWorld world, PlotId fromId, PlotId toId, IPlayer mover) {
        super(null, world);
        this.fromId = fromId;
        this.toId = toId;
        this.world = world;
        this.mover = mover;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    @Override
    public Plot getPlot() {
        return PlotMeCoreManager.getInstance().getPlotById(fromId);
    }

    public Plot getPlotTo() {
        return PlotMeCoreManager.getInstance().getPlotById(toId);
    }

    public IPlayer getPlayer() {
        return mover;
    }

    public PlotId getId() {
        return fromId;
    }

    public PlotId getIdTo() {
        return toId;
    }

    @Override
    public ILocation getUpperBound() {
        return PlotMeCoreManager.getInstance().getPlotTopLoc(fromId);
    }

    @Override
    public ILocation getLowerBound() {
        return PlotMeCoreManager.getInstance().getPlotBottomLoc(fromId);
    }

    public ILocation getUpperBoundTo() {
        return PlotMeCoreManager.getInstance().getPlotTopLoc(toId);
    }

    public ILocation getLowerBoundTo() {
        return PlotMeCoreManager.getInstance().getPlotBottomLoc(toId);
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
