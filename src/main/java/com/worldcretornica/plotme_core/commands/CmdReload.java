package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;

@SuppressWarnings("SameReturnValue")
public class CmdReload extends PlotCommand {

    public CmdReload(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec() {
        sob.getEventFactory().callPlotReloadEvent();

        plugin.reload();
        sob.getLogger().info(C("MsgReloadedSuccess"));

        return true;
    }
}
