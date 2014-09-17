package com.worldcretornica.plotme_core.commands;

import java.util.UUID;

import com.worldcretornica.plotme_core.PlotMe_Core;

import org.bukkit.entity.Player;

public class CmdWEAnywhere extends PlotCommand {

    public CmdWEAnywhere(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.admin.weanywhere")) {
            String name = p.getName();
            UUID uuid = p.getUniqueId();

            if (plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(uuid) && !plugin.getConfig().getBoolean("defaultWEAnywhere")
                    || !plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(uuid) && plugin.getConfig().getBoolean("defaultWEAnywhere")) {
                plugin.getPlotMeCoreManager().removePlayerIgnoringWELimit(uuid);
            } else {
                plugin.getPlotMeCoreManager().addPlayerIgnoringWELimit(uuid);
            }

            if (plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(uuid)) {
                p.sendMessage(C("MsgWorldEditAnywhere"));

                if (isAdvancedLogging()) {
                    plugin.getLogger().info(LOG + name + " enabled WorldEdit anywhere");
                }
            } else {
                p.sendMessage(C("MsgWorldEditInYourPlots"));

                if (isAdvancedLogging()) {
                    plugin.getLogger().info(LOG + name + " disabled WorldEdit anywhere");
                }
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
