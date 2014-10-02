package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.entity.Player;

public class CmdInfo extends PlotCommand {

    public CmdInfo(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.use.info")) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
            } else {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());

                if (id.equals("")) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                    p.sendMessage(GREEN + C("InfoId") + ": " + AQUA + id
                                          + GREEN + " " + C("InfoOwner") + ": " + AQUA + plot.getOwner()
                                          + GREEN + " " + C("InfoBiome") + ": " + AQUA + Util().FormatBiome(plot.getBiome().name()));

                    p.sendMessage(GREEN + C("InfoExpire") + ": " + AQUA + ((plot.getExpiredDate() == null) ? C("WordNever") : plot.getExpiredDate().toString())
                                          + GREEN + " " + C("InfoFinished") + ": " + AQUA + ((plot.isFinished()) ? C("WordYes") : C("WordNo"))
                                          + GREEN + " " + C("InfoProtected") + ": " + AQUA + ((plot.isProtect()) ? C("WordYes") : C("WordNo")));

                    if (plot.allowedcount() > 0) {
                        p.sendMessage(GREEN + C("InfoHelpers") + ": " + AQUA + plot.getAllowed());
                    }

                    if (plugin.getConfig().getBoolean("allowToDeny") && plot.deniedcount() > 0) {
                        p.sendMessage(GREEN + C("InfoDenied") + ": " + AQUA + plot.getDenied());
                    }

                    if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
                        if (plot.getCurrentBidder().equals("")) {
                            p.sendMessage(GREEN + C("InfoAuctionned") + ": " + AQUA + ((plot.isAuctionned()) ? C("WordYes")
                                                                                                                       + GREEN + " " + C("InfoMinimumBid") + ": " + AQUA + Util().round(plot.getCurrentBid()) : C("WordNo"))
                                                  + GREEN + " " + C("InfoForSale") + ": " + AQUA + ((plot.isForSale()) ? AQUA + Util().round(plot.getCustomPrice()) : C("WordNo")));
                        } else {
                            p.sendMessage(GREEN + C("InfoAuctionned") + ": " + AQUA + ((plot.isAuctionned()) ? C("WordYes")
                                                                                                                       + GREEN + " " + C("InfoBidder") + ": " + AQUA + plot.getCurrentBidder()
                                                                                                                       + GREEN + " " + C("InfoBid") + ": " + AQUA + Util().round(plot.getCurrentBid()) : C("WordNo"))
                                                  + GREEN + " " + C("InfoForSale") + ": " + AQUA + ((plot.isForSale()) ? AQUA + Util().round(plot.getCustomPrice()) : C("WordNo")));
                        }
                    }
                } else {
                    p.sendMessage(RED + C("MsgThisPlot") + " (" + id + ") " + C("MsgHasNoOwner"));
                }
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
