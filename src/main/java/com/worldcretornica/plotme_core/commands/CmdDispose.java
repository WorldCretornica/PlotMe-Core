package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotDisposeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdDispose extends PlotCommand {

    public CmdDispose(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p) {
        if (plugin.cPerms(p, "PlotMe.admin.dispose") || plugin.cPerms(p, "PlotMe.use.dispose")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());
                if (id.isEmpty()) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                    if (plot.isProtect()) {
                        p.sendMessage(RED + C("MsgPlotProtectedNotDisposed"));
                    } else {
                        String name = p.getName();

                        if (plot.getOwner().equalsIgnoreCase(name) || plugin.cPerms(p, "PlotMe.admin.dispose")) {
                            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(p);

                            double cost = pmi.getDisposePrice();

                            IWorld w = p.getWorld();

                            InternalPlotDisposeEvent event;

                            if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
                                if (cost != 0 && sob.getBalance(p) < cost) {
                                    p.sendMessage(RED + C("MsgNotEnoughDispose"));
                                    return true;
                                }

                                event = sob.getEventFactory().callPlotDisposeEvent(plugin, w, plot, p);

                                if (event.isCancelled()) {
                                    return true;
                                } else {
                                    EconomyResponse er = sob.withdrawPlayer(p, cost);

                                    if (!er.transactionSuccess()) {
                                        p.sendMessage(RED + er.errorMessage);
                                        Util().warn(er.errorMessage);
                                        return true;
                                    }

                                    if (plot.isAuctionned()) {
                                        String currentbidder = plot.getCurrentBidder();

                                        if (!currentbidder.isEmpty()) {
                                            IOfflinePlayer playercurrentbidder = sob.getOfflinePlayer(plot.getCurrentBidderId());
                                            EconomyResponse er2 = sob.depositPlayer(playercurrentbidder, plot.getCurrentBid());

                                            if (er2.transactionSuccess()) {
                                                IPlayer player = sob.getPlayer(playercurrentbidder.getUniqueId());
                                                if (player != null) {
                                                    player.sendMessage(C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C("MsgWasDisposed") + " " + Util().moneyFormat(cost));
                                                }
                                            } else {
                                                p.sendMessage(RED + er2.errorMessage);
                                                Util().warn(er2.errorMessage);
                                            }
                                        }
                                    }
                                }
                            } else {
                                event = sob.getEventFactory().callPlotDisposeEvent(plugin, w, plot, p);
                            }

                            if (!event.isCancelled()) {
                                if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                                    plugin.getPlotMeCoreManager().removePlot(w, id);
                                }

                                plugin.getPlotMeCoreManager().removeOwnerSign(w, id);
                                plugin.getPlotMeCoreManager().removeSellSign(w, id);
                                plugin.getPlotMeCoreManager().removeAuctionSign(w, id);

                                plugin.getSqlManager().deletePlot(plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), w.getName().toLowerCase());

                                p.sendMessage(C("MsgPlotDisposedAnyoneClaim"));

                                if (isAdvancedLogging()) {
                                    plugin.getLogger().info(LOG + name + " " + C("MsgDisposedPlot") + " " + id);
                                }
                            }
                        } else {
                            p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursCannotDispose"));
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
