package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
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
        if (player.hasPermission("PlotMe.use.claim") || player.hasPermission("PlotMe.admin.claim.other")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                String id = PlotMeCoreManager.getPlotId(player);

                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgCannotClaimRoad"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, player)) {
                    player.sendMessage("§c" + C("MsgThisPlotOwned"));
                } else {
                    String playername = player.getName();
                    UUID uuid = player.getUniqueId();

                    if (args.length == 2) {
                        if (player.hasPermission("PlotMe.admin.claim.other")) {
                            playername = args[1];
                            uuid = null;
                        }
                    }

                    IWorld world = player.getWorld();
                    int plotlimit = getPlotLimit(player);

                    if (playername.equals(player.getName()) && plotlimit != -1 && plugin.getPlotMeCoreManager().getNbOwnedPlot(player, world) >= plotlimit) {
                        player.sendMessage("§c" + C("MsgAlreadyReachedMaxPlots") + " ("
                                                   + plugin.getPlotMeCoreManager().getNbOwnedPlot(player, world) + "/" + getPlotLimit(player) + "). " + C("WordUse") + " §c/plotme home§r " + C("MsgToGetToIt"));
                    } else {
                        PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);

                        double price = 0;

                        InternalPlotCreateEvent event;

                        if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                            price = pmi.getClaimPrice();
                            double balance = sob.getBalance(player);

                            if (balance >= price) {
                                event = sob.getEventFactory().callPlotCreatedEvent(plugin, world, id, player);

                                if (event.isCancelled()) {
                                    return true;
                                } else {
                                    EconomyResponse er = sob.withdrawPlayer(player, price);

                                    if (!er.transactionSuccess()) {
                                        player.sendMessage("§c" + er.errorMessage);
                                        warn(er.errorMessage);
                                        return true;
                                    }
                                }
                            } else {
                                player.sendMessage("§c" + C("MsgNotEnoughBuy") + " " + C("WordMissing") + " §r" + (price - balance) + "§c " + sob.getEconomy().currencyNamePlural());
                                return true;
                            }
                        } else {
                            event = sob.getEventFactory().callPlotCreatedEvent(plugin, world, id, player);
                        }

                        if (!event.isCancelled()) {
                            Plot plot = plugin.getPlotMeCoreManager().createPlot(world, id, playername, uuid);

                            //plugin.getPlotMeCoreManager().adjustLinkedPlots(id, world);
                            if (plot == null) {
                                player.sendMessage("§c" + C("ErrCreatingPlotAt") + " " + id);
                            } else {
                                if (playername.equalsIgnoreCase(player.getName())) {
                                    player.sendMessage(C("MsgThisPlotYours") + " " + C("WordUse") + " §c/plotme home§r " + C("MsgToGetToIt") + " " + Util().moneyFormat(-price));
                                } else {
                                    player.sendMessage(C("MsgThisPlotIsNow") + " " + playername + C("WordPossessive") + ". " + C("WordUse") + " §c/plotme home§r " + C("MsgToGetToIt") + " " + Util().moneyFormat(-price));
                                }

                                if (isAdvancedLogging()) {
                                    plugin.getLogger().info(LOG + playername + " " + C("MsgClaimedPlot") + " " + id + (price != 0 ? " " + C("WordFor") + " " + price : ""));
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
