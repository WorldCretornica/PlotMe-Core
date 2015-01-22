package com.worldcretornica.plotme_core;

public class PlotToClear {

    private String world;
    private String plotId;
    private ClearReason reason;

    public PlotToClear(String world, String id, ClearReason reason) {
        setWorld(world);
        setPlotId(id);
        setReason(reason);
    }


    public final String getWorld() {
        return world;
    }

    public final void setWorld(String world) {
        this.world = world;
    }


    public final String getPlotId() {
        return plotId;
    }

    public final void setPlotId(String plotId) {
        this.plotId = plotId;
    }

    public final ClearReason getReason() {
        return reason;
    }

    public final void setReason(ClearReason reason) {
        this.reason = reason;
    }
}
