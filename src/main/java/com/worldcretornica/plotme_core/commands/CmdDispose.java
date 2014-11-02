package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
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
        if (p.hasPermission("PlotMe.admin.dispose") || p.hasPermission("PlotMe.use.dispose")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(p);
                if (id.isEmpty()) {
                    p.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                    if (plot.isProtect()) {
                        p.sendMessage("§c" + C("MsgPlotProtectedNotDisposed"));
                    } else {
                        String name = p.getName();

                        if (plot.getOwner().equalsIgnoreCase(name) || p.hasPermission("PlotMe.admin.dispose")) {
                            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(p);

                            double cost = pmi.getDisposePrice();

                            IWorld world = p.getWorld();

                            InternalPlotDisposeEvent event;

                            if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
                                if (cost != 0 && sob.getBalance(p) < cost) {
                                    p.sendMessage("§c" + C("MsgNotEnoughDispose"));
                                    return true;
                                }

                                event = sob.getEventFactory().callPlotDisposeEvent(plugin, world, plot, p);

                                if (event.isCancelled()) {
                                    return true;
                                } else {
                                    EconomyResponse er = sob.withdrawPlayer(p, cost);

                                    if (!er.transactionSuccess()) {
                                        p.sendMessage("§c" + er.errorMessage);
                                        warn(er.errorMessage);
                                        return true;
                                    }

                                    if (plot.isAuctioned()) {
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
                                                p.sendMessage("§c" + er2.errorMessage);
                                                warn(er2.errorMessage);
                                            }
                                        }
                                    }
                                }
                            } else {
                                event = sob.getEventFactory().callPlotDisposeEvent(plugin, world, plot, p);
                            }

                            if (!event.isCancelled()) {
                                if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                                    plugin.getPlotMeCoreManager().removePlot(world, id);
                                }

                                plugin.getPlotMeCoreManager().removeOwnerSign(world, id);
                                plugin.getPlotMeCoreManager().removeSellSign(world, id);
                                plugin.getPlotMeCoreManager().removeAuctionSign(world, id);

                                plugin.getSqlManager().deletePlot(PlotMeCoreManager.getIdX(id), PlotMeCoreManager.getIdZ(id), world.getName().toLowerCase());

                                p.sendMessage(C("MsgPlotDisposedAnyoneClaim"));

                                if (isAdvancedLogging()) {
                                    plugin.getLogger().info(LOG + name + " " + C("MsgDisposedPlot") + " " + id);
                                }
                            }
                        } else {
                            p.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursCannotDispose"));
                        }
                    }
                } else {
                    p.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                p.sendMessage("§c" + C("MsgNotPlotWorld"));
            }
        } else {
            p.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
