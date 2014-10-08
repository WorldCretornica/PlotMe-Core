package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.utils.Util;

public abstract class PlotCommand {

    protected PlotMe_Core plugin;

    protected final String RED = plugin.getServerBridge().getColor("RED");
    protected final String RESET = plugin.getServerBridge().getColor("RESET");
    protected final String AQUA = plugin.getServerBridge().getColor("AQUA");
    protected final String GREEN = plugin.getServerBridge().getColor("GREEN");
    protected final String BLUE = plugin.getServerBridge().getColor("BLUE");
    protected final String ITALIC = plugin.getServerBridge().getColor("ITALIC");
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
        return plugin.getServerBridge().getConfig().getBoolean("AdvancedLogging");
    }
}
