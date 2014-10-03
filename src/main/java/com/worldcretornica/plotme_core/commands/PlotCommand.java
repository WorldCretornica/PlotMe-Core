package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IServerObjectBuilder;
import com.worldcretornica.plotme_core.utils.Util;

public abstract class PlotCommand {

    protected PlotMe_Core plugin;

    protected final String RED = plugin.getServerObjectBuilder().getColor("RED");
    protected final String RESET = plugin.getServerObjectBuilder().getColor("RESET");
    protected final String AQUA = plugin.getServerObjectBuilder().getColor("AQUA");
    protected final String GREEN = plugin.getServerObjectBuilder().getColor("GREEN");
    protected final String BLUE = plugin.getServerObjectBuilder().getColor("BLUE");
    protected final String ITALIC = plugin.getServerObjectBuilder().getColor("ITALIC");
    protected final String LOG = "[Event] ";
    protected final IServerObjectBuilder sob;

    public PlotCommand(PlotMe_Core instance) {
        plugin = instance;
        sob = plugin.getServerObjectBuilder();
    }

    protected Util Util() {
        return plugin.getUtil();
    }

    protected String C(String caption) {
        return Util().C(caption);
    }
    
    protected boolean isAdvancedLogging() {
        return plugin.getServerObjectBuilder().getConfig().getBoolean("AdvancedLogging");
    }
}
