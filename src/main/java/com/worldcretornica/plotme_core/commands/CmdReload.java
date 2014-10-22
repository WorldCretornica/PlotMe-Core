package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;

public class CmdReload extends PlotCommand {

    public CmdReload(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec() {
        sob.getEventFactory().callPlotReloadEvent();

        plugin.reload();
        plugin.getLogger().info(C("MsgReloadedSuccess"));

        return true;
    }
}
