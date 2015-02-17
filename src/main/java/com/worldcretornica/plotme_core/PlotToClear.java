package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotToClear {

    private IWorld world;
    private PlotId plotId;
    private ClearReason reason;
    private ICommandSender requester;

    public PlotToClear(IWorld world, PlotId id, ClearReason reason, ICommandSender requester) {
        setWorld(world);
        setPlotId(id);
        setReason(reason);
        setRequester(requester);
    }

    public final IWorld getWorld() {
        return world;
    }

    private void setWorld(IWorld world) {
        this.world = world;
    }

    public final PlotId getPlotId() {
        return plotId;
    }

    public final void setPlotId(PlotId plotId) {
        this.plotId = plotId;
    }

    public final ClearReason getReason() {
        return reason;
    }

    public final void setReason(ClearReason reason) {
        this.reason = reason;
    }

    public final ICommandSender getRequester() {
        return requester;
    }

    private void setRequester(ICommandSender requester) {
        this.requester = requester;
    }
}
