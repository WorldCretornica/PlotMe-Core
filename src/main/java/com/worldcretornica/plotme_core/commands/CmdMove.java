package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotMoveEvent;

public class CmdMove extends PlotCommand {

    public CmdMove(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "move";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_MOVE)) {
            if (!manager.isPlotWorld(player)) {
                player.sendMessage(C("MsgNotPlotWorld"));
            } else if (args.length < 3 || args[1].isEmpty() || args[2].isEmpty()) {
                player.sendMessage(getUsage());
            } else {
                String plot1 = args[1];
                String plot2 = args[2];
                if (plot1.equals(plot2)) {
                    player.sendMessage("You can't do that!");
                    return true;
                }
                IWorld world = player.getWorld();

                if (!PlotId.isValidID(plot1) || !PlotId.isValidID(plot2)) {
                    player.sendMessage("Something you typed is wrong!");
                } else {
                    PlotId id1 = new PlotId(plot1);
                    final Plot plot_1 = manager.getPlotById(id1, world);
                    if (plot_1 != null) {
                        if (plot_1.isProtected()) {
                            player.sendMessage("You can't do that!");
                            return true;
                        }
                    }
                    PlotId id2 = new PlotId(plot2);
                    final Plot plot_2 = manager.getPlotById(id2, world);
                    if (plot_2 != null) {
                        if (plot_2.isProtected()) {
                            player.sendMessage("You can't do that!");
                            return true;
                        }
                    }
                    PlotMoveEvent event = new PlotMoveEvent(world, id1, id2, player);
                    plugin.getEventBus().post(event);
                    if (!event.isCancelled()) {
                        if (manager.movePlot(world, id1, id2)) {
                            player.sendMessage(C("MsgPlotMovedSuccess"));

                            serverBridge.getLogger()
                                    .info(player.getName() + " " + C("MsgExchangedPlot") + " " + plot1 + " " + C("MsgAndPlot") + " " + plot2);
                        } else {
                            player.sendMessage(C("ErrMovingPlot"));
                        }
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme move <" + C("WordIdFrom") + "> <" + C("WordIdTo") + "> ";
    }
}