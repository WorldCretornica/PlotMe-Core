package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

import java.util.UUID;

public class CmdWEAnywhere extends PlotCommand {

    public CmdWEAnywhere(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        boolean defaultWEAnywhere = serverBridge.getConfig().getBoolean("defaultWEAnywhere");
        if (player.hasPermission(PermissionNames.ADMIN_WEANYWHERE) && plugin.getServerBridge().getPlotWorldEdit() != null) {
            String name = player.getName();
            UUID uuid = player.getUniqueId();

            if (plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(player)) {
                if (!defaultWEAnywhere) {
                    plugin.getPlotMeCoreManager().removePlayerIgnoringWELimit(uuid);
                    if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                        serverBridge.getPlotWorldEdit().setMask(player);
                    }
                } else {
                    plugin.getPlotMeCoreManager().addPlayerIgnoringWELimit(uuid);
                    serverBridge.getPlotWorldEdit().setMask(player);
                }

                player.sendMessage(C("MsgWorldEditInYourPlots"));

                if (isAdvancedLogging()) {
                    plugin.getLogger().info(name + " disabled WorldEdit anywhere");
                }
            } else {
                if (defaultWEAnywhere) {
                    plugin.getPlotMeCoreManager().removePlayerIgnoringWELimit(uuid);
                    serverBridge.getPlotWorldEdit().removeMask(player);
                } else {
                    plugin.getPlotMeCoreManager().addPlayerIgnoringWELimit(uuid);
                    if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                        serverBridge.getPlotWorldEdit().removeMask(player);
                    }
                }
                player.sendMessage(C("MsgWorldEditAnywhere"));

                if (isAdvancedLogging()) {
                    plugin.getLogger().info(name + " enabled WorldEdit anywhere");
                }
            }

        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
