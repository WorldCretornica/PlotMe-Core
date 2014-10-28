package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

import java.util.UUID;

public class CmdWEAnywhere extends PlotCommand {

    public CmdWEAnywhere(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission("PlotMe.admin.weanywhere")) {
            String name = player.getName();
            UUID uuid = player.getUniqueId();

            if (plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(uuid) && !sob.getConfig().getBoolean("defaultWEAnywhere")
                    || !plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(uuid) && sob.getConfig().getBoolean("defaultWEAnywhere")) {
                plugin.getPlotMeCoreManager().removePlayerIgnoringWELimit(uuid);
                if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                    sob.getPlotWorldEdit().setMask(player);
                }
            } else {
                plugin.getPlotMeCoreManager().addPlayerIgnoringWELimit(uuid);
                sob.getPlotWorldEdit().removeMask(player);
            }

            if (plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(uuid)) {
                player.sendMessage(C("MsgWorldEditAnywhere"));

                if (isAdvancedLogging()) {
                    plugin.getLogger().info(LOG + name + " enabled WorldEdit anywhere");
                }
            } else {
                player.sendMessage(C("MsgWorldEditInYourPlots"));

                if (isAdvancedLogging()) {
                    plugin.getLogger().info(LOG + name + " disabled WorldEdit anywhere");
                }
            }
        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
