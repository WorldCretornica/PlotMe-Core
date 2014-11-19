package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.utils.Util;

public class CmdInfo extends PlotCommand {

    public CmdInfo(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission("PlotMe.use.info")) {
            IWorld world = player.getWorld();
            if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                String id = PlotMeCoreManager.getPlotId(player);

                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, world)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(id, world);

                    player.sendMessage("§aID: §b" + id + "§a " + C("InfoOwner") + ": §b" + plot.getOwner()
                                               + "§a " + C("InfoBiome") + ": §b" + plot.getBiome().name());

                    player.sendMessage("§a" + C("InfoExpire") + ": §b" + (plot.getExpiredDate() == null ? C("WordNever") : plot.getExpiredDate().toString())
                                          + "§a " + C("InfoFinished") + ": §b" + (plot.isFinished() ? C("WordYes") : C("WordNo"))
                                          + "§a " + C("InfoProtected") + ": §b" + (plot.isProtect() ? C("WordYes") : C("WordNo")));

                    if (plot.allowedcount() > 0) {
                        player.sendMessage("§a" + C("InfoHelpers") + ": §b" + plot.getAllowed());
                    }

                    if (plot.deniedcount() > 0) {
                        player.sendMessage("§a" + C("InfoDenied") + ": §b" + plot.getDenied());
                    }

                    if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                        if (plot.getCurrentBidder().isEmpty()) {
                            player.sendMessage("§a" + C("InfoAuctionned") + ": §b" + (plot.isAuctioned() ? C("WordYes")
                                                                                                                   + "§a " + C("InfoMinimumBid") + ": §b" + Util.round(plot.getCurrentBid()) : C("WordNo"))
                                                       + "§a " + C("InfoForSale") + ": §b" + (plot.isForSale() ? "§b" + Util.round(plot.getCustomPrice()) : C("WordNo")));
                        } else {
                            player.sendMessage("§a" + C("InfoAuctionned") + ": §b" + (plot.isAuctioned() ? C("WordYes")
                                                                                                              + "§a " + C("InfoBidder") + ": §b" + plot.getCurrentBidder()
                                                                                                                   + "§a " + C("InfoBid") + ": §b" + Util.round(plot.getCurrentBid()) : C("WordNo"))
                                                       + "§a " + C("InfoForSale") + ": §b" + (plot.isForSale() ? "§b" + Util.round(plot.getCustomPrice()) : C("WordNo")));
                        }
                    }
                    ILocation bottom = PlotMeCoreManager.getPlotBottomLoc(player.getWorld(), id);
                    ILocation top = PlotMeCoreManager.getPlotTopLoc(player.getWorld(), id);

                    player.sendMessage("§b" + C("WordBottom") + ": §r" + bottom.getBlockX() + "§9,§r" + bottom.getBlockZ());
                    player.sendMessage("§b" + C("WordTop") + ": §r" + top.getBlockX() + "§9,§r" + top.getBlockZ());

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
