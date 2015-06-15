package com.worldcretornica.plotme_core.commands;

import com.google.common.collect.Lists;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.util.List;

public class CmdExpired extends PlotCommand {

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
                PlotMapInfo pmi = manager.getMap(player);
                if (pmi.getDaysToExpiration() != 0) {
                    int page = 1;

                    if (args.length == 2) {
                        page = Integer.parseInt(args[1]);
                    }

                    List<List<Plot>> partition = Lists.partition(plugin.getSqlManager().getExpiredPlots(world), 10);
                    if (partition.isEmpty()) {
                        player.sendMessage(C("MsgNoPlotExpired"));
                    } else {
                        player.sendMessage(C("MsgExpiredPlotsPage") + " (" + page + "/" + partition.size() + ") : ");
                        for (Plot plot : partition.get(page)) {
                            assert plot.getExpiredDate() != null;
                            player.sendMessage(plot.getId() + " -> " + plot.getOwner() + " @ " + plot.getExpiredDate().toString());
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