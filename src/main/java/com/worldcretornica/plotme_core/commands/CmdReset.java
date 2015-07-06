package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.ClearReason;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.event.PlotResetEvent;

public class CmdReset extends PlotCommand {

    public CmdReset(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "reset";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(getUsage());
            return true;
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_RESET) || player.hasPermission(PermissionNames.USER_RESET)) {
            if (manager.isPlotWorld(player)) {
                Plot plot = manager.getPlot(player);
                if (plot == null) {
                    player.sendMessage(C("NoPlotFound"));
                } else if (plot.isProtected()) {
                    player.sendMessage(C("MsgPlotProtectedCannotReset"));
                } else if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_RESET)) {
                    PlotResetEvent event = new PlotResetEvent(plot, player);
                    plugin.getEventBus().post(event);

                    if (!event.isCancelled()) {
                        manager.clear(plot, player, ClearReason.Reset);
                        if (manager.deletePlot(plot)) {
                            sender.sendMessage("Plot Reset");
                        } else {
                            player.sendMessage("Plot was not reset? Something stopped this command.");
                        }
                        if (isAdvancedLogging()) {
                            serverBridge.getLogger().info(C("MsgResetPlot", player.getName(), plot.getId().getID()));
                        }
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + "(" + plot.getId().toString() + ") " + C("MsgNotYoursNotAllowedReset"));
                }
            } else {
                player.sendMessage(C("NotPlotWorld"));
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("CmdResetUsage");
    }
}
