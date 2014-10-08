package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.utils.Util;

public abstract class PlotCommand {

    protected PlotMe_Core plugin;

    protected final String RED;
    protected final String RESET;
    protected final String AQUA;
    protected final String GREEN;
    protected final String BLUE;
    protected final String ITALIC;
    protected final String LOG = "[Event] ";
    protected final IServerBridge sob;

    public PlotCommand(PlotMe_Core instance) {
        plugin = instance;
        sob = plugin.getServerBridge();
        RED = plugin.getServerBridge().getColor("RED");
        RESET = plugin.getServerBridge().getColor("RESET");
        AQUA = plugin.getServerBridge().getColor("AQUA");
        GREEN = plugin.getServerBridge().getColor("GREEN");
        BLUE = plugin.getServerBridge().getColor("BLUE");
        ITALIC = plugin.getServerBridge().getColor("ITALIC");
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
