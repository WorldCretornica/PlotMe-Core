package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.api.event.InternalPlotOwnerChangeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdSetOwner extends PlotCommand {

    public CmdSetOwner(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player player, String[] args) {
        World world = player.getWorld();
        PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);
        if (player.hasPermission(PermissionNames.ADMIN_SETOWNER)) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                String id = PlotMeCoreManager.getPlotId(player);
                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else {
                    String newowner = args[1];
                    String oldowner = "<" + C("WordNotApplicable") + ">";
                    String playername = player.getName();

                    if (!PlotMeCoreManager.isPlotAvailable(id, pmi)) {
                        Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

                        oldowner = plot.getOwner();

                        InternalPlotOwnerChangeEvent event;

                        if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                            if (pmi.isRefundClaimPriceOnSetOwner() && !newowner.equals(oldowner)) {
                                event = serverBridge.getEventFactory().callPlotOwnerChangeEvent(plugin, world, plot, player, newowner);

                                if (event.isCancelled()) {
                                    return true;
                                } else if (plot.getOwnerId() != null) {
                                    IOfflinePlayer playeroldowner = serverBridge.getOfflinePlayer(plot.getOwnerId());
                                    EconomyResponse er = serverBridge.depositPlayer(playeroldowner, pmi.getClaimPrice());

                                    if (er.transactionSuccess()) {
                                        Player oldOwner = serverBridge.getPlayer(playeroldowner.getUniqueId());
                                        if (oldOwner != null) {
                                            oldOwner.sendMessage(
                                                    C("MsgYourPlot") + " " + id + " " + C("MsgNowOwnedBy") + " " + newowner + ". " + Util()
                                                            .moneyFormat(pmi.getClaimPrice(), true));
                                        }
                                    } else {
                                        player.sendMessage("§c" + er.errorMessage);
                                        serverBridge.getLogger().warning(er.errorMessage);
                                        return true;
                                    }
                                }
                            } else {
                                event = serverBridge.getEventFactory().callPlotOwnerChangeEvent(plugin, world, plot, player, newowner);
                            }

                            if (plot.getCurrentBidderId() != null) {
                                IOfflinePlayer playercurrentbidder = serverBridge.getOfflinePlayer(plot.getCurrentBidderId());
                                EconomyResponse er = serverBridge.depositPlayer(playercurrentbidder, plot.getCurrentBid());

                                if (er.transactionSuccess()) {
                                    Player currentBidder = serverBridge.getPlayer(playercurrentbidder.getUniqueId());
                                    if (currentBidder != null) {
                                        currentBidder.sendMessage(
                                                C("WordPlot") + " " + id + " " + C("MsgChangedOwnerFrom") + " " + oldowner + " " + C("WordTo") + " "
                                                + newowner + ". " + Util().moneyFormat(plot.getCurrentBid(), true));
                                    }
                                } else {
                                    player.sendMessage(er.errorMessage);
                                    serverBridge.getLogger().warning(er.errorMessage);
                                }
                            }
                        } else {
                            event = serverBridge.getEventFactory().callPlotOwnerChangeEvent(plugin, world, plot, player, newowner);
                        }

                        if (!event.isCancelled()) {
                            plot.setCurrentBidder(null);
                            plot.setCurrentBidderId(null);
                            plot.setCurrentBid(0.0);
                            plot.setAuctioned(false);
                            plot.setForSale(false);

                            plugin.getPlotMeCoreManager().setSellSign(world, plot);

                            plot.updateField("currentbidder", null);
                            plot.updateField("currentbid", 0);
                            plot.updateField("auctionned", false);
                            plot.updateField("forsale", false);
                            plot.updateField("currentbidderid", null);

                            plot.setOwner(newowner);

                            PlotMeCoreManager.setOwnerSign(world, plot);

                            plot.updateField("owner", newowner);
                        }
                    } else {
                        plugin.getPlotMeCoreManager().createPlot(world, id, newowner, null, pmi);
                    }

                    player.sendMessage(C("MsgOwnerChangedTo") + " §c" + newowner);

                    if (isAdvancedLogging()) {
                        serverBridge.getLogger()
                                .info(playername + " " + C("MsgChangedOwnerOf") + " " + id + " " + C("WordFrom") + " " + oldowner + " " + C("WordTo")
                                      + " " + newowner);
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
