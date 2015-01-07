package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.*;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotDisposeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdDispose extends PlotCommand {

    public CmdDispose(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission(PermissionNames.ADMIN_DISPOSE) || player.hasPermission(PermissionNames.USER_DISPOSE)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);
            if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                String id = PlotMeCoreManager.getPlotId(player);
                if (id.isEmpty()) {
                    player.sendMessage("§c" + C(MSG_NO_PLOT_FOUND));
                } else if (!PlotMeCoreManager.isPlotAvailable(id, pmi)) {
                    Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

                    if (plot.isProtect()) {
                        player.sendMessage("§c" + C("MsgPlotProtectedNotDisposed"));
                    } else {
                        String name = player.getName();

                        if (plot.getOwner().equalsIgnoreCase(name) || player.hasPermission(PermissionNames.ADMIN_DISPOSE)) {

                            double cost = pmi.getDisposePrice();

                            InternalPlotDisposeEvent event;

                            if (plugin.getPlotMeCoreManager().isEconomyEnabled(pmi)) {
                                if (cost != 0 && serverBridge.getBalance(player) < cost) {
                                    player.sendMessage("§c" + C("MsgNotEnoughDispose"));
                                    return true;
                                }

                                event = serverBridge.getEventFactory().callPlotDisposeEvent(plugin, world, plot, player);

                                if (event.isCancelled()) {
                                    return true;
                                } else {
                                    EconomyResponse economyResponse = serverBridge.withdrawPlayer(player, cost);

                                    if (!economyResponse.transactionSuccess()) {
                                        player.sendMessage("§c" + economyResponse.errorMessage);
                                        serverBridge.getLogger().warning(economyResponse.errorMessage);
                                        return true;
                                    }

                                    if (plot.isAuctioned()) {
                                        String currentbidder = plot.getCurrentBidder();

                                        if (currentbidder != null) {
                                            IOfflinePlayer playercurrentbidder = serverBridge.getOfflinePlayer(plot.getCurrentBidderId());
                                            EconomyResponse er2 = serverBridge.depositPlayer(playercurrentbidder, plot.getCurrentBid());

                                            if (er2.transactionSuccess()) {
                                                IPlayer currentBidder = serverBridge.getPlayer(playercurrentbidder.getUniqueId());
                                                if (currentBidder != null) {
                                                    currentBidder.sendMessage(C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C("MsgWasDisposed") + " " + Util().moneyFormat(cost, true));
                                                }
                                            } else {
                                                player.sendMessage("§c" + er2.errorMessage);
                                                serverBridge.getLogger().warning(er2.errorMessage);
                                            }
                                        }
                                    }
                                }
                            } else {
                                event = serverBridge.getEventFactory().callPlotDisposeEvent(plugin, world, plot, player);
                            }

                            if (!event.isCancelled()) {
                                if (!PlotMeCoreManager.isPlotAvailable(id, pmi)) {
                                    PlotMeCoreManager.removePlot(pmi, id);
                                }

                                PlotMeCoreManager.removeOwnerSign(world, id);
                                PlotMeCoreManager.removeSellSign(world, id);
                                PlotMeCoreManager.removeAuctionSign(world, id);

                                plugin.getSqlManager().deletePlot(PlotMeCoreManager.getIdX(id), PlotMeCoreManager.getIdZ(id), world.getName());

                                player.sendMessage(C("MsgPlotDisposedAnyoneClaim"));

                                if (isAdvancedLogging()) {
                                    serverBridge.getLogger().info(name + " " + C("MsgDisposedPlot") + " " + id);
                                }
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursCannotDispose"));
                        }
                    }
                } else {
                    player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
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
