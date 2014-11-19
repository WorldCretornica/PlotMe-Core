package com.worldcretornica.plotme_core.api.event;

import java.util.Map;

public class InternalPlotWorldCreateEvent implements ICancellable {

    private boolean _canceled;
    private final String worldname;
    private Map<String, String> parameters;

    public InternalPlotWorldCreateEvent(String worldname, Map<String, String> parameters) {
        this.worldname = worldname;
        this.parameters = parameters;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        _canceled = canceled;
    }

    public String getWorldName() {
        return worldname;
    }


    public Map<String, String> getParameters() {
        return parameters;
    }

}
