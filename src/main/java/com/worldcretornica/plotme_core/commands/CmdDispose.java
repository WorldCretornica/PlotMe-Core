package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
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

                                event = serverBridge.getEventFactory().callPlotDisposeEvent(world, plot, player);

                                if (event.isCancelled()) {
                                    return true;
                                }
                                EconomyResponse economyResponse = serverBridge.withdrawPlayer(player, cost);

                                if (!economyResponse.transactionSuccess()) {
                                    player.sendMessage("§c" + economyResponse.errorMessage);
                                    serverBridge.getLogger().warning(economyResponse.errorMessage);
                                    return true;
                                }
                            } else {
                                event = serverBridge.getEventFactory().callPlotDisposeEvent(world, plot, player);
                            }

                            if (!event.isCancelled()) {
                                if (!manager.isPlotAvailable(id, pmi)) {
                                    manager.removePlot(pmi, id);
                                }

                                manager.removeOwnerSign(world, id);
                                manager.removeSellSign(world, id);
                                manager.removeAuctionSign(world, id);

                                plugin.getSqlManager().deletePlot(id, world.getName());
                                manager.adjustWall(world, id, false);
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
