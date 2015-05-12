package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.event.PlotDoneChangeEvent;

public class CmdDone extends PlotCommand {

    public CmdDone(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "done";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.USER_DONE) || player.hasPermission(PermissionNames.ADMIN_DONE)) {
            if (manager.isPlotWorld(player)) {
                Plot plot = manager.getPlot(player);

                if (plot != null) {
                    String name = player.getName();

                    if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_DONE)) {
                        PlotDoneChangeEvent
                                event =
                                new PlotDoneChangeEvent(player.getWorld(), plot, player, plot.isFinished());
                        serverBridge.getEventBus().post(event);

                        if (!event.isCancelled()) {
                            if (plot.isFinished()) {
                                plot.setUnfinished();
                                player.sendMessage(C("MsgUnmarkFinished"));

                                if (isAdvancedLogging()) {
                                    serverBridge.getLogger().info(name + " " + C("WordMarked") + " " + plot.getId() + " " + C("WordFinished"));
                                }
                            } else {
                                plot.setFinished();
                                player.sendMessage(C("MsgMarkFinished"));

                                if (isAdvancedLogging()) {
                                    serverBridge.getLogger().info(name + " " + C("WordMarked") + " " + plot.getId() + " " + C("WordUnfinished"));
                                }
                            }
                        }
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + "(" + plot.getId() + ") " + C("MsgHasNoOwner"));
                }
            } else {

                player.sendMessage(C("MsgNotPlotWorld"));
                return true;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme done";
    }
}
