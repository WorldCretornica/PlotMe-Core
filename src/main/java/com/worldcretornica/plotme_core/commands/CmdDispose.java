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
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);
                if (id == null) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!manager.isPlotAvailable(id, pmi)) {
                    Plot plot = manager.getPlotById(id, pmi);

                    if (plot.isProtect()) {
                        player.sendMessage("§c" + C("MsgPlotProtectedNotDisposed"));
                    } else {
                        String name = player.getName();

                        if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_DISPOSE)) {

                            double cost = pmi.getDisposePrice();

                            InternalPlotDisposeEvent event;

                            if (manager.isEconomyEnabled(pmi)) {
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
                                                    currentBidder.sendMessage(
                                                            C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C(
                                                                    "MsgWasDisposed") + " " + Util().moneyFormat(cost, true));
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
                                if (!manager.isPlotAvailable(id, pmi)) {
                                    manager.removePlot(pmi, id);
                                }

                                manager.removeOwnerSign(world, id);
                                manager.removeSellSign(world, id);
                                manager.removeAuctionSign(world, id);

                                plugin.getSqlManager().deletePlot(id, world.getName());

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
