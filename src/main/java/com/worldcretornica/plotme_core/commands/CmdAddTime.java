package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;

public class CmdAddTime extends PlotCommand {

    public CmdAddTime(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "addtime";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        if (args.length > 1) {
            throw new BadUsageException(getUsage());
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_ADDTIME)) {
            if (manager.isPlotWorld(player)) {
                PlotMapInfo pmi = manager.getMap(player);
                if (pmi.getDaysToExpiration() != 0) {
                    PlotId id = manager.getPlotId(player);
                    if (id == null) {
                        player.sendMessage(C("MsgNoPlotFound"));
                        return true;
                    }
                    if (!manager.isPlotAvailable(id, player.getWorld())) {
                        Plot plot = manager.getPlotById(id, player.getWorld());
                        if (plot != null) {
                            String name = player.getName();

                            plot.resetExpire(pmi.getDaysToExpiration());
                            plugin.getSqlManager().savePlot(plot);
                            player.sendMessage(C("MsgPlotExpirationReset"));

                            if (isAdvancedLogging()) {
                                serverBridge.getLogger().info(name + " reset expiration on plot " + id);
                            }
                        }
                    } else {
                        player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                        return true;
                    }
                } else {
                    return true;
                }
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
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
