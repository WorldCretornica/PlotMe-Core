package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;

public class CmdReload extends PlotCommand {

    public CmdReload(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(ICommandSender s) {
        sob.getEventFactory().callPlotReloadEvent();

        plugin.reload();
        s.sendMessage(C("MsgReloadedSuccess"));

        if (isAdvancedLogging()) {
            plugin.getLogger().info(LOG + s.getName() + " " + C("MsgReloadedConfigurations"));
        }
        return true;
    }
}
