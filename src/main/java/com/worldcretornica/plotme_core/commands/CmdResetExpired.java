package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotRunnableDeleteExpire;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;

@SuppressWarnings("SameReturnValue")
public class CmdResetExpired extends PlotCommand {

    public CmdResetExpired(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(ICommandSender sender, String[] args) {
        if (args.length <= 1) {
            sob.getLogger().info(C("WordUsage") + ": §c/plotme resetexpired <" + C("WordWorld") + "> §rExample: §c/plotme resetexpired plotworld ");
        } else if (plugin.getWorldCurrentlyProcessingExpired() != null) {
            sob.getLogger().info(C("MsgAlreadyProcessingPlots"));
        } else {
            IWorld world = sob.getWorld(args[1]);

            if (world == null) {
                sob.getLogger().info("§c" + C("WordWorld") + " '" + args[1] + "' " + C("MsgDoesNotExistOrNotLoaded"));
                return true;
            } else if (!plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                sob.getLogger().info("§c" + C("MsgNotPlotWorld"));
                return true;
            } else {
                plugin.setWorldCurrentlyProcessingExpired(world);
                plugin.setCounterExpired(50);
                plugin.setNbPerDeletionProcessingExpired();

                plugin.scheduleTask(new PlotRunnableDeleteExpire(plugin, sender));
            }
        }
        return true;
    }
}
