package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;

import java.util.UUID;

public class CmdWEAnywhere extends PlotCommand {

    public CmdWEAnywhere(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "weanywhere";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_WEANYWHERE) && plugin.getServerBridge().isUsingWEdit()) {
            String name = player.getName();
            UUID uuid = player.getUniqueId();
            boolean defaultWEAnywhere = plugin.getConfig().getBoolean("defaultWEAnywhere");
            boolean playerIgnoringWELimit = manager.isPlayerIgnoringWELimit(player);
            if (playerIgnoringWELimit && !defaultWEAnywhere || !playerIgnoringWELimit && defaultWEAnywhere) {
                manager.removePlayerIgnoringWELimit(uuid);
            } else {
                manager.addPlayerIgnoringWELimit(uuid);
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
        return C("WordUsage") + ": /plotme weanywhere";
    }

}
