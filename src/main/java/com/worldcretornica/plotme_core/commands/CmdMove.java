package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotId;
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
            if (!manager.isPlotWorld(player)) {
                player.sendMessage("§c" + C("MsgNotPlotWorld"));
            } else if (args.length < 3 || args[1].isEmpty() || args[2].isEmpty()) {
                player.sendMessage(C("WordUsage") + ": §c/plotme move <" + C("WordIdFrom") + "> <" + C("WordIdTo") + "> §r" + C("WordExample")
                        + ": §c/plotme move 0;1 2;-1");
            } else {
                String plot1 = args[1];
                String plot2 = args[2];
                if (plot1.equals(plot2)) {
                    player.sendMessage(C("WordUsage") + ": §c/plotme move <" + C("WordIdFrom") + "> <" + C("WordIdTo") + "> §r" + C("WordExample")
                            + ": §c/plotme move 0;1 2;-1");
                    return true;
                }
                IWorld world = player.getWorld();

                if (!PlotId.isValidID(plot1) || !PlotId.isValidID(plot2)) {
                    player.sendMessage(C("WordUsage") + ": §c/plotme move <" + C("WordIdFrom") + "> <" + C("WordIdTo") + "> §r" + C("WordExample")
                            + ": §c/plotme move 0;1 2;-1");
                } else {
                    PlotId id1 = new PlotId(plot1);
                    PlotId id2 = new PlotId(plot2);
                    InternalPlotMoveEvent event = serverBridge.getEventFactory().callPlotMoveEvent(world, id1, id2, player);
                    if (!event.isCancelled()) {
                        if (manager.movePlot(world, id1, id2)) {
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