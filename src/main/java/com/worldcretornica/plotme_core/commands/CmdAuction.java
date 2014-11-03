package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
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
        if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
            if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);

                if (pmi.isCanPutOnSale()) {
                    if (player.hasPermission("PlotMe.use.auction") || player.hasPermission("PlotMe.admin.auction")) {
                        String id = PlotMeCoreManager.getPlotId(player);

                        if (id.isEmpty()) {
                            player.sendMessage("§c" + C("MsgNoPlotFound"));
                        } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, world)) {
                            Plot plot = plugin.getPlotMeCoreManager().getPlotById(world, id);

                            String name = player.getName();

                            if (plot.getOwner().equalsIgnoreCase(name) || player.hasPermission("PlotMe.admin.auction")) {

                                if (plot.isAuctioned()) {
                                    if (plot.getCurrentBidderId() != null) {
                                        if (player.hasPermission("PlotMe.admin.auction")) {
                                            IOfflinePlayer playercurrentbidder = sob.getOfflinePlayer(plot.getCurrentBidderId());
                                            EconomyResponse er = sob.depositPlayer(playercurrentbidder, plot.getCurrentBid());

                                            if (er.transactionSuccess()) {
                                                for (IPlayer onlinePlayers : sob.getOnlinePlayers()) {
                                                    if (onlinePlayers.getName().equalsIgnoreCase(plot.getCurrentBidder())) {
                                                        onlinePlayers.sendMessage(C("MsgAuctionCancelledOnPlot")
                                                                                          + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + ". " + Util().moneyFormat(plot.getCurrentBid()));
                                                        break;
                                                    }
                                                }
                                            } else {
                                                player.sendMessage("§c" + er.errorMessage);
                                                warn(er.errorMessage);
                                            }

                                            plot.setAuctioned(false);
                                            plugin.getPlotMeCoreManager().adjustWall(player);
                                            plugin.getPlotMeCoreManager().setSellSign(world, plot);
                                            plot.setCurrentBid(0);
                                            plot.setCurrentBidder("");

                                            plot.updateField("currentbid", 0);
                                            plot.updateField("currentbidder", "");
                                            plot.updateField("currentbidderid", null);
                                            plot.updateField("auctionned", false);

                                            player.sendMessage(C("MsgAuctionCancelled"));

                                            if (isAdvancedLogging()) {
                                                plugin.getLogger().info(LOG + name + " " + C("MsgStoppedTheAuctionOnPlot") + " " + id);
                                            }
                                        } else {
                                            player.sendMessage("§c" + C("MsgPlotHasBidsAskAdmin"));
                                        }
                                    } else {
                                        plot.setAuctioned(false);
                                        plugin.getPlotMeCoreManager().adjustWall(player);
                                        plugin.getPlotMeCoreManager().setSellSign(world, plot);
                                        plot.setCurrentBid(0);
                                        plot.setCurrentBidder("");

                                        plot.updateField("currentbid", 0);
                                        plot.updateField("currentbidder", "");
                                        plot.updateField("currentbidderid", null);
                                        plot.updateField("auctionned", false);

                                        player.sendMessage(C("MsgAuctionCancelled"));

                                        if (isAdvancedLogging()) {
                                            plugin.getLogger().info(LOG + name + " " + C("MsgStoppedTheAuctionOnPlot") + " " + id);
                                        }
                                    }
                                } else {
                                    double bid = 1;

                                    if (args.length == 2) {
                                        try {
                                            bid = Double.parseDouble(args[1]);
                                        } catch (NumberFormatException e) {
                                        }
                                    }

                                    if (bid < 0) {
                                        player.sendMessage("§c" + C("MsgInvalidAmount"));
                                    } else {

                                        InternalPlotAuctionEvent event = sob.getEventFactory().callPlotAuctionEvent(plugin, world, plot, player, bid);

                                        if (!event.isCancelled()) {
                                            plot.setCurrentBid(bid);
                                            plot.setAuctioned(true);
                                            plugin.getPlotMeCoreManager().adjustWall(player);
                                            plugin.getPlotMeCoreManager().setSellSign(world, plot);

                                            plot.updateField("currentbid", bid);
                                            plot.updateField("auctionned", true);

                                            player.sendMessage(C("MsgAuctionStarted"));

                                            if (isAdvancedLogging()) {
                                                plugin.getLogger().info(LOG + name + " " + C("MsgStartedAuctionOnPlot") + " " + id + " " + C("WordAt") + " " + bid);
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
