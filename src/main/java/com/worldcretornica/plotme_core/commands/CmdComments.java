package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

public class CmdComments extends PlotCommand {

    public CmdComments(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission("PlotMe.use.comments")) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                player.sendMessage("§c" + C("MsgNotPlotWorld"));
            } else if (args.length < 2) {
                String id = plugin.getPlotMeCoreManager().getPlotId(player);

                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, player)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(player, id);

                    if (plot.getOwnerId().equals(player.getUniqueId()) || plot.isAllowed(player.getUniqueId()) || player.hasPermission("PlotMe.admin")) {
                        if (plot.getCommentsCount() == 0) {
                            player.sendMessage(C("MsgNoComments"));
                        } else {
                            player.sendMessage(C("MsgYouHave") + " §b" + plot.getCommentsCount() + "§r " + C("MsgComments"));

                            for (String[] comment : plot.getComments()) {
                                player.sendMessage("§b" + C("WordFrom") + " : §c" + comment[0]);
                                player.sendMessage("§o" + comment[1]);
                            }

                        }
                    } else {
                        player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedViewComments"));
                    }
                } else {
                    player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            }
        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
