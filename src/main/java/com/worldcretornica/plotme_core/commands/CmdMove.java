package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotMoveEvent;

public class CmdMove extends PlotCommand {

    public CmdMove(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission(PermissionNames.ADMIN_MOVE)) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                player.sendMessage("§c" + C("MsgNotPlotWorld"));
            } else if (args.length < 3 || args[1].isEmpty() || args[2].isEmpty()) {
                player.sendMessage(C("WordUsage") + ": §c/plotme move <" + C("WordIdFrom") + "> <" + C("WordIdTo") + "> §r" + C("WordExample")
                                   + ": §c/plotme move 0;1 2;-1");
            } else {
                String plot1 = args[1];
                String plot2 = args[2];
                IWorld world = player.getWorld();

                if (!PlotMeCoreManager.isValidId(world, plot1) || !PlotMeCoreManager.isValidId(world, plot2)) {
                    player.sendMessage(C("WordUsage") + ": §c/plotme move <" + C("WordIdFrom") + "> <" + C("WordIdTo") + "> §r" + C("WordExample")
                                       + ": §c/plotme move 0;1 2;-1");
                } else {
                    InternalPlotMoveEvent event = serverBridge.getEventFactory().callPlotMoveEvent(plugin, world, plot1, plot2, player);
                    if (!event.isCancelled()) {
                        if (plugin.getPlotMeCoreManager().movePlot(world, plot1, plot2)) {
                            player.sendMessage(C("MsgPlotMovedSuccess"));

                            serverBridge.getLogger()
                                    .info(player.getName() + " " + C("MsgExchangedPlot") + " " + plot1 + " " + C("MsgAndPlot") + " " + plot2);
                        } else {
                            player.sendMessage("§c" + C("ErrMovingPlot"));
                        }
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