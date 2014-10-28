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

    public boolean exec(IPlayer p, String[] args) {
        if (p.hasPermission("PlotMe.admin.done")) {
            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(p);

            if (pmi == null) {
                p.sendMessage("§c" + C("MsgNotPlotWorld"));
                return true;
            } else {
                int page = 1;

                if (args.length == 2) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ex) {
                    }
                }

                int pagesize = 8;
                int maxpage = (int) Math.ceil((double) plugin.getSqlManager().getFinishedPlotCount(p.getWorld().getName()) / (double) pagesize);

                if (page < 0) {
                    page = 1;
                } else if (page > maxpage) {
                    page = maxpage;
                }

                List<Plot> finishedplots = plugin.getSqlManager().getDonePlots(p.getWorld().getName(), page, pagesize);

                if (finishedplots.isEmpty()) {
                    p.sendMessage(C("MsgNoPlotsFinished"));
                } else {
                    p.sendMessage(C("MsgFinishedPlotsPage") + " " + page + "/" + maxpage);

                    for (int i = (page - 1) * pagesize; i < finishedplots.size() && i < page * pagesize; i++) {
                        Plot plot = finishedplots.get(i);

                        String starttext = "  §b" + plot.getId() + "§r -> " + plot.getOwner();

                        int textLength = MinecraftFontWidthCalculator.getStringWidth(starttext);

                        String line = starttext + whitespace(550 - textLength) + "@" + plot.getFinishedDate();

                        p.sendMessage(line);
                    }
                }
            }
        } else {
            p.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
