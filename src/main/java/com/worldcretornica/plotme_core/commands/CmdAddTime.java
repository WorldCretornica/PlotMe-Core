package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.CommandExBase;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;

public class CmdAddTime extends PlotCommand {

    public CmdAddTime(PlotMe_Core instance, CommandExBase commandExBase) {
        super(instance);
    }

    public String getName() {
        return "addtime";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(getUsage());
            return true;
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_ADDTIME)) {
            if (manager.isPlotWorld(player)) {
                PlotMapInfo pmi = manager.getMap(player);
                if (pmi.getDaysToExpiration() != 0) {
                    Plot plot = manager.getPlot(player);
                    if (plot == null) {
                        player.sendMessage(C("NoPlotFound"));
                        return true;
                    }
                    String name = player.getName();

                    plot.resetExpire(pmi.getDaysToExpiration());
                    plugin.getSqlManager().savePlot(plot);
                    player.sendMessage(C("PlotExpirationReset"));

                    if (isAdvancedLogging()) {
                        serverBridge.getLogger().info(name + " reset expiration on plot " + plot.getId().getID());
                    }
                } else {
                    return true;
                }
            } else {
                player.sendMessage(C("NotPlotWorld"));
                return true;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("CmdAddTimeUsage");
    }

}
