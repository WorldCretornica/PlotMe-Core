package com.worldcretornica.plotme_core;

import org.jetbrains.annotations.NotNull;

public class PlotToClear {

    private String world;
    private String plotid;
    private ClearReason reason;

    public PlotToClear(String world, String id, ClearReason reason) {
        setWorld(world);
        setPlotId(id);
        setReason(reason);
    }

    @NotNull
    public final String getWorld() {
        return world;
    }

    public final void setWorld(@NotNull String world) {
        this.world = world;
    }

    @NotNull
    public final String getPlotId() {
        return plotid;
    }

    public final void setPlotId(@NotNull String plotid) {
        this.plotid = plotid;
    }

    @NotNull
    public final ClearReason getReason() {
        return reason;
    }

    public final void setReason(ClearReason reason) {
        this.reason = reason;
    }
}
