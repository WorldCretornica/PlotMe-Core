package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

import java.util.List;

public class CmdDoneList extends PlotCommand {

    public CmdDoneList(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (manager.isPlotWorld(player)) {
            if (player.hasPermission(PermissionNames.ADMIN_DONE)) {

                int page = 1;

                if (args.length == 2) {
                    page = Integer.parseInt(args[1]);
                }

                //int maxPage = (int) Math.ceil(plugin.getSqlManager().getFinishedPlotCount(player.getWorld().getName()) / 8F);
                int maxPage = 8;
                if (page < 1) {
                    page = 1;
                } else if (page > maxPage) {
                    page = maxPage;
                }

                List<Plot> donePlots = null; //= plugin.getSqlManager().getDonePlots(player.getWorld().getName(), page, 8);

                if (donePlots.isEmpty()) {
                    player.sendMessage(C("MsgNoPlotsFinished"));
                } else {
                    player.sendMessage(C("MsgFinishedPlotsPage") + " " + page + "/" + maxPage);

                    for (Plot plot : donePlots) {

                        player.sendMessage(plot.getId() + " -> " + plot.getOwner() + " @ " + plot.getFinishedDate());
                    }
                }
            } else {
                player.sendMessage(C("MsgPermissionDenied"));
                return false;
            }
        } else {
            player.sendMessage(C("MsgNotPlotWorld"));
        }
        return true;
    }
}
