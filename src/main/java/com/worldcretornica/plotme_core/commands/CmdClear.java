package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.ClearReason;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotClearEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdClear extends PlotCommand {

    public CmdClear(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission(PermissionNames.ADMIN_CLEAR) || player.hasPermission(PermissionNames.USER_CLEAR)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);
                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                } else if (!manager.isPlotAvailable(id, pmi)) {
                    Plot plot = manager.getPlotById(id, pmi);

                    if (plot.isProtect()) {
                        player.sendMessage(C("MsgPlotProtectedCannotClear"));
                    } else {
                        String playerName = player.getName();

                        if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_CLEAR)) {

                            double price = 0.0;

                            InternalPlotClearEvent event;

                            if (manager.isEconomyEnabled(pmi)) {
                                price = pmi.getClearPrice();
                                double balance = serverBridge.getBalance(player);

                                if (balance >= price) {
                                    event = serverBridge.getEventFactory().callPlotClearEvent(world, plot, player);

                                    if (event.isCancelled()) {
                                        return true;
                                    } else {
                                        EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                        if (!er.transactionSuccess()) {
                                            player.sendMessage(er.errorMessage);
                                            serverBridge.getLogger().warning(er.errorMessage);
                                            return true;
                                        }
                                    }
                                } else {
                                    player.sendMessage(
                                            C("MsgNotEnoughClear") + " " + C("WordMissing") + " " + (price - balance) + " " + serverBridge
                                                    .getEconomy().currencyNamePlural());
                                    return true;
                                }
                            } else {
                                event = serverBridge.getEventFactory().callPlotClearEvent(world, plot, player);
                            }

                            if (!event.isCancelled()) {
                                manager.clear(world, plot, player, ClearReason.Clear);

                                if (isAdvancedLogging()) {
                                    if (price == 0) {
                                        serverBridge.getLogger().info(playerName + " " + C("MsgClearedPlot") + " " + id);
                                    } else {
                                        serverBridge.getLogger()
                                                .info(playerName + " " + C("MsgClearedPlot") + " " + id + (" " + C("WordFor") + " " + price));
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedClear"));
                        }
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
            }
        } else {
            player.sendMessage(C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
