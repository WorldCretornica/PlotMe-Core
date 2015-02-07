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

            if (manager.isPlayerIgnoringWELimit(player) && !defaultWEAnywhere || !manager.isPlayerIgnoringWELimit(player) && defaultWEAnywhere) {
                manager.removePlayerIgnoringWELimit(uuid);
                plugin.getServerBridge().getPlotWorldEdit().setMask(player);
                player.sendMessage(C("MsgWorldEditInYourPlots"));
                if (isAdvancedLogging()) {
                    plugin.getLogger().info(name + "disabled WorldEdit Anywhere");
                }
            } else {
                manager.addPlayerIgnoringWELimit(uuid);
                plugin.getServerBridge().getPlotWorldEdit().removeMask(player);
                player.sendMessage(C("MsgWorldEditAnywhere"));
                if (isAdvancedLogging()) {
                    plugin.getLogger().info(name + "enabled WorldEdit Anywhere");
                }
            }

        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
