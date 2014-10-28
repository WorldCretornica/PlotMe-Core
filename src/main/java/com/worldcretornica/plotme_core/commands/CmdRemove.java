package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotRemoveAllowedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.UUID;

public class CmdRemove extends PlotCommand {

    public CmdRemove(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission("PlotMe.admin.remove") || player.hasPermission("PlotMe.use.remove")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(player.getLocation());
                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, player)) {
                    if (args.length < 2 || args[1].isEmpty()) {
                        player.sendMessage(C("WordUsage") + ": §c/plotme " + C("CommandRemove") + " <" + C("WordPlayer") + ">");
                    } else {
                        Plot plot = plugin.getPlotMeCoreManager().getPlotById(player, id);
                        UUID playeruuid = player.getUniqueId();
                        String allowed = args[1];

                        if (plot.getOwnerId().equals(playeruuid) || player.hasPermission("PlotMe.admin.remove")) {
                            if (plot.isAllowedConsulting(allowed) || plot.isGroupAllowed(allowed)) {

                                IWorld world = player.getWorld();

                                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);

                                double price = 0;

                                InternalPlotRemoveAllowedEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                                    price = pmi.getRemovePlayerPrice();
                                    double balance = sob.getBalance(player);

                                    if (balance >= price) {
                                        event = sob.getEventFactory().callPlotRemoveAllowedEvent(plugin, world, plot, player, allowed);

                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = sob.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage("§c" + er.errorMessage);
                                                Util().warn(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage("§c" + C("MsgNotEnoughRemove") + " " + C("WordMissing") + " §r" + Util().moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = sob.getEventFactory().callPlotRemoveAllowedEvent(plugin, world, plot, player, allowed);
                                }

                                if (!event.isCancelled()) {
                                    plot.removeAllowed(allowed);

                                    player.sendMessage(C("WordPlayer") + " §c" + allowed + "§r " + C("WordRemoved") + ". " + Util().moneyFormat(-price));

                                    if (isAdvancedLogging()) {
                                        plugin.getLogger().info(LOG + allowed + " " + C("MsgRemovedPlayer") + " " + allowed + " " + C("MsgFromPlot") + " " + id + (price != 0 ? " " + C("WordFor") + " " + price : ""));
                                    }
                                }
                            } else {
                                player.sendMessage(C("WordPlayer") + " §c" + args[1] + "§r " + C("MsgWasNotAllowed"));
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedRemove"));
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
