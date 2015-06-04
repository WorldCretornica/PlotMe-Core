package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.util.TreeSet;

public class CmdExpired extends PlotCommand {

    public CmdExpired(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "expired";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_EXPIRED)) {
            IWorld world = player.getWorld();
            if (manager.isPlotWorld(world)) {
                PlotMapInfo pmi = manager.getMap(player);
                if (pmi.getDaysToExpiration() != 0) {
                    int page = 1;

                    if (args.length == 2) {
                        page = Integer.parseInt(args[1]);
                    }

                    TreeSet<Plot> expiredPlots = plugin.getSqlManager().getExpiredPlots(world);

                    if (expiredPlots.isEmpty()) {
                        player.sendMessage(C("MsgNoPlotExpired"));
                    } else {
                        player.sendMessage(C("MsgExpiredPlotsPage") + " " + page + "/" + expiredPlots.size() / 5);

                        for (int i = (page - 1) * 5; i < expiredPlots.size() && i < page * 5; i++) {
                            //Plot plot = expiredPlots;
                            //player.sendMessage(plot.getId() + " -> " + plot.getOwner() + " @ " + plot.getExpiredDate().toString());
                        }
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
        return C("WordUsage") + ": /plotme expired";
    }
}