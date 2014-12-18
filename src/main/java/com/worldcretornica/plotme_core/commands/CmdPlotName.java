package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

/**
 * Class containing the method allowing players to name a plot.
 */
public class CmdPlotName extends PlotCommand {
    private Plot plot;

    public CmdPlotName(PlotMe_Core plugin) {
        super(plugin);
    }

    public boolean exec(IPlayer player, String[] arg) {
        if (player.hasPermission("plotme.use.nameplot")) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                player.sendMessage(C("MsgNotPlotWorld"));
            } else {
                String id = PlotMeCoreManager.getPlotId(player.getLocation());
                if (id.isEmpty()) {
                    player.sendMessage(C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, player)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(id, player);
                    if (plot.getOwner().equalsIgnoreCase(player.getName())){
                        String name = "";
                        for (int i = 1, argLength = arg.length; i < argLength; i++) {
                            name += arg[i] + " ";
                        }
                        name = name.trim();
                        if (name.length() > 32) {
                            player.sendMessage("Plot Name is too long.");
                        } else {
                            plot.setPlotName(arg[1]);
                            plot.updateField("plotname", name);
                            PlotMeCoreManager.setOwnerSign(player.getWorld(), plot);
                            player.sendMessage("Your plot is now named : " + name);
                        }
                    } else{
                        player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedName"));
                    }
                }
            }
        }
        return false;
    }
}