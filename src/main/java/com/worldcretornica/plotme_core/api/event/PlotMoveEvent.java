package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotMoveEvent extends PlotEvent implements ICancellable, Event {

    private final PlotId fromId;
    private final PlotId toId;
    private final IPlayer mover;
    private boolean canceled;

    public PlotMoveEvent(IWorld world, PlotId fromId, PlotId toId, IPlayer mover) {
        super(null, world);
        this.fromId = fromId;
        this.toId = toId;
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

    public PlotId getId() {
        return fromId;
    }

    public PlotId getIdTo() {
        return toId;
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
