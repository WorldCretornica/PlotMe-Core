package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

public class CmdAddTime extends PlotCommand {

    public CmdAddTime(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission("PlotMe.admin.addtime")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                String id = PlotMeCoreManager.getPlotId(player);

                if (id.isEmpty()) {
                    player.sendMessage("§c" + C(MSG_NO_PLOT_FOUND));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, player)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(id, player);
                    if (plot != null) {
                        String name = player.getName();

                        plot.resetExpire(plugin.getPlotMeCoreManager().getMap(player).getDaysToExpiration());
                        player.sendMessage(C("MsgPlotExpirationReset"));

                        if (isAdvancedLogging()) {
                            serverBridge.getLogger().info(name + " reset expiration on plot " + id);
                        }
                    }
                } else {
                    player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                player.sendMessage("§c" + C("MsgNotPlotWorld"));
            }
        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
