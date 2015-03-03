package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.utils.MinecraftFontWidthCalculator;
import com.worldcretornica.plotme_core.utils.Util;

import java.util.List;

public class CmdExpired extends PlotCommand {

    public CmdExpired(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission(PermissionNames.ADMIN_EXPIRED)) {
            IWorld world = player.getWorld();
            if (manager.isPlotWorld(world)) {
                int page = 1;

                if (args.length == 2) {
                    page = Integer.parseInt(args[1]);
                }

                //int maxPage = (int) Math.ceil(plugin.getSqlManager().getExpiredPlotCount(world.getName()) / 8);
                //Temporary maxPage to allow for compile TODO: Remove this and fix this
                int maxPage = 8;

                List<Plot> expiredPlots = plugin.getSqlManager().getExpiredPlots(world.getName(), page, 8);

                if (expiredPlots.isEmpty()) {
                    player.sendMessage(C("MsgNoPlotExpired"));
                } else {
                    player.sendMessage(C("MsgExpiredPlotsPage") + " " + page + "/" + maxPage);

                    for (int i = (page - 1) * 8; i < expiredPlots.size() && i < page * 8; i++) {
                        Plot plot = expiredPlots.get(i);

                        String startText = "  §b" + plot.getId() + "§r -> " + plot.getOwner();

                        int textLength = MinecraftFontWidthCalculator.getStringWidth(startText);

                        String line = startText + Util.whitespace(550 - textLength) + "@" + plot.getExpiredDate();

                        player.sendMessage(line);
                    }
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