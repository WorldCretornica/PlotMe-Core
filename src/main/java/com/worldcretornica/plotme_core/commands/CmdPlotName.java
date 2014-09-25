package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.entity.Player;

/**
 * Class containing the method allowing players to name a plot.
 */
public class CmdPlotName extends PlotCommand {
    private Plot plot;

    public CmdPlotName(PlotMe_Core plugin) {
        super(plugin);
    }

    //TODO: NAMING PLOTS.
    public boolean exec(Player player, String[] arg) {
        if (plugin.cPerms(player, "plotme.use.nameplot")) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                player.sendMessage(RED + C("MsgNotPlotWorld"));
            } else {
                String id = plugin.getPlotMeCoreManager().getPlotId(player.getLocation());
                if (id.isEmpty()) {
                    player.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, player)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(player, id);
                    if (plot.getOwner().equalsIgnoreCase(player.getName())){
                        String name = "";
                        for (int i = 1, argLength = arg.length; i < argLength; i++) {
                            name += arg[i] + " ";
                        }
                            name = name.trim();
                        if (name.length() > 32) {
                            player.sendMessage(RED + "Plot Name is too long.");
                        } else {
                            plot.setPlotName(arg[1]);
                            plot.updateField("plotname", name);
                            player.sendMessage("Your plot is now named : " + name);
                        }
                    } else{
                        player.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedName"));
                    }
                }
                // plugin.getPlotMeCoreManager().setBiome(w, id, biome);
            }
        }
        return false;
    }
}