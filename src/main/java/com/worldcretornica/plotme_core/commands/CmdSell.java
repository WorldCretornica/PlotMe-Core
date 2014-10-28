package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotSellChangeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdSell extends PlotCommand {

    public CmdSell(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p, String[] args) {
        if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(p);

            if (pmi.isCanSellToBank() || pmi.isCanPutOnSale()) {
                if (p.hasPermission("PlotMe.use.sell") || p.hasPermission("PlotMe.admin.sell")) {
                    ILocation location = p.getLocation();
                    String id = plugin.getPlotMeCoreManager().getPlotId(location);

                    if (id.isEmpty()) {
                        p.sendMessage("§c" + C("MsgNoPlotFound"));
                    } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                        Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                        if (plot.getOwnerId().equals(p.getUniqueId()) || p.hasPermission("PlotMe.admin.sell")) {
                            IWorld w = p.getWorld();

                            InternalPlotSellChangeEvent event;

                            if (plot.isForSale()) {
                                event = sob.getEventFactory().callPlotSellChangeEvent(plugin, w, plot, p, plot.getCustomPrice(), false, false);

                                if (!event.isCancelled()) {
                                    plot.setCustomPrice(0);
                                    plot.setForSale(false);

                                    plot.updateField("customprice", 0);
                                    plot.updateField("forsale", false);

                                    plugin.getPlotMeCoreManager().adjustWall(location);
                                    plugin.getPlotMeCoreManager().setSellSign(w, plot);

                                    p.sendMessage(C("MsgPlotNoLongerSale"));

                                    if (isAdvancedLogging()) {
                                        plugin.getLogger().info(LOG + p.getName() + " " + C("MsgRemovedPlot") + " " + id + " " + C("MsgFromBeingSold"));
                                    }
                                }
                            } else {
                                double price = pmi.getSellToPlayerPrice();
                                boolean bank = false;

                                if (args.length == 2) {
                                    if (args[1].equalsIgnoreCase("bank")) {
                                        bank = true;
                                    } else if (pmi.isCanCustomizeSellPrice()) {
                                        try {
                                            price = Double.parseDouble(args[1]);
                                        } catch (Exception e) {
                                            if (pmi.isCanSellToBank()) {
                                                p.sendMessage(C("WordUsage") + ": §c /plotme " + C("CommandSellBank") + "|<" + C("WordAmount") + ">");
                                                p.sendMessage("  " + C("WordExample") + ": §c/plotme " + C("CommandSellBank") + " §r or §c /plotme " + C("CommandSell") + " 200");
                                            } else {
                                                p.sendMessage(C("WordUsage") + ": §c"
                                                                      + " /plotme " + C("CommandSell") + " <" + C("WordAmount") + ">§r"
                                                                      + " " + C("WordExample") + ": §c/plotme " + C("CommandSell") + " 200");
                                            }
                                        }
                                    } else {
                                        p.sendMessage("§c" + C("MsgCannotCustomPriceDefault") + " " + price);
                                        return true;
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
                                                for (IPlayer player : sob.getOnlinePlayers()) {
                                                    if (player.getName().equalsIgnoreCase(currentbidder)) {
                                                        player.sendMessage(C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C("MsgSoldToBank") + " " + Util().moneyFormat(bid));
                                                        break;
                                                    }
                                                }
                                            } else {
                                                p.sendMessage("§c" + er.errorMessage);
                                                Util().warn(er.errorMessage);
                                            }
                                        }

                                        double sellprice = pmi.getSellToBankPrice();

                                        event = sob.getEventFactory().callPlotSellChangeEvent(plugin, w, plot, p, pmi.getBuyFromBankPrice(), true, true);

                                        if (!event.isCancelled()) {
                                            EconomyResponse er = sob.depositPlayer(p, sellprice);

                                            if (er.transactionSuccess()) {
                                                plot.setOwner("$Bank$");
                                                plot.setForSale(true);
                                                plot.setCustomPrice(pmi.getBuyFromBankPrice());
                                                plot.setAuctioned(false);
                                                plot.setCurrentBidder("");
                                                plot.setCurrentBidderId(null);
                                                plot.setCurrentBid(0);

                                                plot.removeAllAllowed();

                                                plugin.getPlotMeCoreManager().setOwnerSign(w, plot);
                                                plugin.getPlotMeCoreManager().setSellSign(w, plot);

                                                plot.updateField("owner", plot.getOwner());
                                                plot.updateField("forsale", true);
                                                plot.updateField("auctionned", true);
                                                plot.updateField("customprice", plot.getCustomPrice());
                                                plot.updateField("currentbidder", "");
                                                plot.updateField("currentbidderid", null);
                                                plot.updateField("currentbid", 0);

                                                p.sendMessage(C("MsgPlotSold") + " " + Util().moneyFormat(sellprice));

                                                if (isAdvancedLogging()) {
                                                    plugin.getLogger().info(LOG + p.getName() + " " + C("MsgSoldToBankPlot") + " " + id + " " + C("WordFor") + " " + sellprice);
                                                }
                                            } else {
                                                p.sendMessage(" " + er.errorMessage);
                                                Util().warn(er.errorMessage);
                                            }
                                        }
                                    } else {
                                        p.sendMessage("§c" + C("MsgCannotSellToBank"));
                                    }
                                } else if (price < 0) {
                                    p.sendMessage("§c" + C("MsgInvalidAmount"));
                                } else {
                                    event = sob.getEventFactory().callPlotSellChangeEvent(plugin, w, plot, p, price, false, true);

                                    if (!event.isCancelled()) {
                                        plot.setCustomPrice(price);
                                        plot.setForSale(true);

                                        plot.updateField("customprice", price);
                                        plot.updateField("forsale", true);

                                        plugin.getPlotMeCoreManager().adjustWall(location);
                                        plugin.getPlotMeCoreManager().setSellSign(w, plot);

                                        p.sendMessage(C("MsgPlotForSale"));

                                        if (isAdvancedLogging()) {
                                            plugin.getLogger().info(LOG + p.getName() + " " + C("MsgPutOnSalePlot") + " " + id + " " + C("WordFor") + " " + price);
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
        return true;
    }
}
