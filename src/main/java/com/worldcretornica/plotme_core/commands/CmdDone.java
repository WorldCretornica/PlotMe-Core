package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.event.InternalPlotDoneChangeEvent;

public class CmdDone extends PlotCommand {

    public CmdDone(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p) {
        if (p.hasPermission("PlotMe.use.done") || p.hasPermission("PlotMe.admin.done")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());

                if (id.isEmpty()) {
                    p.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);
                    String name = p.getName();

                    if (plot.getOwner().equalsIgnoreCase(name) || p.hasPermission("PlotMe.admin.done")) {
                        InternalPlotDoneChangeEvent event = sob.getEventFactory().callPlotDoneEvent(plugin, p.getWorld(), plot, p, plot.isFinished());

                        if (!event.isCancelled()) {
                            if (plot.isFinished()) {
                                plot.setUnfinished();
                                p.sendMessage(C("MsgUnmarkFinished"));

                                if (isAdvancedLogging()) {
                                    plugin.getLogger().info(LOG + name + " " + C("WordMarked") + " " + id + " " + C("WordFinished"));
                                }
                            } else {
                                plot.setFinished();
                                p.sendMessage(C("MsgMarkFinished"));

                                if (isAdvancedLogging()) {
                                    plugin.getLogger().info(LOG + name + " " + C("WordMarked") + " " + id + " " + C("WordUnfinished"));
                                }
                            }
                        }
                    }
                } else {
                    p.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                p.sendMessage("§c" + C("MsgNotPlotWorld"));
                return true;
            }
        } else {
            p.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
