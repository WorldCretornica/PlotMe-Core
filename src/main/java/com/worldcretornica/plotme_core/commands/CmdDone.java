package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.event.InternalPlotDoneChangeEvent;

public class CmdDone extends PlotCommand {

    public CmdDone(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.use.done") || plugin.cPerms(p, "PlotMe.admin.done")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());

                if (id.isEmpty()) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);
                    String name = p.getName();

                    if (plot.getOwner().equalsIgnoreCase(name) || plugin.cPerms(p, "PlotMe.admin.done")) {
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
                    p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
                return true;
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
