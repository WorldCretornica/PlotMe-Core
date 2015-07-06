package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;

public class CmdWEAnywhere extends PlotCommand {

    public CmdWEAnywhere(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "weanywhere";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(getUsage());
            return true;
        }

        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_WEANYWHERE)) {
            boolean defaultWEAnywhere = plugin.getConfig().getBoolean("defaultWEAnywhere");
            boolean playerIgnoringWELimit = manager.isPlayerIgnoringWELimit(player);
            if (playerIgnoringWELimit && !defaultWEAnywhere || !playerIgnoringWELimit && defaultWEAnywhere) {
                manager.removePlayerIgnoringWELimit(player.getUniqueId());
            } else {
                manager.addPlayerIgnoringWELimit(player.getUniqueId());
            }
            if (manager.isPlayerIgnoringWELimit(player)) {
                player.sendMessage(C("WorldEditAnywhere"));
                if (isAdvancedLogging()) {
                    plugin.getLogger().info(player.getName() + "enabled WorldEdit Anywhere");
                }
            } else {
                player.sendMessage("You can now worldedit in only your plots.");
                if (isAdvancedLogging()) {
                    plugin.getLogger().info(player.getName() + "disabled WorldEdit Anywhere");
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("CmdWEAnywhereUsage");
    }

}
