package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotRunnableDeleteExpire;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;

public class CmdResetExpired extends PlotCommand {

    public CmdResetExpired(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "resetexpired";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        if (plugin.getWorldCurrentlyProcessingExpired() != null) {
            serverBridge.getLogger().info(C("MsgAlreadyProcessingPlots"));
        } else if (!manager.isPlotWorld(player.getWorld())) {
            serverBridge.getLogger().info(C("MsgNotPlotWorld"));
        } else {
            plugin.setWorldCurrentlyProcessingExpired(player.getWorld());
            plugin.setCounterExpired(50);

            plugin.scheduleTask(new PlotRunnableDeleteExpire(plugin, sender));
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme resetexpired";
    }
}
