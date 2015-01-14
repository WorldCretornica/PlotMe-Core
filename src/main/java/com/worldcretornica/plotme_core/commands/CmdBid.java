package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.api.event.InternalPlotBidEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdBid extends PlotCommand {

    public CmdBid(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        World world = player.getWorld();
        if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
            if (player.hasPermission(PermissionNames.PLOT_ME_USE_BID)) {
                String id = PlotMeCoreManager.getPlotId(player);

                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, world)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(id, world);

                    if (plot.isAuctioned()) {
                        String bidder = player.getName();

                        if (plot.getOwner().equalsIgnoreCase(bidder)) {
                            player.sendMessage("§c" + C("MsgCannotBidOwnPlot"));
                        } else if (args.length == 2) {
                            double currentbid = plot.getCurrentBid();
                            String currentbidder = plot.getCurrentBidder();
                            IOfflinePlayer playercurrentbidder = serverBridge.getOfflinePlayer(plot.getCurrentBidderId());

                            double bid = Double.parseDouble(args[1]);

                            if (bid == currentbid) {
                                if (currentbidder != null) {
                                    player.sendMessage(
                                            "§c" + C("MsgInvalidBidMustBeAbove") + " §r" + Util().moneyFormat(plot.getCurrentBid(), false));
                                }
                            } else {
                                double balance = serverBridge.getBalance(player);

                                if (bid >= balance) {
                                    if (!currentbidder.equals(bidder) || bid > balance + currentbid) {
                                        player.sendMessage("§c" + C("MsgNotEnoughBid"));
                                    } else {
                                        InternalPlotBidEvent
                                                event =
                                                serverBridge.getEventFactory().callPlotBidEvent(plugin, player.getWorld(), plot, player, bid);

                                        if (!event.isCancelled()) {
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, bid);

                                            if (er.transactionSuccess()) {
                                                if (playercurrentbidder != null) {
                                                    EconomyResponse er2 = serverBridge.depositPlayer(playercurrentbidder, currentbid);

                                                    if (er2.transactionSuccess()) {
                                                        for (IPlayer onlinePlayers : serverBridge.getOnlinePlayers()) {
                                                            if (onlinePlayers.getName().equalsIgnoreCase(currentbidder)) {
                                                                onlinePlayers.sendMessage(
                                                                        C("MsgOutbidOnPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot
                                                                                .getOwner() + ". " + Util().moneyFormat(bid, true));
                                                                break;
                                                            }
                                                        }
                                                    } else {
                                                        player.sendMessage(er2.errorMessage);
                                                        serverBridge.getLogger().warning(er2.errorMessage);
                                                    }
                                                }

                                                plot.setCurrentBidder(bidder);
                                                plot.setCurrentBid(bid);

                                                plot.updateField("currentbidder", bidder);
                                                plot.updateField("currentbid", bid);

                                                plugin.getPlotMeCoreManager().setSellSign(player.getWorld(), plot);

                                                double price = -bid;
                                                player.sendMessage(C("MsgBidAccepted") + " " + Util().moneyFormat(price, true));

                                                if (isAdvancedLogging()) {
                                                    serverBridge.getLogger().info(bidder + " bid " + bid + " on plot " + id);
                                                }
                                            } else {
                                                player.sendMessage(er.errorMessage);
                                                serverBridge.getLogger().warning(er.errorMessage);
                                            }
                                        }
                                    }
                                } else if (currentbidder.equals(bidder) && bid > balance + currentbid) {
                                    player.sendMessage("§c" + C("MsgNotEnoughBid"));
                                } else {
                                    InternalPlotBidEvent
                                            event =
                                            serverBridge.getEventFactory().callPlotBidEvent(plugin, player.getWorld(), plot, player, bid);

                                    if (!event.isCancelled()) {
                                        EconomyResponse er = serverBridge.withdrawPlayer(player, bid);

                                        if (er.transactionSuccess()) {
                                            if (playercurrentbidder != null) {
                                                EconomyResponse er2 = serverBridge.depositPlayer(playercurrentbidder, currentbid);

                                                if (er2.transactionSuccess()) {
                                                    for (IPlayer onlinePlayers : serverBridge.getOnlinePlayers()) {
                                                        if (onlinePlayers.getName().equalsIgnoreCase(currentbidder)) {
                                                            onlinePlayers.sendMessage(
                                                                    C("MsgOutbidOnPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner()
                                                                    + ". " + Util().moneyFormat(bid, true));
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    player.sendMessage(er2.errorMessage);
                                                    serverBridge.getLogger().warning(er2.errorMessage);
                                                }
                                            }

                                            plot.setCurrentBidder(bidder);
                                            plot.setCurrentBid(bid);

                                            plot.updateField("currentbidder", bidder);
                                            plot.updateField("currentbid", bid);

                                            plugin.getPlotMeCoreManager().setSellSign(player.getWorld(), plot);

                                            double price = -bid;
                                            player.sendMessage(C("MsgBidAccepted") + " " + Util().moneyFormat(price, true));

                                            if (isAdvancedLogging()) {
                                                serverBridge.getLogger().info(bidder + " bid " + bid + " on plot " + id);
                                            }
                                        } else {
                                            player.sendMessage(er.errorMessage);
                                            serverBridge.getLogger().warning(er.errorMessage);
                                        }
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(
                                    C("WordUsage") + ": §c/plotme bid <" + C("WordAmount") + "> §r" + C("WordExample") + ": §c/plotme bid 100");
                        }
                    } else {
                        player.sendMessage("§c" + C("MsgPlotNotAuctionned"));
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
        return true;
    }
}
