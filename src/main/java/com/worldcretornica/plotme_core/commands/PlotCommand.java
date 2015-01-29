package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.utils.Util;

public abstract class PlotCommand {

    protected final PlotMe_Core plugin;

    protected final IServerBridge serverBridge;
    
    protected final PlotMeCoreManager manager;

    public PlotCommand(PlotMe_Core instance) {
        plugin = instance;
        serverBridge = plugin.getServerBridge();
        manager = PlotMeCoreManager.getInstance();
    }

    Util Util() {
        return plugin.getUtil();
    }

    short getPlotLimit(IPlayer player) {

        if (player.hasPermission("plotme.limit.*")) {
            return -1;
        }
        short max = -2;
        for (short ctr = 0; ctr < 255; ctr++) {
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
