package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.ClearReason;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotResetEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdReset extends PlotCommand {

    public CmdReset(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "reset";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_RESET) || player.hasPermission("PlotMe.use.reset")) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);
                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                }
                Plot plot = manager.getPlotById(id, pmi);

                if (plot == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                } else if (plot.isProtected()) {
                    player.sendMessage(C("MsgPlotProtectedCannotReset"));
                } else if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_RESET)) {

                    InternalPlotResetEvent event = serverBridge.getEventFactory().callPlotResetEvent(world, plot, player);

                    if (!event.isCancelled()) {
                        manager.setBiome(world, id, "PLAINS");
                        manager.clear(world, plot, player, ClearReason.Reset);

                        if (manager.isEconomyEnabled(pmi) && pmi.isRefundClaimPriceOnReset()) {
                            IPlayer playerOwner = serverBridge.getPlayer(plot.getOwnerId());

                            EconomyResponse er = serverBridge.depositPlayer(playerOwner, pmi.getClaimPrice());

                            if (er.transactionSuccess()) {
                                playerOwner.sendMessage(
                                        C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C("MsgWasReset")
                                                + " " + plugin.moneyFormat(pmi.getClaimPrice(), true));
                            } else {
                                player.sendMessage(er.errorMessage);
                                serverBridge.getLogger().warning(er.errorMessage);
                                return true;
                            }
                        }

                        if (!manager.isPlotAvailable(id, pmi)) {
                            manager.removePlot(pmi, id);
                        }

                        manager.removeOwnerSign(world, id);
                        manager.removeSellSign(world, id);
                        plugin.getSqlManager().deletePlot(plot.getInternalID());

                        if (isAdvancedLogging()) {
                            serverBridge.getLogger().info(player.getName() + " " + C("MsgResetPlot") + " " + id);
                        }
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedReset"));
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
