package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.ClearReason;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotClearEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdClear extends PlotCommand {

    public CmdClear(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p) {
        if (plugin.cPerms(p, "PlotMe.admin.clear") || plugin.cPerms(p, "PlotMe.use.clear")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());
                if (id.isEmpty()) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                    if (plot.isProtect()) {
                        p.sendMessage(RED + C("MsgPlotProtectedCannotClear"));
                    } else {
                        String playername = p.getName();

                        if (plot.getOwner().equalsIgnoreCase(playername) || plugin.cPerms(p, "PlotMe.admin.clear")) {
                            IWorld w = p.getWorld();

                            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(w);

                            double price = 0;

                            InternalPlotClearEvent event;

                            if (plugin.getPlotMeCoreManager().isEconomyEnabled(w)) {
                                price = pmi.getClearPrice();
                                double balance = sob.getBalance(p);

                                if (balance >= price) {
                                    event = sob.getEventFactory().callPlotClearEvent(plugin, w, plot, p);

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
                                    p.sendMessage(RED + C("MsgNotEnoughClear") + " " + C("WordMissing") + " " + RESET + (price - balance) + RED + " " + sob.getEconomy().currencyNamePlural());
                                    return true;
                                }
                            } else {
                                event = sob.getEventFactory().callPlotClearEvent(plugin, w, plot, p);
                            }

                            if (!event.isCancelled()) {
                                plugin.getPlotMeCoreManager().clear(w, plot, p, ClearReason.Clear);

                                if (isAdvancedLogging()) {
                                    plugin.getLogger().info(LOG + playername + " " + C("MsgClearedPlot") + " " + id + (price != 0 ? " " + C("WordFor") + " " + price : ""));
                                }
                            }
                        } else {
                            p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedClear"));
                        }
                    }
                } else {
                    p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
