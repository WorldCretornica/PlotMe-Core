package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.*;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.api.event.InternalPlotClearEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdClear extends PlotCommand {

    public CmdClear(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission(PermissionNames.ADMIN_CLEAR) || player.hasPermission(PermissionNames.USER_CLEAR)) {
            World world = player.getWorld();
            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);
            if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                String id = PlotMeCoreManager.getPlotId(player);
                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!PlotMeCoreManager.isPlotAvailable(id, pmi)) {
                    Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

                    if (plot.isProtect()) {
                        player.sendMessage("§c" + C("MsgPlotProtectedCannotClear"));
                    } else {
                        String playerName = player.getName();

                        if (plot.getOwner().equalsIgnoreCase(playerName) || player.hasPermission(PermissionNames.ADMIN_CLEAR)) {


                            double price = 0.0;

                            InternalPlotClearEvent event;

                            if (plugin.getPlotMeCoreManager().isEconomyEnabled(pmi)) {
                                price = pmi.getClearPrice();
                                double balance = serverBridge.getBalance(player);

                                if (balance >= price) {
                                    event = serverBridge.getEventFactory().callPlotClearEvent(plugin, world, plot, player);

                                    if (event.isCancelled()) {
                                        return true;
                                    } else {
                                        EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                        if (!er.transactionSuccess()) {
                                            player.sendMessage("§c" + er.errorMessage);
                                            serverBridge.getLogger().warning(er.errorMessage);
                                            return true;
                                        }
                                    }
                                } else {
                                    player.sendMessage("§c" + C("MsgNotEnoughClear") + " " + C("WordMissing") + " §r" + (price - balance) + "§c " + serverBridge.getEconomy().currencyNamePlural());
                                    return true;
                                }
                            } else {
                                event = serverBridge.getEventFactory().callPlotClearEvent(plugin, world, plot, player);
                            }

                            if (!event.isCancelled()) {
                                plugin.getPlotMeCoreManager().clear(world, plot, player, ClearReason.Clear);

                                if (isAdvancedLogging()) {
                                    if (price == 0)
                                        serverBridge.getLogger().info(playerName + " " + C("MsgClearedPlot") + " " + id);
                                    else
                                        serverBridge.getLogger().info(playerName + " " + C("MsgClearedPlot") + " " + id + (" " + C("WordFor") + " " + price));
                                }
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedClear"));
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
