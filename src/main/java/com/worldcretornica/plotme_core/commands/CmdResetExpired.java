package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotRunnableDeleteExpire;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.World;

@SuppressWarnings("SameReturnValue")
public class CmdResetExpired extends PlotCommand {

    public CmdResetExpired(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(ICommandSender sender, String[] args) {
        if (args.length <= 1) {
            serverBridge.getLogger().info(C("WordUsage") + ": plotme resetexpired <" + C("WordWorld") + "> §rExample: §c/plotme resetexpired plotworld ");
        } else if (plugin.getWorldCurrentlyProcessingExpired() != null) {
            serverBridge.getLogger().info(C("MsgAlreadyProcessingPlots"));
        } else {
            World world = serverBridge.getWorld(args[1]);

            if (!plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                serverBridge.getLogger().info("§c" + C("MsgNotPlotWorld"));
            } else {
                plugin.setWorldCurrentlyProcessingExpired(world);
                plugin.setCounterExpired((short) 50);

                plugin.scheduleTask(new PlotRunnableDeleteExpire(plugin, sender));
            }
        }
        return true;
    }
}
