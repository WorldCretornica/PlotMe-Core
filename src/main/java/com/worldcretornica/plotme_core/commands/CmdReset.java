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
        if (p.hasPermission("PlotMe.admin.reset") || p.hasPermission("PlotMe.use.reset")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                Plot plot = plugin.getPlotMeCoreManager().getPlotById(p);

                if (plot == null) {
                    p.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (plot.isProtect()) {
                    p.sendMessage("§c" + C("MsgPlotProtectedCannotReset"));
                } else {
                    String playername = p.getName();
                    String id = plot.getId();

                    if (plot.getOwner().equalsIgnoreCase(playername) || p.hasPermission("PlotMe.admin.reset")) {
                        IWorld world = p.getWorld();

                        InternalPlotResetEvent event = sob.getEventFactory().callPlotResetEvent(plugin, world, plot, p);

                        if (!event.isCancelled()) {
                            plugin.getPlotMeCoreManager().setBiome(world, id, sob.getBiome("PLAINS"));
                            plugin.getPlotMeCoreManager().clear(world, plot, p, ClearReason.Reset);
                            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(p);

                            if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
                                if (plot.isAuctioned()) {
                                    if (plot.getCurrentBidderId() != null) {
                                        IOfflinePlayer offlinePlayer = sob.getOfflinePlayer(plot.getCurrentBidderId());
                                        EconomyResponse economyResponse = sob.depositPlayer(offlinePlayer, plot.getCurrentBid());

                                        if (economyResponse.transactionSuccess()) {
                                            IPlayer player = sob.getPlayer(offlinePlayer.getUniqueId());
                                            if (player != null) {
                                                player.sendMessage(C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C("MsgWasReset") + " " + Util().moneyFormat(plot.getCurrentBid()));
                                            }
                                        } else {
                                            p.sendMessage(economyResponse.errorMessage);
                                            warn(economyResponse.errorMessage);
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
                                        p.sendMessage("§c" + er.errorMessage);
                                        warn(er.errorMessage);
                                        return true;
                                    }
                                }
                            }

                            if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                                plugin.getPlotMeCoreManager().removePlot(world, id);
                            }

                            plugin.getPlotMeCoreManager().removeOwnerSign(world, id);
                            plugin.getPlotMeCoreManager().removeSellSign(world, id);
                            plugin.getSqlManager().deletePlot(PlotMeCoreManager.getIdX(id), PlotMeCoreManager.getIdZ(id), world.getName().toLowerCase());

                            pmi.addFreed(id);

                            if (isAdvancedLogging()) {
                                plugin.getLogger().info(LOG + p.getName() + " " + C("MsgResetPlot") + " " + id);
                            }
                        }
                    } else {
                        p.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedReset"));
                    }
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
