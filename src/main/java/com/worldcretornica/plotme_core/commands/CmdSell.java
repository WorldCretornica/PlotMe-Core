package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.event.PlotMeEventFactory;
import com.worldcretornica.plotme_core.event.PlotSellChangeEvent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CmdSell extends PlotCommand {

    public CmdSell(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player p, String[] args) {
        if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(p);

            if (pmi.isCanSellToBank() || pmi.isCanPutOnSale()) {
                if (plugin.cPerms(p, "PlotMe.use.sell") || plugin.cPerms(p, "PlotMe.admin.sell")) {
                    Location l = p.getLocation();
                    String id = plugin.getPlotMeCoreManager().getPlotId(l);

                    if (id.equals("")) {
                        p.sendMessage(RED + C("MsgNoPlotFound"));
                    } else {
                        if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                            Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                            if (plot.getOwner().equalsIgnoreCase(p.getName()) || plugin.cPerms(p, "PlotMe.admin.sell")) {
                                World w = p.getWorld();
                                String name = p.getName();

                                PlotSellChangeEvent event;

                                if (plot.isForSale()) {
                                    event = PlotMeEventFactory.callPlotSellChangeEvent(plugin, w, plot, p, plot.getCustomPrice(), false, false);

                                    if (!event.isCancelled()) {
                                        plot.setCustomPrice(0);
                                        plot.setForSale(false);

                                        plot.updateField("customprice", 0);
                                        plot.updateField("forsale", false);

                                        plugin.getPlotMeCoreManager().adjustWall(l);
                                        plugin.getPlotMeCoreManager().setSellSign(w, plot);

                                        p.sendMessage(C("MsgPlotNoLongerSale"));

                                        if (true) {
                                            plugin.getLogger().info(LOG + name + " " + C("MsgRemovedPlot") + " " + id + " " + C("MsgFromBeingSold"));
                                        }
                                    }
                                } else {
                                    double price = pmi.getSellToPlayerPrice();
                                    boolean bank = false;

                                    if (args.length == 2) {
                                        if (args[1].equalsIgnoreCase("bank")) {
                                            bank = true;
                                        } else {
                                            if (pmi.isCanCustomizeSellPrice()) {
                                                try {
                                                    price = Double.parseDouble(args[1]);
                                                } catch (Exception e) {
                                                    if (pmi.isCanSellToBank()) {
                                                        p.sendMessage(C("WordUsage") + ": " + RED + " /plotme " + C("CommandSellBank") + "|<" + C("WordAmount") + ">");
                                                        p.sendMessage("  " + C("WordExample") + ": " + RED + "/plotme " + C("CommandSellBank") + " " + RESET + " or " + RED + " /plotme " + C("CommandSell") + " 200");
                                                    } else {
                                                        p.sendMessage(C("WordUsage") + ": " + RED
                                                                + " /plotme " + C("CommandSell") + " <" + C("WordAmount") + ">" + RESET
                                                                + " " + C("WordExample") + ": " + RED + "/plotme " + C("CommandSell") + " 200");
                                                    }
                                                }
                                            } else {
                                                p.sendMessage(RED + C("MsgCannotCustomPriceDefault") + " " + price);
                                                return true;
                                            }
                                        }
                                    }

                                    if (bank) {
                                        if (!pmi.isCanSellToBank()) {
                                            p.sendMessage(RED + C("MsgCannotSellToBank"));
                                        } else {
                                            String currentbidder = plot.getCurrentBidder();

                                            if (!currentbidder.equals("")) {
                                                double bid = plot.getCurrentBid();

                                                EconomyResponse er = plugin.getEconomy().depositPlayer(currentbidder, bid);

                                                if (!er.transactionSuccess()) {
                                                    p.sendMessage(RED + er.errorMessage);
                                                    Util().warn(er.errorMessage);
                                                } else {
                                                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                                                        if (player.getName().equalsIgnoreCase(currentbidder)) {
                                                            player.sendMessage(C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C("MsgSoldToBank") + " " + Util().moneyFormat(bid));
                                                            break;
                                                        }
                                                    }
                                                }
                                            }

                                            double sellprice = pmi.getSellToBankPrice();

                                            event = PlotMeEventFactory.callPlotSellChangeEvent(plugin, w, plot, p, pmi.getBuyFromBankPrice(), true, true);

                                            if (!event.isCancelled()) {
                                                EconomyResponse er = plugin.getEconomy().depositPlayer(name, sellprice);

                                                if (er.transactionSuccess()) {
                                                    plot.setOwner("$Bank$");
                                                    plot.setForSale(true);
                                                    plot.setCustomPrice(pmi.getBuyFromBankPrice());
                                                    plot.setAuctionned(false);
                                                    plot.setCurrentBidder("");
                                                    plot.setCurrentBid(0);

                                                    plot.removeAllAllowed();

                                                    plugin.getPlotMeCoreManager().setOwnerSign(w, plot);
                                                    plugin.getPlotMeCoreManager().setSellSign(w, plot);

                                                    plot.updateField("owner", plot.getOwner());
                                                    plot.updateField("forsale", true);
                                                    plot.updateField("auctionned", true);
                                                    plot.updateField("customprice", plot.getCustomPrice());
                                                    plot.updateField("currentbidder", "");
                                                    plot.updateField("currentbid", 0);

                                                    p.sendMessage(C("MsgPlotSold") + " " + Util().moneyFormat(sellprice));

                                                    if (true) {
                                                        plugin.getLogger().info(LOG + name + " " + C("MsgSoldToBankPlot") + " " + id + " " + C("WordFor") + " " + sellprice);
                                                    }
                                                } else {
                                                    p.sendMessage(" " + er.errorMessage);
                                                    Util().warn(er.errorMessage);
                                                }
                                            }
                                        }
                                    } else {
                                        if (price < 0) {
                                            p.sendMessage(RED + C("MsgInvalidAmount"));
                                        } else {
                                            event = PlotMeEventFactory.callPlotSellChangeEvent(plugin, w, plot, p, price, false, true);

                                            if (!event.isCancelled()) {
                                                plot.setCustomPrice(price);
                                                plot.setForSale(true);

                                                plot.updateField("customprice", price);
                                                plot.updateField("forsale", true);

                                                plugin.getPlotMeCoreManager().adjustWall(l);
                                                plugin.getPlotMeCoreManager().setSellSign(w, plot);

                                                p.sendMessage(C("MsgPlotForSale"));

                                                if (true) {
                                                    plugin.getLogger().info(LOG + name + " " + C("MsgPutOnSalePlot") + " " + id + " " + C("WordFor") + " " + price);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                p.sendMessage(RED + C("MsgDoNotOwnPlot"));
                            }
                        } else {
                            p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                        }
                    }
                } else {
                    p.sendMessage(RED + C("MsgPermissionDenied"));
                }
            } else {
                p.sendMessage(RED + C("MsgSellingPlotsIsDisabledWorld"));
            }
        } else {
            p.sendMessage(RED + C("MsgEconomyDisabledWorld"));
        }
        return true;
    }
}
