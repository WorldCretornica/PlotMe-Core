package com.worldcretornica.plotme_core.api.event;

import java.util.Map;

public class InternalPlotWorldCreateEvent implements ICancellable {

    private boolean canceled;
    private final String worldname;
    private final Map<String, String> parameters;

    public InternalPlotWorldCreateEvent(String worldname, Map<String, String> parameters) {
        this.worldname = worldname;
        this.parameters = parameters;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public String getWorldName() {
        return worldname;
    }


    public Map<String, String> getParameters() {
        return parameters;
    }

}
