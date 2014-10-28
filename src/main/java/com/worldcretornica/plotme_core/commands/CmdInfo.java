package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.utils.Util;

public class CmdInfo extends PlotCommand {

    public CmdInfo(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p) {
        if (p.hasPermission("PlotMe.use.info")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());

                if (id.isEmpty()) {
                    p.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                    p.sendMessage("§aID: §b" + id
                                          + "§a " + C("InfoOwner") + ": §b" + plot.getOwner()
                                          + "§a " + C("InfoBiome") + ": §b" + Util.FormatBiome(plot.getBiome().name()));

                    p.sendMessage("§a" + C("InfoExpire") + ": §b" + (plot.getExpiredDate() == null ? C("WordNever") : plot.getExpiredDate().toString())
                                          + "§a " + C("InfoFinished") + ": §b" + (plot.isFinished() ? C("WordYes") : C("WordNo"))
                                          + "§a " + C("InfoProtected") + ": §b" + (plot.isProtect() ? C("WordYes") : C("WordNo")));

                    if (plot.allowedcount() > 0) {
                        p.sendMessage("§a" + C("InfoHelpers") + ": §b" + plot.getAllowed());
                    }

                    if (sob.getConfig().getBoolean("allowToDeny") && plot.deniedcount() > 0) {
                        p.sendMessage("§a" + C("InfoDenied") + ": §b" + plot.getDenied());
                    }

                    if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
                        if (plot.getCurrentBidder().isEmpty()) {
                            p.sendMessage("§a" + C("InfoAuctionned") + ": §b" + (plot.isAuctioned() ? C("WordYes")
                                                                                                              + "§a " + C("InfoMinimumBid") + ": §b" + Util().round(plot.getCurrentBid()) : C("WordNo"))
                                                  + "§a " + C("InfoForSale") + ": §b" + (plot.isForSale() ? "§b" + Util().round(plot.getCustomPrice()) : C("WordNo")));
                        } else {
                            p.sendMessage("§a" + C("InfoAuctionned") + ": §b" + (plot.isAuctioned() ? C("WordYes")
                                                                                                              + "§a " + C("InfoBidder") + ": §b" + plot.getCurrentBidder()
                                                                                                              + "§a " + C("InfoBid") + ": §b" + Util().round(plot.getCurrentBid()) : C("WordNo"))
                                                  + "§a " + C("InfoForSale") + ": §b" + (plot.isForSale() ? "§b" + Util().round(plot.getCustomPrice()) : C("WordNo")));
                        }
                    }
                    ILocation bottom = plugin.getPlotMeCoreManager().getPlotBottomLoc(p.getWorld(), id);
                    ILocation top = plugin.getPlotMeCoreManager().getPlotTopLoc(p.getWorld(), id);

                    p.sendMessage("§b" + C("WordBottom") + ": §r" + bottom.getBlockX() + "§9,§r" + bottom.getBlockZ());
                    p.sendMessage("§b" + C("WordTop") + ": §r" + top.getBlockX() + "§9,§r" + top.getBlockZ());

                } else {
                    p.sendMessage("§c" + C("MsgThisPlot") + " (" + id + ") " + C("MsgHasNoOwner"));
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
