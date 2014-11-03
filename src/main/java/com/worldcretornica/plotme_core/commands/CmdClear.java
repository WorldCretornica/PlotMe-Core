package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.*;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotClearEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdClear extends PlotCommand {

    public CmdClear(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission("PlotMe.admin.clear") || player.hasPermission("PlotMe.use.clear")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                String id = PlotMeCoreManager.getPlotId(player);
                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, player)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(player, id);

                    if (plot.isProtect()) {
                        player.sendMessage("§c" + C("MsgPlotProtectedCannotClear"));
                    } else {
                        String playername = player.getName();

                        if (plot.getOwner().equalsIgnoreCase(playername) || player.hasPermission("PlotMe.admin.clear")) {
                            IWorld world = player.getWorld();

                            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);

                            double price = 0;

                            InternalPlotClearEvent event;

                            if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                                price = pmi.getClearPrice();
                                double balance = sob.getBalance(player);

                                if (balance >= price) {
                                    event = sob.getEventFactory().callPlotClearEvent(plugin, world, plot, player);

                                    if (event.isCancelled()) {
                                        return true;
                                    } else {
                                        EconomyResponse er = sob.withdrawPlayer(player, price);

                                        if (!er.transactionSuccess()) {
                                            player.sendMessage("§c" + er.errorMessage);
                                            warn(er.errorMessage);
                                            return true;
                                        }
                                    }
                                } else {
                                    player.sendMessage("§c" + C("MsgNotEnoughClear") + " " + C("WordMissing") + " §r" + (price - balance) + "§c " + sob.getEconomy().currencyNamePlural());
                                    return true;
                                }
                            } else {
                                event = sob.getEventFactory().callPlotClearEvent(plugin, world, plot, player);
                            }

                            if (!event.isCancelled()) {
                                plugin.getPlotMeCoreManager().clear(world, plot, player, ClearReason.Clear);

                                if (isAdvancedLogging()) {
                                    plugin.getLogger().info(LOG + playername + " " + C("MsgClearedPlot") + " " + id + (price != 0 ? " " + C("WordFor") + " " + price : ""));
                                }
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedClear"));
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
