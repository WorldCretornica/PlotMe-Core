package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotMoveEvent extends InternalPlotEvent implements ICancellable {

    private final String fromId;
    private final String toId;
    private final IWorld world;
    private final IPlayer mover;
    private boolean canceled;

    public InternalPlotMoveEvent(PlotMe_Core instance, IWorld world, String fromId, String toId, IPlayer mover) {
        super(instance, null, world);
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
        return PlotMeCoreManager.getInstance().getPlotById(fromId, world);
    }

    public Plot getPlotTo() {
        return PlotMeCoreManager.getInstance().getPlotById(toId, world);
    }

    public IPlayer getPlayer() {
        return mover;
    }

    public String getId() {
        return fromId;
    }

    public String getIdTo() {
        return toId;
    }

    @Override
    public ILocation getUpperBound() {
        return PlotMeCoreManager.getInstance().getPlotTopLoc(world, fromId);
    }

    @Override
    public ILocation getLowerBound() {
        return PlotMeCoreManager.getInstance().getPlotBottomLoc(world, fromId);
    }

    public ILocation getUpperBoundTo() {
        return PlotMeCoreManager.getInstance().getPlotTopLoc(world, toId);
    }

    public ILocation getLowerBoundTo() {
        return PlotMeCoreManager.getInstance().getPlotBottomLoc(world, toId);
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
