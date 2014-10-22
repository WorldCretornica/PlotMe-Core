package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotRunnableDeleteExpire;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;

public class CmdResetExpired extends PlotCommand {

    public CmdResetExpired(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(ICommandSender sender, String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandResetExpired") + " <" + C("WordWorld") + "> " + RESET + "Example: " + RED + "/plotme " + C("CommandResetExpired") + " plotworld ");
        } else if (plugin.getWorldCurrentlyProcessingExpired() != null) {
            sender.sendMessage(plugin.getCommandSenderCurrentlyProcessingExpired().getName() + " " + C("MsgAlreadyProcessingPlots"));
        } else {
            IWorld world = sob.getWorld(args[1]);

            if (world == null) {
                sender.sendMessage(RED + C("WordWorld") + " '" + args[1] + "' " + C("MsgDoesNotExistOrNotLoaded"));
                return true;
            } else if (!plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                sender.sendMessage(RED + C("MsgNotPlotWorld"));
                return true;
            } else {
                plugin.setWorldCurrentlyProcessingExpired(world);
                plugin.setCommandSenderCurrentlyProcessingExpired(sender);
                plugin.setCounterExpired(50);
                plugin.setNbPerDeletionProcessingExpired(5);

                plugin.scheduleTask(new PlotRunnableDeleteExpire(plugin), 5, 50);
            }
        }
        return true;
    }
}
