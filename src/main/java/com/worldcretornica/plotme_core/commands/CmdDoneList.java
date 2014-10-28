package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.utils.MinecraftFontWidthCalculator;

import java.util.List;

import static com.worldcretornica.plotme_core.utils.Util.whitespace;

public class CmdDoneList extends PlotCommand {

    public CmdDoneList(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission("PlotMe.admin.done")) {
            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(player);

            if (pmi == null) {
                player.sendMessage("§c" + C("MsgNotPlotWorld"));
                return true;
            } else {
                int page = 1;

                if (args.length == 2) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ex) {
                    }
                }

                int maxpage = (int) Math.ceil(plugin.getSqlManager().getFinishedPlotCount(player.getWorld().getName()) / 8);

                if (page < 0) {
                    page = 1;
                } else if (page > maxpage) {
                    page = maxpage;
                }

                List<Plot> finishedplots = plugin.getSqlManager().getDonePlots(player.getWorld().getName(), page, 8);

                if (finishedplots.isEmpty()) {
                    player.sendMessage(C("MsgNoPlotsFinished"));
                } else {
                    player.sendMessage(C("MsgFinishedPlotsPage") + " " + page + "/" + maxpage);

                    for (int i = (page - 1) * 8; i < finishedplots.size() && i < page * 8; i++) {
                        Plot plot = finishedplots.get(i);

                        String starttext = "  §b" + plot.getId() + "§r -> " + plot.getOwner();

                        int textLength = MinecraftFontWidthCalculator.getStringWidth(starttext);

                        String line = starttext + whitespace(550 - textLength) + "@" + plot.getFinishedDate();

                        player.sendMessage(line);
                    }
                }
            }
        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
