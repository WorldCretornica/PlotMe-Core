package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.utils.Util;

public abstract class PlotCommand {

    protected final PlotMe_Core plugin;

    protected final IServerBridge serverBridge;

    public PlotCommand(PlotMe_Core instance) {
        plugin = instance;
        serverBridge = plugin.serverBridge;
    }

    Util Util() {
        return plugin.getUtil();
    }

    void warn(String msg) {
        serverBridge.getLogger().warning(msg);
    }

    int getPlotLimit(IPlayer player) {

        if (player.hasPermission("plotme.limit.*")) {
            return -1;
        }
        int max = -2;
        for (int ctr = 0; ctr < 255; ctr++) {
            if (player.hasPermission("plotme.limit." + ctr)) {
                max = ctr;
            }
        }

        if (max == -2) {
            if (player.hasPermission("plotme.admin")) {
                return -1;
            } else if (player.hasPermission("plotme.use")) {
                return 1;
            } else {
                return 0;
            }
        }

        return max;
    }


    protected String C(String caption) {
        return Util().C(caption);
    }
    
    protected boolean isAdvancedLogging() {
        return serverBridge.getConfig().getBoolean("AdvancedLogging");
    }
}
