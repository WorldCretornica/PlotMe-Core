package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotBuyEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdBuy extends PlotCommand {

    public CmdBuy(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        IWorld world = player.getWorld();
        if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
            if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                if (player.hasPermission("PlotMe.use.buy") || player.hasPermission("PlotMe.admin.buy")) {
                    String id = PlotMeCoreManager.getPlotId(player);

                    if (id.isEmpty()) {
                        player.sendMessage("§c" + C(MSG_NO_PLOT_FOUND));
                    } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, world)) {
                        Plot plot = plugin.getPlotMeCoreManager().getPlotById(id, world);

                        if (plot.isForSale()) {
                            String buyer = player.getName();

                            if (plot.getOwner().equalsIgnoreCase(buyer)) {
                                player.sendMessage("§c" + C("MsgCannotBuyOwnPlot"));
                            } else {
                                int plotlimit = getPlotLimit(player);

                                if (plotlimit != -1 && plugin.getPlotMeCoreManager().getNbOwnedPlot(player.getUniqueId(), player.getName(), world.getName()) >= plotlimit) {
                                    player.sendMessage(C("MsgAlreadyReachedMaxPlots") + " ("
                                                               + plugin.getPlotMeCoreManager().getNbOwnedPlot(player.getUniqueId(), player.getName(), world.getName()) + "/" + getPlotLimit(player) + "). "
                                                          + C("WordUse") + " §c/plotme home§r " + C("MsgToGetToIt"));
                                } else {

                                    double cost = plot.getCustomPrice();

                                    if (serverBridge.getBalance(player) < cost) {
                                        player.sendMessage("§c" + C("MsgNotEnoughBuy"));
                                    } else {

                                        InternalPlotBuyEvent event = serverBridge.getEventFactory().callPlotBuyEvent(plugin, world, plot, player, cost);

                                        if (!event.isCancelled()) {
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, cost);

                                            if (er.transactionSuccess()) {
                                                String oldowner = plot.getOwner();
                                                IOfflinePlayer playercurrentbidder = null;

                                                if (plot.getOwnerId() != null) {
                                                    playercurrentbidder = serverBridge.getOfflinePlayer(plot.getOwnerId());
                                                }

                                                if (!"$Bank$".equalsIgnoreCase(oldowner) && playercurrentbidder != null) {
                                                    EconomyResponse er2 = serverBridge.depositPlayer(playercurrentbidder, cost);

                                                    if (er2.transactionSuccess()) {
                                                        for (IPlayer onlinePlayers : serverBridge.getOnlinePlayers()) {
                                                            if (onlinePlayers.getName().equalsIgnoreCase(oldowner)) {
                                                                onlinePlayers.sendMessage(C("WordPlot") + " " + id + " "
                                                                                           + C("MsgSoldTo") + " " + buyer + ". " + Util().moneyFormat(cost));
                                                                break;
                                                            }
                                                        }
                                                    } else {
                                                        player.sendMessage("§c" + er2.errorMessage);
                                                        warn(er2.errorMessage);
                                                    }
                                                }

                                                plot.setOwner(buyer);
                                                plot.setCustomPrice(0.0);
                                                plot.setForSale(false);

                                                plot.updateField("owner", buyer);
                                                plot.updateField("customprice", 0);
                                                plot.updateField("forsale", false);

                                                plugin.getPlotMeCoreManager().adjustWall(player);
                                                plugin.getPlotMeCoreManager().setSellSign(world, plot);
                                                PlotMeCoreManager.setOwnerSign(world, plot);

                                                player.sendMessage(C("MsgPlotBought") + " " + Util().moneyFormat(-cost));

                                                if (isAdvancedLogging()) {
                                                    plugin.getLogger().info(buyer + " " + C("MsgBoughtPlot") + " " + id + " " + C("WordFor") + " " + cost);
                                                }
                                            } else {
                                                player.sendMessage("§c" + er.errorMessage);
                                                warn(er.errorMessage);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgPlotNotForSale"));
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
        }
        return true;
    }
}
