package com.worldcretornica.plotme_core.commands;

import com.google.common.collect.Lists;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.CommandExBase;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;

import java.util.List;

public class CmdDoneList extends PlotCommand {

    public CmdDoneList(PlotMe_Core instance, CommandExBase commandExBase) {
        super(instance);
    }

    public String getName() {
        return "donelist";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        if (manager.isPlotWorld(player)) {
            if (player.hasPermission(PermissionNames.ADMIN_DONE) || player.hasPermission(PermissionNames.USER_DONE)) {

                int page = 1;

                if (args.length == 2) {
                    page = Integer.parseInt(args[1]);
                }

                List<List<Plot>> partition = Lists.partition(plugin.getSqlManager().getFinishedPlots(player.getWorld()), 10);

                if (partition.isEmpty()) {
                    player.sendMessage(C("NoFinishedPlots"));
                } else {
                    player.sendMessage(C("MsgFinishedPlotsPage", page, partition.size()));

                    for (Plot plot : partition.get(page)) {
                        player.sendMessage(plot.getId() + " -> " + plot.getOwner() + " @ " + plot.getFinishedDate());
                    }
                }
            } else {
                return false;
            }
        } else {
            player.sendMessage(C("NotPlotWorld"));
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme donelist";
    }
}
