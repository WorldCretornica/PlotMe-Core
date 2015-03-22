package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.util.List;

public class CmdExpired extends PlotCommand implements CommandBase {

    public CmdExpired(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "expired";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
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

                        player.sendMessage(plot.getId() + " -> " + plot.getOwner() + " @ " + plot.getExpiredDate());
                    }
                }
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
                return true;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return null;
    }
}