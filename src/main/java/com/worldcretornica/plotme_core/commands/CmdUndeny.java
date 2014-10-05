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

    public boolean exec(IPlayer p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.admin.undeny") || plugin.cPerms(p, "PlotMe.use.undeny")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());
                if (id.isEmpty()) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    if (args.length < 2 || args[1].isEmpty()) {
                        p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandUndeny") + " <" + C("WordPlayer") + ">");
                    } else {
                        Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);
                        String playername = p.getName();
                        String denied = args[1];

                        if (plot.getOwner().equalsIgnoreCase(playername) || plugin.cPerms(p, "PlotMe.admin.undeny")) {
                            if (plot.isDeniedConsulting(denied) || plot.isGroupDenied(denied)) {

                                IWorld w = p.getWorld();

                                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(w);

                                double price = 0;

                                InternalPlotRemoveDeniedEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(w)) {
                                    price = pmi.getUndenyPlayerPrice();
                                    double balance = sob.getBalance(p);

                                    if (balance >= price) {
                                        event = sob.getEventFactory().callPlotRemoveDeniedEvent(plugin, w, plot, p, denied);

                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = sob.withdrawPlayer(p, price);

                                            if (!er.transactionSuccess()) {
                                                p.sendMessage(RED + er.errorMessage);
                                                Util().warn(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        p.sendMessage(RED + C("MsgNotEnoughUndeny") + " " + C("WordMissing") + " " + RESET + Util().moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = sob.getEventFactory().callPlotRemoveDeniedEvent(plugin, w, plot, p, denied);
                                }

                                if (!event.isCancelled()) {
                                    plot.removeDenied(denied);

                                    p.sendMessage(C("WordPlayer") + " " + RED + denied + RESET + " " + C("MsgNowUndenied") + " " + Util().moneyFormat(-price));

                                    if (isAdvancedLogging()) {
                                        plugin.getLogger().info(LOG + playername + " " + C("MsgUndeniedPlayer") + " " + denied + " " + C("MsgFromPlot") + " " + id + (price != 0 ? " " + C("WordFor") + " " + price : ""));
                                    }
                                }
                            } else {
                                p.sendMessage(C("WordPlayer") + " " + RED + args[1] + RESET + " " + C("MsgWasNotDenied"));
                            }
                        } else {
                            p.sendMessage(RED + C("MsgThisPlot") + " (" + id + ") " + C("MsgNotYoursNotAllowedUndeny"));
                        }
                    }
                } else {
                    p.sendMessage(RED + C("MsgThisPlot") + " (" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
