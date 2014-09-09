package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.entity.Player;

public class CmdWEAnywhere extends PlotCommand {

    public CmdWEAnywhere(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.admin.weanywhere")) {
            String name = p.getName();

            if (plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getName()) && !plugin.getConfig().getBoolean("defaultWEAnywhere")
                    || !plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getName()) && plugin.getConfig().getBoolean("defaultWEAnywhere")) {
                plugin.getPlotMeCoreManager().removePlayerIgnoringWELimit(p.getName());
            } else {
                plugin.getPlotMeCoreManager().addPlayerIgnoringWELimit(p.getName());
            }

            if (plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getName())) {
                p.sendMessage(C("MsgWorldEditAnywhere"));

                if (true) {
                    plugin.getLogger().info(LOG + name + " enabled WorldEdit anywhere");
                }
            } else {
                p.sendMessage(C("MsgWorldEditInYourPlots"));

                if (true) {
                    plugin.getLogger().info(LOG + name + " disabled WorldEdit anywhere");
                }
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
