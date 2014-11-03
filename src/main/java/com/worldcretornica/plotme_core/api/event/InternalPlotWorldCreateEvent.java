package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.api.ICommandSender;

import java.util.Map;

public class InternalPlotWorldCreateEvent implements ICancellable {

    private boolean _canceled;
    private String worldname;
    private ICommandSender _creator;
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
