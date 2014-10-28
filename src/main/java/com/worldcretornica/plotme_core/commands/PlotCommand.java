package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.utils.Util;

public abstract class PlotCommand {

    protected PlotMe_Core plugin;

    protected final String LOG = "[Event] ";
    protected final IServerBridge sob;

    public PlotCommand(PlotMe_Core instance) {
        plugin = instance;
        sob = plugin.getServerBridge();
    }

    protected Util Util() {
        return plugin.getUtil();
    }

    protected String C(String caption) {
        return Util().C(caption);
    }
    
    protected boolean isAdvancedLogging() {
        return sob.getConfig().getBoolean("AdvancedLogging");
    }
}
