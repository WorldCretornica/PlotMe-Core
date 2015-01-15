package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotRemoveDeniedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdUndeny extends PlotCommand {

    public CmdUndeny(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission(PermissionNames.ADMIN_UNDENY) || player.hasPermission(PermissionNames.USER_UNDENY)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);
            if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                String id = PlotMeCoreManager.getPlotId(player);
                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!PlotMeCoreManager.isPlotAvailable(id, pmi)) {
                    if (args.length < 2 || args[1].isEmpty()) {
                        player.sendMessage(C("WordUsage") + ": §c/plotme undeny <" + C("WordPlayer") + ">");
                    } else {
                        Plot plot = PlotMeCoreManager.getPlotById(id, pmi);
                        String playername = player.getName();
                        String denied = args[1];

                        if (plot.getOwner().equalsIgnoreCase(playername) || player.hasPermission(PermissionNames.ADMIN_UNDENY)) {
                            if (plot.isDeniedConsulting(denied) || plot.isGroupDenied(denied)) {

                                double price = 0.0;

                                InternalPlotRemoveDeniedEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(pmi)) {
                                    price = pmi.getUndenyPlayerPrice();
                                    double balance = serverBridge.getBalance(player);

                                    if (balance >= price) {
                                        event = serverBridge.getEventFactory().callPlotRemoveDeniedEvent(plugin, world, plot, player, denied);

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
                                        player.sendMessage("§c" + C("MsgNotEnoughUndeny") + " " + C("WordMissing") + " §r" + Util()
                                                .moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = serverBridge.getEventFactory().callPlotRemoveDeniedEvent(plugin, world, plot, player, denied);
                                }

                                if (!event.isCancelled()) {
                                    plot.removeDenied(denied);

                                    double price1 = -price;
                                    player.sendMessage(
                                            C("WordPlayer") + " §c" + denied + "§r " + C("MsgNowUndenied") + " " + Util().moneyFormat(price1, true));

                                    if (isAdvancedLogging()) {
                                        if (price != 0) {
                                            serverBridge.getLogger()
                                                    .info(playername + " " + C("MsgUndeniedPlayer") + " " + denied + " " + C("MsgFromPlot") + " " + id
                                                          + (" " + C("WordFor") + " " + price));
                                        } else {
                                            serverBridge.getLogger()
                                                    .info(playername + " " + C("MsgUndeniedPlayer") + " " + denied + " " + C("MsgFromPlot") + " "
                                                          + id);
                                        }
                                    }
                                }
                            } else {
                                player.sendMessage(C("WordPlayer") + " §c" + args[1] + "§r " + C("MsgWasNotDenied"));
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgThisPlot") + " (" + id + ") " + C("MsgNotYoursNotAllowedUndeny"));
                        }
                    }
                } else {
                    player.sendMessage("§c" + C("MsgThisPlot") + " (" + id + ") " + C("MsgHasNoOwner"));
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
