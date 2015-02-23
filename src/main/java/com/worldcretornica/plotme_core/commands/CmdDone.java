package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.event.InternalPlotDoneChangeEvent;

public class CmdDone extends PlotCommand {

    public CmdDone(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission(PermissionNames.USER_DONE) || player.hasPermission(PermissionNames.ADMIN_DONE)) {
            if (manager.isPlotWorld(player)) {
                PlotId id = manager.getPlotId(player);

                if (id == null) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!manager.isPlotAvailable(id, player)) {
                    Plot plot = manager.getPlotById(id, player);
                    String name = player.getName();

                    if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_DONE)) {
                        InternalPlotDoneChangeEvent
                                event =
                                serverBridge.getEventFactory().callPlotDoneEvent(player.getWorld(), plot, player, plot.isFinished());

                        if (!event.isCancelled()) {
                            if (plot.isFinished()) {
                                plot.setUnfinished();
                                player.sendMessage(C("MsgUnmarkFinished"));

                                if (isAdvancedLogging()) {
                                    serverBridge.getLogger().info(name + " " + C("WordMarked") + " " + id + " " + C("WordFinished"));
                                }
                            } else {
                                plot.setFinished();
                                player.sendMessage(C("MsgMarkFinished"));

                                if (isAdvancedLogging()) {
                                    serverBridge.getLogger().info(name + " " + C("WordMarked") + " " + id + " " + C("WordUnfinished"));
                                }
                            }
                        }
                    }
                } else {
                    player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {

                player.sendMessage("§c" + C("MsgNotPlotWorld"));
                return true;
            }
        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
