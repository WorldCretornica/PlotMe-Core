package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotProtectChangeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdProtect extends PlotCommand {

    public CmdProtect(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission(PermissionNames.ADMIN_PROTECT) || player.hasPermission(PermissionNames.USER_PROTECT)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                String id = manager.getPlotId(player);

                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!manager.isPlotAvailable(id, pmi)) {
                    Plot plot = manager.getPlotById(id, pmi);

                    String name = player.getName();

                    if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_PROTECT)) {
                        InternalPlotProtectChangeEvent event;

                        if (plot.isProtect()) {
                            event = serverBridge.getEventFactory().callPlotProtectChangeEvent(plugin, world, plot, player, false);

                            if (!event.isCancelled()) {
                                plot.setProtect(false);
                                manager.adjustWall(player);

                                plot.updateField("protected", false);

                                player.sendMessage(C("MsgPlotNoLongerProtected"));

                                if (isAdvancedLogging()) {
                                    serverBridge.getLogger().info(name + " " + C("MsgUnprotectedPlot") + " " + id);
                                }
                            }
                        } else {

                            double cost = 0.0;

                            if (manager.isEconomyEnabled(pmi)) {
                                cost = pmi.getProtectPrice();

                                if (serverBridge.getBalance(player) < cost) {
                                    player.sendMessage("§c" + C("MsgNotEnoughProtectPlot"));
                                    return true;
                                } else {
                                    event = serverBridge.getEventFactory().callPlotProtectChangeEvent(plugin, world, plot, player, true);

                                    if (event.isCancelled()) {
                                        return true;
                                    } else {
                                        EconomyResponse er = serverBridge.withdrawPlayer(player, cost);

                                        if (!er.transactionSuccess()) {
                                            player.sendMessage("§c" + er.errorMessage);
                                            serverBridge.getLogger().warning(er.errorMessage);
                                            return true;
                                        }
                                    }
                                }

                            } else {
                                event = serverBridge.getEventFactory().callPlotProtectChangeEvent(plugin, world, plot, player, true);
                            }

                            if (!event.isCancelled()) {
                                plot.setProtect(true);
                                manager.adjustWall(player);

                                plot.updateField("protected", true);

                                double price = -cost;
                                player.sendMessage(C("MsgPlotNowProtected") + " " + Util().moneyFormat(price, true));

                                if (isAdvancedLogging()) {
                                    serverBridge.getLogger().info(name + " " + C("MsgProtectedPlot") + " " + id);
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
                player.sendMessage("§c" + C("MsgNotPlotWorld"));
                return true;
            }
        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
