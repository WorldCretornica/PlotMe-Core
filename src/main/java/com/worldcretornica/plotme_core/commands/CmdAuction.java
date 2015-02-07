package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotAuctionEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdAuction extends PlotCommand {

    public CmdAuction(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        IWorld world = player.getWorld();
        PlotMapInfo pmi = manager.getMap(world);
        if (manager.isPlotWorld(world)) {
            if (manager.isEconomyEnabled(pmi)) {
                if (pmi.isCanPutOnSale()) {
                    if (player.hasPermission(PermissionNames.USE_AUCTION) || player.hasPermission(PermissionNames.ADMIN_AUCTION)) {
                        String id = manager.getPlotId(player);

                        if (id.isEmpty()) {
                            player.sendMessage("§c" + C("MsgNoPlotFound"));
                        } else if (!manager.isPlotAvailable(id, pmi)) {
                            Plot plot = manager.getPlotById(id, pmi);
                            if (plot.isForSale()) {
                                player.sendMessage(C("You cannot auction a plot that is for sale"));
                                return true;
                            }

                            String name = player.getName();

                            if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_AUCTION)) {

                                if (plot.isAuctioned()) {
                                    if (plot.getCurrentBidderId() != null) {
                                        if (player.hasPermission(PermissionNames.ADMIN_AUCTION)) {
                                            IOfflinePlayer currentBidder = serverBridge.getOfflinePlayer(plot.getCurrentBidderId());
                                            EconomyResponse er = serverBridge.depositPlayer(currentBidder, plot.getCurrentBid());

                                            if (er.transactionSuccess()) {
                                                for (IPlayer onlinePlayers : serverBridge.getOnlinePlayers()) {
                                                    if (onlinePlayers.getName().equalsIgnoreCase(plot.getCurrentBidder())) {
                                                        onlinePlayers.sendMessage(C("MsgAuctionCancelledOnPlot")
                                                                                  + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + ". "
                                                                                  + Util().moneyFormat(plot.getCurrentBid(), true));
                                                        break;
                                                    }
                                                }
                                            } else {
                                                player.sendMessage("§c" + er.errorMessage);
                                                serverBridge.getLogger().warning(er.errorMessage);
                                            }

                                            plot.setAuctioned(false);
                                            manager.adjustWall(world, id, true);
                                            manager.removeAuctionSign(world, id);
                                            plot.setCurrentBid(0.0);
                                            plot.setCurrentBidder(null);

                                            plot.updateField("currentbid", 0);
                                            plot.updateField("currentbidder", null);
                                            plot.updateField("currentbidderid", null);
                                            plot.updateField("auctionned", false);

                                            player.sendMessage(C("MsgAuctionCancelled"));

                                            if (isAdvancedLogging()) {
                                                serverBridge.getLogger().info(name + " " + C("MsgStoppedTheAuctionOnPlot") + " " + id);
                                            }
                                        } else {
                                            player.sendMessage("§c" + C("MsgPlotHasBidsAskAdmin"));
                                        }
                                    } else {
                                        plot.setAuctioned(false);
                                        manager.adjustWall(player);
                                        manager.removeAuctionSign(world, id);
                                        plot.setCurrentBid(0.0);
                                        plot.setCurrentBidder(null);

                                        plot.updateField("currentbid", 0);
                                        plot.updateField("currentbidder", null);
                                        plot.updateField("currentbidderid", null);
                                        plot.updateField("auctionned", false);

                                        player.sendMessage(C("MsgAuctionCancelled"));

                                        if (isAdvancedLogging()) {
                                            serverBridge.getLogger().info(name + " " + C("MsgStoppedTheAuctionOnPlot") + " " + id);
                                        }
                                    }
                                } else {
                                    double bid = 1.0;

                                    if (args.length == 2) {
                                        bid = Double.parseDouble(args[1]);
                                    }

                                    if (bid < 0.0) {
                                        player.sendMessage("§c" + C("MsgInvalidAmount"));
                                    } else {

                                        InternalPlotAuctionEvent
                                                event =
                                                serverBridge.getEventFactory().callPlotAuctionEvent(plugin, world, plot, player, bid);

                                        if (!event.isCancelled()) {
                                            plot.setCurrentBid(bid);
                                            plot.setAuctioned(true);
                                            manager.adjustWall(world, id, true);
                                            manager.setAuctionSign(world, plot);

                                            plot.updateField("currentbid", bid);
                                            plot.updateField("auctionned", true);

                                            player.sendMessage(C("MsgAuctionStarted"));

                                            if (isAdvancedLogging()) {
                                                serverBridge.getLogger()
                                                        .info(name + " " + C("MsgStartedAuctionOnPlot") + " " + id + " " + C("WordAt") + " " + bid);
                                            }
                                        }
                                    }
                                }
                            } else {
                                player.sendMessage("§c" + C("MsgDoNotOwnPlot"));
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                        }
                    } else {
                        player.sendMessage("§c" + C("MsgPermissionDenied"));
                        return false;
                    }
                } else {
                    player.sendMessage("§c" + C("MsgSellingPlotsIsDisabledWorld"));
                }
            } else {
                player.sendMessage("§c" + C("MsgEconomyDisabledWorld"));
            }
        }
        return true;
    }
}
