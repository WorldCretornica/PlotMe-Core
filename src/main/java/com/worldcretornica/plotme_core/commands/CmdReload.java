package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.CommandSender;

@SuppressWarnings("SameReturnValue")
public class CmdReload extends PlotCommand {

    public CmdReload(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(CommandSender sender) {

        if (sender.hasPermission("plotme.admin.reload")) {
            serverBridge.getEventFactory().callPlotReloadEvent();

            plugin.reload();
            serverBridge.getLogger().info(C("MsgReloadedSuccess"));

            return true;
        } else {
            return false;
        }
    }
}
