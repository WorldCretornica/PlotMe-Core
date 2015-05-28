package com.worldcretornica.plotme_core.api.event;

/**
 * PlotMe Enable Event
 */
public class PlotMeEnabledEvent implements Event {

    private final String version;

    public PlotMeEnabledEvent(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
