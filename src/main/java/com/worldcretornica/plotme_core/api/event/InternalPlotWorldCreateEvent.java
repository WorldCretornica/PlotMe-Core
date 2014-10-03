package com.worldcretornica.plotme_core.api.event;

import java.util.Map;

import com.worldcretornica.plotme_core.api.ICommandSender;

public class InternalPlotWorldCreateEvent implements ICancellable {

    private boolean _canceled;
    private String _worldname;
    private ICommandSender _creator;
    private Map<String, String> _parameters;

    public InternalPlotWorldCreateEvent(String worldname, ICommandSender cs, Map<String, String> parameters) {
        _worldname = worldname;
        _creator = cs;
        _parameters = parameters;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancelled(boolean canceled) {
        _canceled = canceled;
    }

    public String getWorldName() {
        return _worldname;
    }

    public ICommandSender getCreator() {
        return _creator;
    }

    public Map<String, String> getParameters() {
        return _parameters;
    }

}
