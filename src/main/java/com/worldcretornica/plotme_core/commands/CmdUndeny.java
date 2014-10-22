package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotRemoveDeniedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdUndeny extends PlotCommand {

    public CmdUndeny(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.undeny") || PlotMe_Core.cPerms(player, "PlotMe.use.undeny")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(player.getLocation());
                if (id.isEmpty()) {
                    player.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, player)) {
                    if (args.length < 2 || args[1].isEmpty()) {
                        player.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandUndeny") + " <" + C("WordPlayer") + ">");
                    } else {
                        Plot plot = plugin.getPlotMeCoreManager().getPlotById(player, id);
                        String playername = player.getName();
                        String denied = args[1];

                        if (plot.getOwner().equalsIgnoreCase(playername) || PlotMe_Core.cPerms(player, "PlotMe.admin.undeny")) {
                            if (plot.isDeniedConsulting(denied) || plot.isGroupDenied(denied)) {

                                IWorld world = player.getWorld();

                                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);

                                double price = 0;

                                InternalPlotRemoveDeniedEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                                    price = pmi.getUndenyPlayerPrice();
                                    double balance = sob.getBalance(player);

                                    if (balance >= price) {
                                        event = sob.getEventFactory().callPlotRemoveDeniedEvent(plugin, world, plot, player, denied);

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
                                        player.sendMessage(RED + C("MsgNotEnoughUndeny") + " " + C("WordMissing") + " " + RESET + Util().moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = sob.getEventFactory().callPlotRemoveDeniedEvent(plugin, world, plot, player, denied);
                                }

                                if (!event.isCancelled()) {
                                    plot.removeDenied(denied);

                                    player.sendMessage(C("WordPlayer") + " " + RED + denied + RESET + " " + C("MsgNowUndenied") + " " + Util().moneyFormat(-price));

                                    if (isAdvancedLogging()) {
                                        plugin.getLogger().info(LOG + playername + " " + C("MsgUndeniedPlayer") + " " + denied + " " + C("MsgFromPlot") + " " + id + (price != 0 ? " " + C("WordFor") + " " + price : ""));
                                    }
                                }
                            } else {
                                player.sendMessage(C("WordPlayer") + " " + RED + args[1] + RESET + " " + C("MsgWasNotDenied"));
                            }
                        } else {
                            player.sendMessage(RED + C("MsgThisPlot") + " (" + id + ") " + C("MsgNotYoursNotAllowedUndeny"));
                        }
                    }
                } else {
                    player.sendMessage(RED + C("MsgThisPlot") + " (" + id + ") " + C("MsgHasNoOwner"));
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
