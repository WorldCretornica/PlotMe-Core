package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotMoveEvent;

public class CmdMove extends PlotCommand {

    public CmdMove(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.move")) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                player.sendMessage(RED + C("MsgNotPlotWorld"));
            } else if (args.length < 3 || args[1].isEmpty() || args[2].isEmpty()) {
                player.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandMove") + " <" + C("WordIdFrom") + "> <" + C("WordIdTo") + "> "
                                      + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandMove") + " 0;1 2;-1");
            } else {
                String plot1 = args[1];
                String plot2 = args[2];
                IWorld w = player.getWorld();

                if (!plugin.getPlotMeCoreManager().isValidId(w, plot1) || !plugin.getPlotMeCoreManager().isValidId(w, plot2)) {
                    player.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandMove") + " <" + C("WordIdFrom") + "> <" + C("WordIdTo") + "> "
                                          + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandMove") + " 0;1 2;-1");
                    return true;
                } else {
                    InternalPlotMoveEvent event = sob.getEventFactory().callPlotMoveEvent(plugin, w, w, plot1, plot2, player);

                    if (!event.isCancelled()) {
                        if (plugin.getPlotMeCoreManager().movePlot(player.getWorld(), plot1, plot2)) {
                            player.sendMessage(C("MsgPlotMovedSuccess"));

                            plugin.getLogger().info(LOG + player.getName() + " " + C("MsgExchangedPlot") + " " + plot1 + " " + C("MsgAndPlot") + " " + plot2);
                        } else {
                            player.sendMessage(RED + C("ErrMovingPlot"));
                        }
                    }
                }
            }
        } else {
            player.sendMessage(RED + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
