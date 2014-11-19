package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotSellChangeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdSell extends PlotCommand {

    public CmdSell(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        IWorld world = player.getWorld();
        if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);
            if (plugin.getPlotMeCoreManager().isEconomyEnabled(pmi)) {

                if (pmi.isCanSellToBank() || pmi.isCanPutOnSale()) {
                    if (player.hasPermission("PlotMe.use.sell") || player.hasPermission("PlotMe.admin.sell")) {
                        String id = PlotMeCoreManager.getPlotId(player);

                        if (id.isEmpty()) {
                            player.sendMessage("§c" + C("MsgNoPlotFound"));
                        } else if (!PlotMeCoreManager.isPlotAvailable(id, pmi)) {
                            Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

                            if (plot.getOwnerId().equals(player.getUniqueId()) || player.hasPermission("PlotMe.admin.sell")) {

                                InternalPlotSellChangeEvent event;

                                if (plot.isForSale()) {
                                    event = sob.getEventFactory().callPlotSellChangeEvent(plugin, world, plot, player, plot.getCustomPrice(), false, false);

                                    if (!event.isCancelled()) {
                                        plot.setCustomPrice(0);
                                        plot.setForSale(false);

                                        plot.updateField("customprice", 0);
                                        plot.updateField("forsale", false);

                                        plugin.getPlotMeCoreManager().adjustWall(player);
                                        plugin.getPlotMeCoreManager().setSellSign(world, plot);

                                        player.sendMessage(C("MsgPlotNoLongerSale"));

                                        if (isAdvancedLogging()) {
                                            sob.getLogger().info(LOG + player.getName() + " " + C("MsgRemovedPlot") + " " + id + " " + C("MsgFromBeingSold"));
                                        }
                                    }
                                } else {
                                    double price = pmi.getSellToPlayerPrice();
                                    boolean bank = false;

                                    if (args.length == 2) {
                                        if ("bank".equalsIgnoreCase(args[1])) {
                                            bank = true;
                                        } else {
                                            try {
                                                price = Double.parseDouble(args[1]);
                                            } catch (Exception e) {
                                                if (pmi.isCanSellToBank()) {
                                                    player.sendMessage(C("WordUsage") + ": §c /plotme sell bank|<" + C("WordAmount") + ">");
                                                    player.sendMessage(C("WordExample") + ": §c/plotme sell bank §r or §c /plotme sell 200");
                                                } else {
                                                    player.sendMessage(C("WordUsage") + ": §c /plotme sell <" + C("WordAmount") + ">§r " + C("WordExample") + ": §c/plotme sell 200");
                                                }
                                            }
                                        }
                                    }

                                    if (bank) {
                                        if (pmi.isCanSellToBank()) {
                                            String currentbidder = plot.getCurrentBidder();

                                            if (!currentbidder.isEmpty()) {
                                                double bid = plot.getCurrentBid();
                                                IOfflinePlayer playercurrentbidder = sob.getOfflinePlayer(plot.getCurrentBidderId());

                                                EconomyResponse er = sob.depositPlayer(playercurrentbidder, bid);

                                                if (er.transactionSuccess()) {
                                                    for (IPlayer iPlayer : sob.getOnlinePlayers()) {
                                                        if (iPlayer.getName().equalsIgnoreCase(currentbidder)) {
                                                            iPlayer.sendMessage(C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C("MsgSoldToBank") + " " + Util().moneyFormat(bid));
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    player.sendMessage("§c" + er.errorMessage);
                                                    warn(er.errorMessage);
                                                }
                                            }

                                            double sellprice = pmi.getSellToBankPrice();

                                            event = sob.getEventFactory().callPlotSellChangeEvent(plugin, world, plot, player, pmi.getBuyFromBankPrice(), true, true);

                                            if (!event.isCancelled()) {
                                                EconomyResponse er = sob.depositPlayer(player, sellprice);

                                                if (er.transactionSuccess()) {
                                                    plot.setOwner("$Bank$");
                                                    plot.setForSale(true);
                                                    plot.setCustomPrice(pmi.getBuyFromBankPrice());
                                                    plot.setAuctioned(false);
                                                    plot.setCurrentBidder("");
                                                    plot.setCurrentBidderId(null);
                                                    plot.setCurrentBid(0);

                                                    plot.removeAllAllowed();

                                                    PlotMeCoreManager.setOwnerSign(world, plot);
                                                    plugin.getPlotMeCoreManager().setSellSign(world, plot);

                                                    plot.updateField("owner", plot.getOwner());
                                                    plot.updateField("forsale", true);
                                                    plot.updateField("auctionned", true);
                                                    plot.updateField("customprice", plot.getCustomPrice());
                                                    plot.updateField("currentbidder", "");
                                                    plot.updateField("currentbidderid", null);
                                                    plot.updateField("currentbid", 0);

                                                    player.sendMessage(C("MsgPlotSold") + " " + Util().moneyFormat(sellprice));

                                                    if (isAdvancedLogging()) {
                                                        sob.getLogger().info(LOG + player.getName() + " " + C("MsgSoldToBankPlot") + " " + id + " " + C("WordFor") + " " + sellprice);
                                                    }
                                                } else {
                                                    player.sendMessage(er.errorMessage);
                                                    warn(er.errorMessage);
                                                }
                                            }
                                        } else {
                                            player.sendMessage("§c" + C("MsgCannotSellToBank"));
                                        }
                                    } else if (price < 0) {
                                        player.sendMessage("§c" + C("MsgInvalidAmount"));
                                    } else {
                                        event = sob.getEventFactory().callPlotSellChangeEvent(plugin, world, plot, player, price, false, true);

                                        if (!event.isCancelled()) {
                                            plot.setCustomPrice(price);
                                            plot.setForSale(true);

                                            plot.updateField("customprice", price);
                                            plot.updateField("forsale", true);

                                            plugin.getPlotMeCoreManager().adjustWall(player);
                                            plugin.getPlotMeCoreManager().setSellSign(world, plot);

                                            player.sendMessage(C("MsgPlotForSale"));

                                            if (isAdvancedLogging()) {
                                                sob.getLogger().info(LOG + player.getName() + " " + C("MsgPutOnSalePlot") + " " + id + " " + C("WordFor") + " " + price);
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
