package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

import java.util.UUID;

public class CmdWEAnywhere extends PlotCommand {

    public CmdWEAnywhere(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p) {
        if (plugin.cPerms(p, "PlotMe.admin.weanywhere")) {
            String name = p.getName();
            UUID uuid = p.getUniqueId();

            if (plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(uuid) && !sob.getConfig().getBoolean("defaultWEAnywhere")
                    || !plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(uuid) && sob.getConfig().getBoolean("defaultWEAnywhere")) {
                plugin.getPlotMeCoreManager().removePlayerIgnoringWELimit(uuid);
                if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                    sob.getPlotWorldEdit().setMask(p);
                }
            } else {
                plugin.getPlotMeCoreManager().addPlayerIgnoringWELimit(uuid);
                sob.getPlotWorldEdit().removeMask(p);
            }

            if (plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(uuid)) {
                p.sendMessage(C("MsgWorldEditAnywhere"));

                if (isAdvancedLogging()) {
                    plugin.getLogger().info(LOG + name + " enabled WorldEdit anywhere");
                }
            } else {
                p.sendMessage(C("MsgWorldEditInYourPlots"));

                if (isAdvancedLogging()) {
                    plugin.getLogger().info(LOG + name + " disabled WorldEdit anywhere");
                }
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
