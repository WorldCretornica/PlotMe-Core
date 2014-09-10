package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.utils.Util;
import org.bukkit.ChatColor;

public abstract class PlotCommand {

    protected PlotMe_Core plugin;

    protected final ChatColor RED = ChatColor.RED;
    protected final ChatColor RESET = ChatColor.RESET;
    protected final ChatColor AQUA = ChatColor.AQUA;
    protected final ChatColor GREEN = ChatColor.GREEN;
    protected final ChatColor ITALIC = ChatColor.ITALIC;
    protected final String LOG = "[Event] ";

    public PlotCommand(PlotMe_Core instance) {
        plugin = instance;
    }

    protected Util Util() {
        return plugin.getUtil();
    }

    protected String C(String caption) {
        return Util().C(caption);
    }
}
