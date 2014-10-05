package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.event.InternalPlotProtectChangeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdProtect extends PlotCommand {

    public CmdProtect(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.admin.protect") || plugin.cPerms(p, "PlotMe.use.protect")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());

                if (id.isEmpty()) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                    String name = p.getName();

                    if (plot.getOwner().equalsIgnoreCase(name) || plugin.cPerms(p, "PlotMe.admin.protect")) {
                        InternalPlotProtectChangeEvent event;

                        if (plot.isProtect()) {
                            event = sob.getEventFactory().callPlotProtectChangeEvent(plugin, p.getWorld(), plot, p, false);

                            if (!event.isCancelled()) {
                                plot.setProtect(false);
                                plugin.getPlotMeCoreManager().adjustWall(p.getLocation());

                                plot.updateField("protected", false);

                                p.sendMessage(C("MsgPlotNoLongerProtected"));

                                if (isAdvancedLogging()) {
                                    plugin.getLogger().info(LOG + name + " " + C("MsgUnprotectedPlot") + " " + id);
                                }
                            }
                        } else {
                            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(p);

                            double cost = 0;

                            if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
                                cost = pmi.getProtectPrice();

                                if (sob.getBalance(p) < cost) {
                                    p.sendMessage(RED + C("MsgNotEnoughProtectPlot"));
                                    return true;
                                } else {
                                    event = sob.getEventFactory().callPlotProtectChangeEvent(plugin, p.getWorld(), plot, p, true);

                                    if (event.isCancelled()) {
                                        return true;
                                    } else {
                                        EconomyResponse er = sob.withdrawPlayer(p, cost);

                                        if (!er.transactionSuccess()) {
                                            p.sendMessage(RED + er.errorMessage);
                                            Util().warn(er.errorMessage);
                                            return true;
                                        }
                                    }
                                }

                            } else {
                                event = sob.getEventFactory().callPlotProtectChangeEvent(plugin, p.getWorld(), plot, p, true);
                            }

                            if (!event.isCancelled()) {
                                plot.setProtect(true);
                                plugin.getPlotMeCoreManager().adjustWall(p.getLocation());

                                plot.updateField("protected", true);

                                p.sendMessage(C("MsgPlotNowProtected") + " " + Util().moneyFormat(-cost));

                                if (isAdvancedLogging()) {
                                    plugin.getLogger().info(LOG + name + " " + C("MsgProtectedPlot") + " " + id);
                                }
                            }
                        }
                    } else {
                        p.sendMessage(RED + C("MsgDoNotOwnPlot"));
                    }
                } else {
                    p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
                return true;
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
