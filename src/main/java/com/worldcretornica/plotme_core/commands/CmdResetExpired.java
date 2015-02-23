package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotRunnableDeleteExpire;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;

public class CmdResetExpired extends PlotCommand {

    public CmdResetExpired(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(ICommandSender sender, String string) {
        if (plugin.getWorldCurrentlyProcessingExpired() != null) {
            serverBridge.getLogger().info(C("MsgAlreadyProcessingPlots"));
        } else {
            IWorld world = serverBridge.getWorld(string);

            if (!manager.isPlotWorld(world)) {
                serverBridge.getLogger().info("§c" + C("MsgNotPlotWorld"));
            } else {
                plugin.setWorldCurrentlyProcessingExpired(world);
                plugin.setCounterExpired(50);

                plugin.scheduleTask(new PlotRunnableDeleteExpire(plugin, sender));
            }
        }
        return true;
    }
}
