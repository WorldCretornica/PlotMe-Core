package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;

import java.util.UUID;

public class CmdWEAnywhere extends PlotCommand implements CommandBase {

    public CmdWEAnywhere(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "weanywhere";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_WEANYWHERE) && plugin.getServerBridge().getPlotWorldEdit() != null) {
            String name = player.getName();
            UUID uuid = player.getUniqueId();
            boolean defaultWEAnywhere = plugin.getConfig().getBoolean("defaultWEAnywhere");
            boolean playerIgnoringWELimit = manager.isPlayerIgnoringWELimit(player);
            if (playerIgnoringWELimit && !defaultWEAnywhere || !playerIgnoringWELimit && defaultWEAnywhere) {
                manager.removePlayerIgnoringWELimit(uuid);
                plugin.getServerBridge().getPlotWorldEdit().setMask(player);
            } else {
                manager.addPlayerIgnoringWELimit(uuid);
                plugin.getServerBridge().getPlotWorldEdit().removeMask(player);
            }
            if (manager.isPlayerIgnoringWELimit(player)) {
                player.sendMessage(C("MsgWorldEditAnywhere"));
                if (isAdvancedLogging()) {
                    plugin.getLogger().info(name + "enabled WorldEdit Anywhere");
                }
            } else {
                player.sendMessage(C("MsgWorldEditInYourPlots"));
                if (isAdvancedLogging()) {
                    plugin.getLogger().info(name + "disabled WorldEdit Anywhere");
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return null;
    }

}
