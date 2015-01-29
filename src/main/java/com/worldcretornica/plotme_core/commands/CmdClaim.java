package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotCreateEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.UUID;

public class CmdClaim extends PlotCommand {

    public CmdClaim(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission(PermissionNames.USER_CLAIM) || player.hasPermission(PermissionNames.ADMIN_CLAIM_OTHER)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                String id = manager.getPlotId(player);

                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgCannotClaimRoad"));
                } else if (!manager.isPlotAvailable(id, pmi)) {
                    player.sendMessage("§c" + C("MsgThisPlotOwned"));
                } else {
                    String playerName = player.getName();
                    UUID playerUniqueId = player.getUniqueId();

                    if (args.length == 2) {
                        if (player.hasPermission(PermissionNames.ADMIN_CLAIM_OTHER)) {
                            playerName = args[1];
                            playerUniqueId = null;
                        }
                    }

                    int plotLimit = getPlotLimit(player);

                    short plotsOwned = manager.getNbOwnedPlot(player.getUniqueId(), world.getName().toLowerCase());
                    
                    if (playerName.equals(player.getName()) && plotLimit != -1 && plotsOwned >= plotLimit) {
                        player.sendMessage("§c" + C("MsgAlreadyReachedMaxPlots") + " (" + plotsOwned + "/" + getPlotLimit(player)
                                           + "). " + C("WordUse") + " §c/plotme home§r " + C("MsgToGetToIt"));
                    } else {

                        double price = 0.0;

                        InternalPlotCreateEvent event;

                        if (manager.isEconomyEnabled(pmi)) {
                            price = pmi.getClaimPrice();
                            double balance = serverBridge.getBalance(player);

                            if (balance >= price) {
                                event = serverBridge.getEventFactory().callPlotCreatedEvent(plugin, world, id, player);

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
                                player.sendMessage(
                                        "§c" + C("MsgNotEnoughBuy") + " " + C("WordMissing") + " §r" + (price - balance) + "§c " + serverBridge
                                                .getEconomy().currencyNamePlural());
                                return true;
                            }
                        } else {
                            event = serverBridge.getEventFactory().callPlotCreatedEvent(plugin, world, id, player);
                        }

                        if (!event.isCancelled()) {
                            Plot plot = manager.createPlot(world, id, playerName, playerUniqueId, pmi);

                            //plugin.getPlotMeCoreManager().adjustLinkedPlots(id, world);
                            if (plot == null) {
                                player.sendMessage("§c" + C("ErrCreatingPlotAt") + " " + id);
                            } else {
                                if (playerName.equalsIgnoreCase(player.getName())) {
                                    double price1 = -price;
                                    player.sendMessage(
                                            C("MsgThisPlotYours") + " " + C("WordUse") + " §c/plotme home§r " + C("MsgToGetToIt") + " " + Util()
                                                    .moneyFormat(price1, true));
                                } else {
                                    double price1 = -price;
                                    player.sendMessage(C("MsgThisPlotIsNow") + " " + playerName + C("WordPossessive") + ". " + C("WordUse")
                                                       + " §c/plotme home§r " + C("MsgToGetToIt") + " " + Util().moneyFormat(price1, true));
                                }

                                if (isAdvancedLogging()) {
                                    if (price == 0) {
                                        serverBridge.getLogger().info(playerName + " " + C("MsgClaimedPlot") + " " + id);
                                    } else {
                                        serverBridge.getLogger()
                                                .info(playerName + " " + C("MsgClaimedPlot") + " " + id + (" " + C("WordFor") + " " + price));
                                    }
                                }
                            }
                        }
                    }
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
