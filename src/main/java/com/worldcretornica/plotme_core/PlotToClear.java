package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotToClear {

    private final IWorld world;
    private final PlotId plotId;
    private final ClearReason reason;
    private final ICommandSender requester;

    public PlotToClear(IWorld world, PlotId id, ClearReason reason, ICommandSender requester) {
        this.world = world;
        this.plotId = id;
        this.reason = reason;
        this.requester = requester;
    }

    public final IWorld getWorld() {
        return world;
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

}
