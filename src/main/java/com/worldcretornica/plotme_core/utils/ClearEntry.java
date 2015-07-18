package com.worldcretornica.plotme_core.utils;

import com.worldcretornica.plotme_core.ClearReason;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.ICommandSender;

import java.util.ArrayDeque;

public class ClearEntry {

    private final Plot plot;
    private final ClearReason reason;
    private final ICommandSender sender;
    public ArrayDeque<ChunkEntry> chunkqueue = new ArrayDeque<>();

    public ClearEntry(Plot plot, ClearReason reason, ICommandSender sender) {

        this.plot = plot;
        this.reason = reason;
        this.sender = sender;
    }

    public Plot getPlot() {
        return plot;
    }

    public ClearReason getReason() {
        return reason;
    }

    public ICommandSender getSender() {
        return sender;
    }
}
