package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.event.InternalPlotDoneChangeEvent;

public class CmdDone extends PlotCommand {

    public CmdDone(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission("PlotMe.use.done") || player.hasPermission("PlotMe.admin.done")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                String id = PlotMeCoreManager.getPlotId(player);

                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, player)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(id, player);
                    String name = player.getName();

                    if (plot.getOwner().equalsIgnoreCase(name) || player.hasPermission("PlotMe.admin.done")) {
                        InternalPlotDoneChangeEvent event = sob.getEventFactory().callPlotDoneEvent(plugin, player.getWorld(), plot, player, plot.isFinished());

                        if (!event.isCancelled()) {
                            if (plot.isFinished()) {
                                plot.setUnfinished();
                                player.sendMessage(C("MsgUnmarkFinished"));

                                if (isAdvancedLogging()) {
                                    sob.getLogger().info(LOG + name + " " + C("WordMarked") + " " + id + " " + C("WordFinished"));
                                }
                            } else {
                                plot.setFinished();
                                player.sendMessage(C("MsgMarkFinished"));

                                if (isAdvancedLogging()) {
                                    sob.getLogger().info(LOG + name + " " + C("WordMarked") + " " + id + " " + C("WordUnfinished"));
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
