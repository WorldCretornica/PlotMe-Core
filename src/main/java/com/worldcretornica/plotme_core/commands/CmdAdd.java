package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddAllowedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdAdd extends PlotCommand {

    public CmdAdd(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission("PlotMe.admin.add") || player.hasPermission("PlotMe.use.add")) {
            IWorld world = player.getWorld();
            if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                String id = PlotMeCoreManager.getPlotId(player);
                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, world)) {
                    if (args.length < 2 || args[1].isEmpty()) {
                        player.sendMessage(C("WordUsage") + " §c/plotme add <" + C("WordPlayer") + ">");
                    } else {

                        Plot plot = plugin.getPlotMeCoreManager().getPlotById(world, id);
                        String playername = player.getName();
                        String allowed = args[1];

                        if (plot.getOwner().equalsIgnoreCase(playername) || player.hasPermission("PlotMe.admin.add")) {
                            if (plot.isAllowedConsulting(allowed) || plot.isGroupAllowed(allowed)) {
                                player.sendMessage(C("WordPlayer") + " §c" + args[1] + "§r " + C("MsgAlreadyAllowed"));
                            } else {

                                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);

                                InternalPlotAddAllowedEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                                    double price = pmi.getAddPlayerPrice();
                                    double balance = sob.getBalance(player);

                                    if (balance >= price) {
                                        event = sob.getEventFactory().callPlotAddAllowedEvent(plugin, world, plot, player, allowed);

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
                                        player.sendMessage("§c" + C("MsgNotEnoughAdd") + " " + C("WordMissing") + " §r" + Util().moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = sob.getEventFactory().callPlotAddAllowedEvent(plugin, world, plot, player, allowed);
                                }

                                if (!event.isCancelled()) {
                                    plot.addAllowed(allowed);
                                    plot.removeDenied(allowed);

                                    double price = 0;
                                    player.sendMessage(C("WordPlayer") + " §c" + allowed + "§r " + C("MsgNowAllowed") + " " + Util().moneyFormat(-price));

                                    if (isAdvancedLogging()) {
                                        plugin.getLogger().info(LOG + playername + " " + C("MsgAddedPlayer") + " " + allowed + " " + C("MsgToPlot") + " "
                                                                        + id + (price != 0 ? " " + C("WordFor") + " " + price : ""));
                                    }
                                }
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedAdd"));
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
