package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotMoveEvent extends InternalPlotEvent implements ICancellable {

    private boolean canceled;
    private final String fromId;
    private final String toId;
    private final IWorld toworld;
    private final IPlayer mover;

    public InternalPlotMoveEvent(PlotMe_Core instance, IWorld fromworld, IWorld toworld, String fromId, String toId, IPlayer mover) {
        super(instance, null, fromworld);
        this.fromId = fromId;
        this.toId = toId;
        this.toworld = toworld;
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
        return plugin.getPlotMeCoreManager().getPlotById(fromId, world);
    }

    public Plot getPlotTo() {
        return plugin.getPlotMeCoreManager().getPlotById(toId, toworld);
    }

    public IWorld getWorldTo() {
        return toworld;
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
        return PlotMeCoreManager.getPlotTopLoc(world, fromId);
    }

    @Override
    public ILocation getLowerBound() {
        return PlotMeCoreManager.getPlotBottomLoc(world, fromId);
    }

    public ILocation getUpperBoundTo() {
        return PlotMeCoreManager.getPlotTopLoc(toworld, toId);
    }

    public ILocation getLowerBoundTo() {
        return PlotMeCoreManager.getPlotBottomLoc(toworld, toId);
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
