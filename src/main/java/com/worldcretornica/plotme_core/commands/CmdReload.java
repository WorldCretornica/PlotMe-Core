package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.event.PlotMeEventFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdReload extends PlotCommand {

    public CmdReload(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(CommandSender s, String[] args) {
        if (!(s instanceof Player) || plugin.cPerms(s, "PlotMe.admin.reload")) {
            PlotMeEventFactory.callPlotReloadEvent();

            plugin.reload();
            s.sendMessage(C("MsgReloadedSuccess"));

            if (isAdvancedLogging()) {
                plugin.getLogger().info(LOG + s.getName() + " " + C("MsgReloadedConfigurations"));
            }
        } else {
            s.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
