package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.*;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotResetEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdReset extends PlotCommand {

    public CmdReset(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p) {
        if (PlotMe_Core.cPerms(p, "PlotMe.admin.reset") || PlotMe_Core.cPerms(p, "PlotMe.use.reset")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                Plot plot = plugin.getPlotMeCoreManager().getPlotById(p.getLocation());

                if (plot == null) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (plot.isProtect()) {
                    p.sendMessage(RED + C("MsgPlotProtectedCannotReset"));
                } else {
                    String playername = p.getName();
                    String id = plot.getId();

                    if (plot.getOwner().equalsIgnoreCase(playername) || PlotMe_Core.cPerms(p, "PlotMe.admin.reset")) {
                        IWorld w = p.getWorld();

                        InternalPlotResetEvent event = sob.getEventFactory().callPlotResetEvent(plugin, w, plot, p);

                        if (!event.isCancelled()) {
                            plugin.getPlotMeCoreManager().setBiome(w, id, sob.getBiome("PLAINS"));
                            plugin.getPlotMeCoreManager().clear(w, plot, p, ClearReason.Reset);
                            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(p);

                            if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
                                if (plot.isAuctioned()) {
                                    if (plot.getCurrentBidderId() != null) {
                                        IOfflinePlayer playercurrentbidder = sob.getOfflinePlayer(plot.getCurrentBidderId());
                                        EconomyResponse er = sob.depositPlayer(playercurrentbidder, plot.getCurrentBid());

                                        if (er.transactionSuccess()) {
                                            IPlayer player = sob.getPlayer(playercurrentbidder.getUniqueId());
                                            if (player != null) {
                                                player.sendMessage(C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C("MsgWasReset") + " " + Util().moneyFormat(plot.getCurrentBid()));
                                            }
                                        } else {
                                            p.sendMessage(er.errorMessage);
                                            Util().warn(er.errorMessage);
                                        }
                                    }
                                }

                                if (pmi.isRefundClaimPriceOnReset() && plot.getOwnerId() != null) {
                                    IOfflinePlayer playerowner = sob.getOfflinePlayer(plot.getOwnerId());

                                    EconomyResponse er = sob.depositPlayer(playerowner, pmi.getClaimPrice());

                                    if (er.transactionSuccess()) {
                                        IPlayer player = sob.getPlayer(playerowner.getUniqueId());
                                        if (player.getName().equalsIgnoreCase(plot.getOwner())) {
                                            player.sendMessage(C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C("MsgWasReset") + " " + Util().moneyFormat(pmi.getClaimPrice()));
                                        }
                                    } else {
                                        p.sendMessage(RED + er.errorMessage);
                                        Util().warn(er.errorMessage);
                                        return true;
                                    }
                                }
                            }

                            if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                                plugin.getPlotMeCoreManager().removePlot(w, id);
                            }

                            plugin.getPlotMeCoreManager().removeOwnerSign(w, id);
                            plugin.getPlotMeCoreManager().removeSellSign(w, id);
                            plugin.getSqlManager().deletePlot(PlotMeCoreManager.getIdX(id), PlotMeCoreManager.getIdZ(id), w.getName().toLowerCase());

                            pmi.addFreed(id);

                            if (isAdvancedLogging()) {
                                plugin.getLogger().info(LOG + p.getName() + " " + C("MsgResetPlot") + " " + id);
                            }
                        }
                    } else {
                        p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedReset"));
                    }
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
