package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotToClear {

    private final Plot plot;
    private final PlotId plotId;
    private final ClearReason reason;
    private final ICommandSender requester;
    private IWorld world;

    public PlotToClear(Plot plot, PlotId id, IWorld world, ClearReason reason, ICommandSender requester) {
        this.plot = plot;
        this.plotId = id;
        this.world = world;
        this.reason = reason;
        this.requester = requester;
    }

    public final PlotId getPlotId() {
        return plotId;
    }

    public final ClearReason getReason() {
        return reason;
    }

    public final ICommandSender getRequester() {
        return requester;
    }

    public IWorld getWorld() {
        return world;
    }

    public Plot getPlot() {
        return plot;
    }
}
