package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.ICommandSender;

public class PlotToClear {

    private final Plot plot;
    private final ClearReason reason;
    private final ICommandSender requester;

    public PlotToClear(Plot plot, ClearReason reason, ICommandSender requester) {
        this.plot = plot;
        this.reason = reason;
        this.requester = requester;
    }

    public final ClearReason getReason() {
        return reason;
    }

    public final ICommandSender getRequester() {
        return requester;
    }

    public Plot getPlot() {
        return plot;
    }
}
