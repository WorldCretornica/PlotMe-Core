package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotBuyEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdBuy extends PlotCommand {

    public CmdBuy(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        IWorld world = player.getWorld();
        if (manager.isPlotWorld(world)) {
            if (manager.isEconomyEnabled(world)) {
                if (player.hasPermission(PermissionNames.USER_BUY) || player.hasPermission("PlotMe.admin.buy")) {
                    String id = manager.getPlotId(player);

                    if (id.isEmpty()) {
                        player.sendMessage("§c" + C("MsgNoPlotFound"));
                    } else if (!manager.isPlotAvailable(id, world)) {
                        Plot plot = manager.getPlotById(id, world);

                        if (plot.isForSale()) {
                            String buyer = player.getName();

                            if (plot.getOwnerId().equals(player.getUniqueId())) {
                                player.sendMessage("§c" + C("MsgCannotBuyOwnPlot"));
                            } else {
                                int plotLimit = getPlotLimit(player);

                                short plotsOwned = manager.getNbOwnedPlot(player.getUniqueId(), world.getName().toLowerCase());
                                
                                if (plotLimit != -1 && plotsOwned >= plotLimit) {
                                    player.sendMessage(C("MsgAlreadyReachedMaxPlots") + " ("
                                                       + plotsOwned + "/" + getPlotLimit(player) + "). "
                                                       + C("WordUse") + " §c/plotme home§r " + C("MsgToGetToIt"));
                                } else {

                                    double cost = plot.getCustomPrice();

                                    if (serverBridge.getBalance(player) < cost) {
                                        player.sendMessage("§c" + C("MsgNotEnoughBuy"));
                                    } else {

                                        InternalPlotBuyEvent
                                                event =
                                                serverBridge.getEventFactory().callPlotBuyEvent(plugin, world, plot, player, cost);

                                        if (!event.isCancelled()) {
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, cost);

                                            if (er.transactionSuccess()) {
                                                String oldOwner = plot.getOwner();
                                                IOfflinePlayer currentbidder = null;

                                                if (plot.getOwnerId() != null) {
                                                    currentbidder = serverBridge.getOfflinePlayer(plot.getOwnerId());
                                                }

                                                if (currentbidder != null) {
                                                    EconomyResponse er2 = serverBridge.depositPlayer(currentbidder, cost);

                                                    if (er2.transactionSuccess()) {
                                                        for (IPlayer onlinePlayers : serverBridge.getOnlinePlayers()) {
                                                            if (onlinePlayers.getName().equalsIgnoreCase(oldOwner)) {
                                                                onlinePlayers.sendMessage(C("WordPlot") + " " + id + " "
                                                                                          + C("MsgSoldTo") + " " + buyer + ". " + Util()
                                                                        .moneyFormat(cost, true));
                                                                break;
                                                            }
                                                        }
                                                    } else {
                                                        player.sendMessage("§c" + er2.errorMessage);
                                                        serverBridge.getLogger().warning(er2.errorMessage);
                                                    }
                                                }

                                                plot.setOwner(buyer);
                                                plot.setCustomPrice(0.0);
                                                plot.setForSale(false);

                                                plot.updateField("owner", buyer);
                                                plot.updateField("customprice", 0);
                                                plot.updateField("forsale", false);

                                                manager.adjustWall(world, id, true);
                                                manager.removeSellSign(world, id);
                                                manager.setOwnerSign(world, plot);

                                                player.sendMessage(C("MsgPlotBought") + " " + Util().moneyFormat(-cost, true));

                                                if (isAdvancedLogging()) {
                                                    plugin.getLogger()
                                                            .info(buyer + " " + C("MsgBoughtPlot") + " " + id + " " + C("WordFor") + " " + cost);
                                                }
                                            } else {
                                                player.sendMessage("§c" + er.errorMessage);
                                                serverBridge.getLogger().warning(er.errorMessage);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgPlotNotForSale"));
                        }
                    } else {
                        player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                    }
                } else {
                    player.sendMessage("§c" + C("MsgPermissionDenied"));
                    return false;
                }
            } else {
                player.sendMessage("§c" + C("MsgEconomyDisabledWorld"));
            }
        }
        return true;
    }
}
