package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
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
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.add") || PlotMe_Core.cPerms(player, "PlotMe.use.add")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(player.getLocation());
                if (id.isEmpty()) {
                    player.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, player)) {
                    if (args.length < 2 || args[1].isEmpty()) {
                        player.sendMessage(C("WordUsage") + " " + RED + "/plotme " + C("CommandAdd") + " <" + C("WordPlayer") + ">");
                    } else {

                        Plot plot = plugin.getPlotMeCoreManager().getPlotById(player, id);
                        String playername = player.getName();
                        String allowed = args[1];

                        if (plot.getOwner().equalsIgnoreCase(playername) || PlotMe_Core.cPerms(player, "PlotMe.admin.add")) {
                            if (plot.isAllowedConsulting(allowed) || plot.isGroupAllowed(allowed)) {
                                player.sendMessage(C("WordPlayer") + " " + RED + args[1] + RESET + " " + C("MsgAlreadyAllowed"));
                            } else {
                                IWorld w = player.getWorld();

                                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(player);

                                double price = 0;

                                InternalPlotAddAllowedEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(player)) {
                                    price = pmi.getAddPlayerPrice();
                                    double balance = sob.getBalance(player);

                                    if (balance >= price) {
                                        event = sob.getEventFactory().callPlotAddAllowedEvent(plugin, w, plot, player, allowed);

                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = sob.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage(RED + er.errorMessage);
                                                Util().warn(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage(RED + C("MsgNotEnoughAdd") + " " + C("WordMissing") + " " + RESET + Util().moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = sob.getEventFactory().callPlotAddAllowedEvent(plugin, w, plot, player, allowed);
                                }

                                if (!event.isCancelled()) {
                                    plot.addAllowed(allowed);
                                    plot.removeDenied(allowed);

                                    player.sendMessage(C("WordPlayer") + " " + RED + allowed + RESET + " " + C("MsgNowAllowed") + " " + Util().moneyFormat(-price));

                                    if (isAdvancedLogging()) {
                                        plugin.getLogger().info(LOG + playername + " " + C("MsgAddedPlayer") + " " + allowed + " " + C("MsgToPlot") + " "
                                                                        + id + (price != 0 ? " " + C("WordFor") + " " + price : ""));
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedAdd"));
                        }
                    }
                } else {
                    player.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                player.sendMessage(RED + C("MsgNotPlotWorld"));
            }
        } else {
            player.sendMessage(RED + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
