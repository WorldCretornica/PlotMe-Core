package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.utils.MinecraftFontWidthCalculator;

import java.util.List;

public class CmdExpired extends PlotCommand {

    public CmdExpired(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.admin.expired")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                int page = 1;
                IWorld w = p.getWorld();

                if (args.length == 2) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ex) {
                    }
                }

                int maxpage = (int) Math.ceil((double) plugin.getSqlManager().getExpiredPlotCount(p.getWorld().getName()) / (double) 8);

                List<Plot> expiredplots = plugin.getSqlManager().getExpiredPlots(w.getName(), page, 8);

                if (expiredplots.isEmpty()) {
                    p.sendMessage(C("MsgNoPlotExpired"));
                } else {
                    p.sendMessage(C("MsgExpiredPlotsPage") + " " + page + "/" + maxpage);

                    for (int i = (page - 1) * 8; i < expiredplots.size() && i < page * 8; i++) {
                        Plot plot = expiredplots.get(i);

                        String starttext = "  " + AQUA + plot.getId() + RESET + " -> " + plot.getOwner();

                        int textLength = MinecraftFontWidthCalculator.getStringWidth(starttext);

                        String line = starttext + Util().whitespace(550 - textLength) + "@" + plot.getExpiredDate();

                        p.sendMessage(line);
                    }
                }
            } else {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
                return true;
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
