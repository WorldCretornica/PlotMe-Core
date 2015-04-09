package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.ICommandSender;

public class PlotToClear {

    private final PlotId plotId;
    private final ClearReason reason;
    private final ICommandSender requester;

    public PlotToClear(PlotId id, ClearReason reason, ICommandSender requester) {
        this.plotId = id;
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

}
