package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.ClearReason;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotResetEvent;

public class CmdReset extends PlotCommand {

    public CmdReset(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "reset";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        if (args.length > 1) {
            throw new BadUsageException(getUsage());
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_RESET) || player.hasPermission("PlotMe.use.reset")) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                Plot plot = manager.getPlot(player);

                if (plot == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                } else if (plot.isProtected()) {
                    player.sendMessage(C("MsgPlotProtectedCannotReset"));
                } else if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_RESET)) {
                    PlotResetEvent event = new PlotResetEvent(world, plot, player);
                    plugin.getEventBus().post(event);

                    if (!event.isCancelled()) {
                        manager.clear(plot, world, player, ClearReason.Reset);
                        manager.deletePlot(world, plot);

                        if (isAdvancedLogging()) {
                            serverBridge.getLogger().info(player.getName() + " " + C("MsgResetPlot") + " " + plot.getId().toString());
                        }
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + "(" + plot.getId().toString() + ") " + C("MsgNotYoursNotAllowedReset"));
                }
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme reset";
    }
}
