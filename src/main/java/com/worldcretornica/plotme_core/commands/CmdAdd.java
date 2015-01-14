package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddAllowedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdAdd extends PlotCommand {

    public CmdAdd(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission(PermissionNames.ADMIN_ADD) || player.hasPermission(PermissionNames.USER_ADD)) {
            World world = player.getWorld();
            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);
            if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                String id = PlotMeCoreManager.getPlotId(player);
                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!PlotMeCoreManager.isPlotAvailable(id, pmi)) {
                    if (args.length < 2) {
                        player.sendMessage(C("WordUsage") + " §c/plotme add <" + C("WordPlayer") + ">");
                    } else {
                        Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

                        String allowed = args[1];

                        if (plot.getOwner().equalsIgnoreCase(player.getName()) || player.hasPermission(PermissionNames.ADMIN_ADD)) {
                            if (plot.isAllowedConsulting(allowed) || plot.isGroupAllowed(allowed)) {
                                player.sendMessage(C("WordPlayer") + " §c" + allowed + "§r " + C("MsgAlreadyAllowed"));
                            } else {

                                InternalPlotAddAllowedEvent event;
                                double advancedPrice = 0.0;
                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(pmi)) {
                                    double price = pmi.getAddPlayerPrice();
                                    advancedPrice = price;
                                    double balance = serverBridge.getBalance(player);

                                    if (balance >= price) {
                                        event = serverBridge.getEventFactory().callPlotAddAllowedEvent(plugin, world, plot, player, allowed);

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
                                        player.sendMessage("§c" + C("MsgNotEnoughAdd") + " " + C("WordMissing") + " §r" + Util()
                                                .moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = serverBridge.getEventFactory().callPlotAddAllowedEvent(plugin, world, plot, player, allowed);
                                }

                                if (!event.isCancelled()) {
                                    IPlayer allowed2 = plugin.getServerBridge().getPlayerExact(allowed);
                                    if (allowed2 != null) {
                                        plot.addAllowed(allowed, allowed2.getUniqueId());
                                        plot.removeDenied(allowed2.getUniqueId());
                                    } else {
                                        plot.addAllowed(allowed);
                                        plot.removeDenied(allowed);
                                    }
                                    player.sendMessage(C("WordPlayer") + " §c" + allowed + "§r " + C("MsgNowAllowed"));

                                    if (isAdvancedLogging()) {
                                        if (advancedPrice == 0) {
                                            serverBridge.getLogger()
                                                    .info(player.getName() + " " + C("MsgAddedPlayer") + " " + allowed + " " + C("MsgToPlot") + " "
                                                          + id);
                                        } else {
                                            serverBridge.getLogger()
                                                    .info(player.getName() + " " + C("MsgAddedPlayer") + " " + allowed + " " + C("MsgToPlot") + " "
                                                          + id + (" " + C("WordFor") + " " + advancedPrice));
                                        }
                                    }
                                }
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedAdd"));
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
