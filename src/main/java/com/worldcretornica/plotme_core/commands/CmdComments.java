package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

public class CmdComments extends PlotCommand {

    public CmdComments(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p, String[] args) {
        if (p.hasPermission("PlotMe.use.comments")) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                p.sendMessage("§c" + C("MsgNotPlotWorld"));
            } else if (args.length < 2) {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());

                if (id.isEmpty()) {
                    p.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                    if (plot.getOwnerId().equals(p.getUniqueId()) || plot.isAllowed(p.getUniqueId()) || p.hasPermission("PlotMe.admin")) {
                        if (plot.getCommentsCount() == 0) {
                            p.sendMessage(C("MsgNoComments"));
                        } else {
                            p.sendMessage(C("MsgYouHave") + " §b" + plot.getCommentsCount() + "§r " + C("MsgComments"));

                            for (String[] comment : plot.getComments()) {
                                p.sendMessage("§b" + C("WordFrom") + " : §c" + comment[0]);
                                p.sendMessage("§o" + comment[1]);
                            }

                        }
                    } else {
                        p.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedViewComments"));
                    }
                } else {
                    p.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            }
        } else {
            p.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
