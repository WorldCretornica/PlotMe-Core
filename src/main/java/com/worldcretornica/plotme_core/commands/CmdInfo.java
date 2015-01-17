package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBiome;

public class CmdInfo extends PlotCommand {

    public CmdInfo(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission(PermissionNames.USER_INFO)) {
            IWorld world = player.getWorld();
            if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                String id = PlotMeCoreManager.getPlotId(player);

                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, world)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(id, world);

                    player.sendMessage("§aID: §b" + id + "§a " + C("InfoOwner") + ": §b" + plot.getOwner()
                                       + "§a " + C("InfoBiome") + ": §b" + ((BukkitBiome) plot.getBiome()).getBiome().name());

                    if (plot.getExpiredDate() == null) {
                        if (plot.isFinished()) {
                            if (plot.isProtect()) {
                                player.sendMessage("§a" + C("InfoExpire") + ": §b" + C("WordNever")
                                                   + "§a " + C("InfoFinished") + ": §b" + C("WordYes")
                                                   + "§a " + C("InfoProtected") + ": §b" + C("WordYes"));
                            } else {
                                player.sendMessage("§a" + C("InfoExpire") + ": §b" + C("WordNever")
                                                   + "§a " + C("InfoFinished") + ": §b" + C("WordYes")
                                                   + "§a " + C("InfoProtected") + ": §b" + C("WordNo"));
                            }
                        } else if (plot.isProtect()) {
                            player.sendMessage("§a" + C("InfoExpire") + ": §b" + C("WordNever")
                                               + "§a " + C("InfoFinished") + ": §b" + C("WordNo")
                                               + "§a " + C("InfoProtected") + ": §b" + C("WordYes"));
                        } else {
                            player.sendMessage("§a" + C("InfoExpire") + ": §b" + C("WordNever")
                                               + "§a " + C("InfoFinished") + ": §b" + C("WordNo")
                                               + "§a " + C("InfoProtected") + ": §b" + C("WordNo"));
                        }
                    } else if (plot.isProtect()) {
                        if (plot.isFinished()) {
                            player.sendMessage("§a" + C("InfoExpire") + ": §b" + plot.getExpiredDate()
                                               + "§a " + C("InfoFinished") + ": §b" + C("WordYes")
                                               + "§a " + C("InfoProtected") + ": §b" + C("WordYes"));
                        } else {
                            player.sendMessage("§a" + C("InfoExpire") + ": §b" + plot.getExpiredDate()
                                               + "§a " + C("InfoFinished") + ": §b" + C("WordNo")
                                               + "§a " + C("InfoProtected") + ": §b" + C("WordYes"));
                        }
                    } else if (plot.isFinished()) {
                        player.sendMessage("§a" + C("InfoExpire") + ": §b" + plot.getExpiredDate()
                                           + "§a " + C("InfoFinished") + ": §b" + C("WordYes")
                                           + "§a " + C("InfoProtected") + ": §b" + C("WordNo"));
                    } else {
                        player.sendMessage("§a" + C("InfoExpire") + ": §b" + plot.getExpiredDate()
                                           + "§a " + C("InfoFinished") + ": §b" + C("WordNo")
                                           + "§a " + C("InfoProtected") + ": §b" + C("WordNo"));
                    }

                    if (plot.allowedcount() > 0) {
                        player.sendMessage("§a" + C("InfoHelpers") + ": §b" + plot.getAllowed());
                    }

                    if (plot.deniedcount() > 0) {
                        player.sendMessage("§a" + C("InfoDenied") + ": §b" + plot.getDenied());
                    }

                    if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                        if (plot.getCurrentBidder() == null) {
                            if (plot.isAuctioned()) {
                                if (plot.isForSale()) {
                                    player.sendMessage("§a" + C("InfoAuctionned") + ": §b" + (C("WordYes")
                                                                                              + "§a " + C("InfoMinimumBid") + ": §b" + Math
                                            .round(plot.getCurrentBid()))
                                                       + "§a " + C("InfoForSale") + ": §b" + ("§b" + Math.round(plot.getCustomPrice())));
                                } else {
                                    player.sendMessage("§a" + C("InfoAuctionned") + ": §b" + (C("WordYes")
                                                                                              + "§a " + C("InfoMinimumBid") + ": §b" + Math
                                            .round(plot.getCurrentBid()))
                                                       + "§a " + C("InfoForSale") + ": §b" + C("WordNo"));
                                }
                            } else if (plot.isForSale()) {
                                player.sendMessage("§a" + C("InfoAuctionned") + ": §b" + C("WordNo")
                                                   + "§a " + C("InfoForSale") + ": §b" + ("§b" + Math.round(plot.getCustomPrice())));
                            } else {
                                player.sendMessage("§a" + C("InfoAuctionned") + ": §b" + C("WordNo")
                                                   + "§a " + C("InfoForSale") + ": §b" + C("WordNo"));
                            }
                        } else {
                            if (plot.isAuctioned()) {
                                player.sendMessage("§a" + C("InfoAuctionned") + ": §b" + (C("WordYes")
                                                                                          + "§a " + C("InfoBidder") + ": §b" + plot.getCurrentBidder()
                                                                                          + "§a " + C("InfoBid") + ": §b" + Math
                                                                                                  .round(plot.getCurrentBid()))
                                                   + "§a " + C("InfoForSale") + ": §b" + (plot.isForSale() ? "§b" + Math.round(plot.getCustomPrice())
                                                                                                           : C("WordNo")));
                            } else if (plot.isForSale()) {
                                player.sendMessage("§a" + C("InfoAuctionned") + ": §b" + C("WordNo")
                                                   + "§a " + C("InfoForSale") + ": §b" + ("§b" + Math.round(plot.getCustomPrice())));
                            } else {
                                player.sendMessage("§a" + C("InfoAuctionned") + ": §b" + C("WordNo")
                                                   + "§a " + C("InfoForSale") + ": §b" + C("WordNo"));
                            }
                        }
                    }
                    int bottomX = PlotMeCoreManager.bottomX(id, world);
                    int bottomZ = PlotMeCoreManager.bottomZ(id, world);
                    int topX = PlotMeCoreManager.topX(id, world);
                    int topZ = PlotMeCoreManager.topZ(id, world);

                    player.sendMessage("§b" + C("WordBottom") + ": §r" + bottomX + "§9,§r" + bottomZ);
                    player.sendMessage("§b" + C("WordTop") + ": §r" + topX + "§9,§r" + topZ);

                } else {
                    player.sendMessage("§c" + C("MsgThisPlot") + " (" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                player.sendMessage("§c" + C("MsgNotPlotWorld"));
            }
        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
