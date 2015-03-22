package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotRunnableDeleteExpire;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;

public class CmdResetExpired extends PlotCommand implements CommandBase {

    public CmdResetExpired(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "resetexpired";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (plugin.getWorldCurrentlyProcessingExpired() != null) {
            serverBridge.getLogger().info(C("MsgAlreadyProcessingPlots"));
        } else {
            IWorld world = serverBridge.getWorld(args[1]);

            if (!manager.isPlotWorld(world)) {
                serverBridge.getLogger().info(C("MsgNotPlotWorld"));
            } else {
                plugin.setWorldCurrentlyProcessingExpired(world);
                plugin.setCounterExpired(50);

                plugin.scheduleTask(new PlotRunnableDeleteExpire(plugin, sender));
            }
        }
        return true;
    }

    @Override
    public String getUsage() {
        return null;
    }
}
