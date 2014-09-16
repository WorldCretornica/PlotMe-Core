package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.ClearReason;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.event.PlotMeEventFactory;
import com.worldcretornica.plotme_core.event.PlotResetEvent;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class CmdReset extends PlotCommand {

    public CmdReset(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.admin.reset") || plugin.cPerms(p, "PlotMe.use.reset")) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
            } else {
                Plot plot = plugin.getPlotMeCoreManager().getPlotById(p.getLocation());

                if (plot == null) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (plot.isProtect()) {
                    p.sendMessage(RED + C("MsgPlotProtectedCannotReset"));
                } else {
                    String playername = p.getName();
                    String id = plot.getId();

                    if (plot.getOwner().equalsIgnoreCase(playername) || plugin.cPerms(p, "PlotMe.admin.reset")) {
                        World w = p.getWorld();

                        PlotResetEvent event = PlotMeEventFactory.callPlotResetEvent(plugin, w, plot, p);

                        if (!event.isCancelled()) {
                            plugin.getPlotMeCoreManager().setBiome(w, id, Biome.PLAINS);
                            plugin.getPlotMeCoreManager().clear(w, plot, p, ClearReason.Reset);
                            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(p);

                            if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
                                if (plot.isAuctionned()) {
                                    if (plot.getCurrentBidderId() != null) {
                                        OfflinePlayer playercurrentbidder = Bukkit.getOfflinePlayer(plot.getCurrentBidderId());
                                        EconomyResponse er = plugin.getEconomy().depositPlayer(playercurrentbidder, plot.getCurrentBid());

                                        if (!er.transactionSuccess()) {
                                            p.sendMessage(er.errorMessage);
                                            Util().warn(er.errorMessage);
                                        } else {
                                            Player player = Bukkit.getPlayer(playercurrentbidder.getUniqueId());
                                            if (player != null) {
                                                player.sendMessage(C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C("MsgWasReset") + " " + Util().moneyFormat(plot.getCurrentBid()));
                                            }
                                        }
                                    }
                                }

                                if (pmi.isRefundClaimPriceOnReset() && plot.getOwnerId() != null) {                                    
                                    OfflinePlayer playerowner = Bukkit.getOfflinePlayer(plot.getOwnerId());
                                    
                                    EconomyResponse er = plugin.getEconomy().depositPlayer(playerowner, pmi.getClaimPrice());

                                    if (!er.transactionSuccess()) {
                                        p.sendMessage(RED + er.errorMessage);
                                        Util().warn(er.errorMessage);
                                        return true;
                                    } else {
                                        Player player = Bukkit.getPlayer(playerowner.getUniqueId());
                                        if (player.getName().equalsIgnoreCase(plot.getOwner())) {
                                            player.sendMessage(C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C("MsgWasReset") + " " + Util().moneyFormat(pmi.getClaimPrice()));
                                        }
                                    }
                                }
                            }

                            if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                                plugin.getPlotMeCoreManager().removePlot(w, id);
                            }

                            plugin.getPlotMeCoreManager().removeOwnerSign(w, id);
                            plugin.getPlotMeCoreManager().removeSellSign(w, id);
                            plugin.getSqlManager().deletePlot(plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), w.getName().toLowerCase());

                            pmi.addFreed(id);

                            if (isAdvancedLogging()) {
                                plugin.getLogger().info(LOG + p.getName() + " " + C("MsgResetPlot") + " " + id);
                            }
                        }
                    } else {
                        p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedReset"));
                    }
                }
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
