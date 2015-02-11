package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.ICommandSender;

public class PlotToClear {

    private String world;
    private PlotId plotId;
    private ClearReason reason;
    private ICommandSender requester;

    public PlotToClear(String world, PlotId id, ClearReason reason, ICommandSender requester) {
        setWorld(world);
        setPlotId(id);
        setReason(reason);
        setRequester(requester);
    }

    public final String getWorld() {
        return world;
    }

    public final void setWorld(String world) {
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

    public final void setRequester(ICommandSender requester) {
        this.requester = requester;
    }
}
