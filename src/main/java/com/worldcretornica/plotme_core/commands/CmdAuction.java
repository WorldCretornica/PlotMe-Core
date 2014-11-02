package com.worldcretornica.plotme_core.commands;

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

    public boolean exec(IPlayer p, String[] args) {
        if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
            if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(p);

                if (pmi.isCanPutOnSale()) {
                    if (p.hasPermission("PlotMe.use.auction") || p.hasPermission("PlotMe.admin.auction")) {
                        String id = plugin.getPlotMeCoreManager().getPlotId(p);

                        if (id.isEmpty()) {
                            p.sendMessage("§c" + C("MsgNoPlotFound"));
                        } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                            Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                            String name = p.getName();

                            if (plot.getOwner().equalsIgnoreCase(name) || p.hasPermission("PlotMe.admin.auction")) {
                                IWorld world = p.getWorld();

                                if (plot.isAuctioned()) {
                                    if (plot.getCurrentBidderId() != null) {
                                        if (p.hasPermission("PlotMe.admin.auction")) {
                                            IOfflinePlayer playercurrentbidder = sob.getOfflinePlayer(plot.getCurrentBidderId());
                                            EconomyResponse er = sob.depositPlayer(playercurrentbidder, plot.getCurrentBid());

                                            if (er.transactionSuccess()) {
                                                for (IPlayer player : sob.getOnlinePlayers()) {
                                                    if (player.getName().equalsIgnoreCase(plot.getCurrentBidder())) {
                                                        player.sendMessage(C("MsgAuctionCancelledOnPlot")
                                                                                   + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + ". " + Util().moneyFormat(plot.getCurrentBid()));
                                                        break;
                                                    }
                                                }
                                            } else {
                                                p.sendMessage("§c" + er.errorMessage);
                                                warn(er.errorMessage);
                                            }

                                            plot.setAuctioned(false);
                                            plugin.getPlotMeCoreManager().adjustWall(p);
                                            plugin.getPlotMeCoreManager().setSellSign(world, plot);
                                            plot.setCurrentBid(0);
                                            plot.setCurrentBidder("");

                                            plot.updateField("currentbid", 0);
                                            plot.updateField("currentbidder", "");
                                            plot.updateField("currentbidderid", null);
                                            plot.updateField("auctionned", false);

                                            p.sendMessage(C("MsgAuctionCancelled"));

                                            if (isAdvancedLogging()) {
                                                plugin.getLogger().info(LOG + name + " " + C("MsgStoppedTheAuctionOnPlot") + " " + id);
                                            }
                                        } else {
                                            p.sendMessage("§c" + C("MsgPlotHasBidsAskAdmin"));
                                        }
                                    } else {
                                        plot.setAuctioned(false);
                                        plugin.getPlotMeCoreManager().adjustWall(p);
                                        plugin.getPlotMeCoreManager().setSellSign(world, plot);
                                        plot.setCurrentBid(0);
                                        plot.setCurrentBidder("");

                                        plot.updateField("currentbid", 0);
                                        plot.updateField("currentbidder", "");
                                        plot.updateField("currentbidderid", null);
                                        plot.updateField("auctionned", false);

                                        p.sendMessage(C("MsgAuctionCancelled"));

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
                                        p.sendMessage("§c" + C("MsgInvalidAmount"));
                                    } else {

                                        InternalPlotAuctionEvent event = sob.getEventFactory().callPlotAuctionEvent(plugin, world, plot, p, bid);

                                        if (!event.isCancelled()) {
                                            plot.setCurrentBid(bid);
                                            plot.setAuctioned(true);
                                            plugin.getPlotMeCoreManager().adjustWall(p);
                                            plugin.getPlotMeCoreManager().setSellSign(world, plot);

                                            plot.updateField("currentbid", bid);
                                            plot.updateField("auctionned", true);

                                            p.sendMessage(C("MsgAuctionStarted"));

                                            if (isAdvancedLogging()) {
                                                plugin.getLogger().info(LOG + name + " " + C("MsgStartedAuctionOnPlot") + " " + id + " " + C("WordAt") + " " + bid);
                                            }
                                        }
                                    }
                                }
                            } else {
                                p.sendMessage("§c" + C("MsgDoNotOwnPlot"));
                            }
                        } else {
                            p.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                        }
                    } else {
                        p.sendMessage("§c" + C("MsgPermissionDenied"));
                        return false;
                    }
                } else {
                    p.sendMessage("§c" + C("MsgSellingPlotsIsDisabledWorld"));
                }
            } else {
                p.sendMessage("§c" + C("MsgEconomyDisabledWorld"));
            }
        }
        return true;
    }
}
