package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.event.PlotMeEventFactory;
import com.worldcretornica.plotme_core.event.PlotOwnerChangeEvent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CmdSetOwner extends PlotCommand {

    public CmdSetOwner(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.admin.setowner")) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
            } else {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());
                if (id.isEmpty()) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (args.length < 2 || args[1].isEmpty()) {
                    p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandSetowner") + " <" + C("WordPlayer") + ">");
                } else {
                    String newowner = args[1];
                    String oldowner = "<" + C("WordNotApplicable") + ">";
                    String playername = p.getName();

                    if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                        Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                        PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(p);
                        oldowner = plot.getOwner();

                        PlotOwnerChangeEvent event;

                        if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
                            if (pmi.isRefundClaimPriceOnSetOwner() && newowner != oldowner) {
                                event = PlotMeEventFactory.callPlotOwnerChangeEvent(plugin, p.getWorld(), plot, p, newowner);

                                if (event.isCancelled()) {
                                    return true;
                                } else {
                                    EconomyResponse er = plugin.getEconomy().depositPlayer(oldowner, pmi.getClaimPrice());

                                    if (!er.transactionSuccess()) {
                                        p.sendMessage(RED + er.errorMessage);
                                        Util().warn(er.errorMessage);
                                        return true;
                                    } else {
                                        Player player = Bukkit.getServer().getPlayerExact(oldowner);
                                        if (player != null) {
                                            player.sendMessage(C("MsgYourPlot") + " " + id + " " + C("MsgNowOwnedBy") + " " + newowner + ". " + Util().moneyFormat(pmi.getClaimPrice()));
                                        }
                                    }
                                }
                            } else {
                                event = PlotMeEventFactory.callPlotOwnerChangeEvent(plugin, p.getWorld(), plot, p, newowner);
                            }

                            if (!plot.getCurrentBidder().isEmpty()) {
                                EconomyResponse er = plugin.getEconomy().depositPlayer(plot.getCurrentBidder(), plot.getCurrentBid());

                                if (!er.transactionSuccess()) {
                                    p.sendMessage(er.errorMessage);
                                    Util().warn(er.errorMessage);
                                } else {
                                    Player player = Bukkit.getServer().getPlayerExact(plot.getCurrentBidder());
                                    if (player != null) {
                                        player.sendMessage(C("WordPlot") + " " + id + " " + C("MsgChangedOwnerFrom") + " " + oldowner + " " + C("WordTo") + " " + newowner + ". " + Util().moneyFormat(plot.getCurrentBid()));
                                    }
                                }
                            }
                        } else {
                            event = PlotMeEventFactory.callPlotOwnerChangeEvent(plugin, p.getWorld(), plot, p, newowner);
                        }

                        if (!event.isCancelled()) {
                            plot.setCurrentBidder("");
                            plot.setCurrentBid(0);
                            plot.setAuctionned(false);
                            plot.setForSale(false);

                            plugin.getPlotMeCoreManager().setSellSign(p.getWorld(), plot);

                            plot.updateField("currentbidder", "");
                            plot.updateField("currentbid", 0);
                            plot.updateField("auctionned", false);
                            plot.updateField("forsale", false);

                            plot.setOwner(newowner);

                            plugin.getPlotMeCoreManager().setOwnerSign(p.getWorld(), plot);

                            plot.updateField("owner", newowner);
                        }
                    } else {
                        plugin.getPlotMeCoreManager().createPlot(p.getWorld(), id, newowner);
                    }

                    p.sendMessage(C("MsgOwnerChangedTo") + " " + RED + newowner);

                    plugin.getLogger().info(LOG + playername + " " + C("MsgChangedOwnerOf") + " " + id + " " + C("WordFrom") + " " + oldowner + " " + C("WordTo") + " " + newowner);
                }
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
