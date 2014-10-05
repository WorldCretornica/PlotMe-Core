package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.util.Calendar;
import java.util.UUID;

public class CmdPlotList extends PlotCommand {

    public CmdPlotList(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.use.list")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                String name;
                UUID uuid = null;

                if (plugin.cPerms(p, "PlotMe.admin.list") && args.length == 2) {
                    name = args[1];
                    IOfflinePlayer op = sob.getPlayerExact(name);
                    if (op != null) {
                        uuid = op.getUniqueId();
                    }
                    p.sendMessage(C("MsgListOfPlotsWhere") + " " + AQUA + name + RESET + " " + C("MsgCanBuild"));
                } else {
                    name = p.getName();
                    uuid = p.getUniqueId();
                    p.sendMessage(C("MsgListOfPlotsWhereYou"));
                }

                String oldworld = "";

                // Get plots of that player
                for (Plot plot : plugin.getSqlManager().getPlayerPlots(uuid, name)) {
                    if (!plot.getWorld().isEmpty()) {
                        IWorld world = sob.getWorld(plot.getWorld());
                        if (world != null) {
                            plugin.getPlotMeCoreManager().getMap(world).addPlot(plot.getId(), plot);
                        }
                    }

                    StringBuilder addition = new StringBuilder();

                    // Display worlds
                    if (!oldworld.equalsIgnoreCase(plot.getWorld())) {
                        oldworld = plot.getWorld();
                        p.sendMessage("  World: " + plot.getWorld());
                    }

                    // Is it expired?
                    if (plot.getExpiredDate() != null) {
                        java.util.Date tempdate = plot.getExpiredDate();

                        if (tempdate.compareTo(Calendar.getInstance().getTime()) < 0) {
                            addition.append(RED + " @" + plot.getExpiredDate() + RESET);
                        } else {
                            addition.append(" @" + plot.getExpiredDate());
                        }
                    }

                    // Is it auctionned?
                    if (plot.isAuctionned()) {
                        addition.append(" " + C("WordAuction") + ": " + GREEN + Util().round(plot.getCurrentBid()) + RESET + (!plot.getCurrentBidder().isEmpty() ? " " + plot.getCurrentBidder() : ""));
                    }

                    // Is it for sale?
                    if (plot.isForSale()) {
                        addition.append(" " + C("WordSell") + ": " + GREEN + Util().round(plot.getCustomPrice()) + RESET);
                    }

                    // Is the plot owner the name?
                    if (plot.getOwner().equalsIgnoreCase(name)) {
                        if (plot.allowedcount() == 0) {
                            // Is the name the current player too?
                            if (name.equalsIgnoreCase(p.getName())) {
                                p.sendMessage("  " + plot.getId() + " -> " + AQUA + ITALIC + C("WordYours") + RESET + addition);
                            } else {
                                p.sendMessage("  " + plot.getId() + " -> " + AQUA + ITALIC + plot.getOwner() + RESET + addition);
                            }
                        } else {
                            // Is the owner the current player?
                            if (plot.getOwner().equalsIgnoreCase(p.getName())) {
                                p.sendMessage("  " + plot.getId() + " -> " + AQUA + ITALIC + C("WordYours") + RESET + addition + ", " + C("WordHelpers") + ": " + AQUA + plot.getAllowed().replace(",", "" + RESET + "," + AQUA));
                            } else {
                                p.sendMessage("  " + plot.getId() + " -> " + AQUA + ITALIC + plot.getOwner() + RESET + addition + ", " + C("WordHelpers") + ": " + AQUA + plot.getAllowed().replace(",", "" + RESET + "," + AQUA));
                            }
                        }

                        // Is the name allowed to build there?
                    } else if (plot.isAllowedConsulting(name)) {
                        StringBuilder helpers = new StringBuilder();
                        for (String allowed : plot.allowed().getPlayers()) {
                            if (p.getName().equalsIgnoreCase(allowed)) {
                                if (name.equalsIgnoreCase(p.getName())) {
                                    helpers.append(AQUA).append(ITALIC).append("You").append(RESET).append(", ");
                                } else {
                                    helpers.append(AQUA).append(ITALIC).append(args[1]).append(RESET).append(", ");
                                }
                            } else {
                                helpers.append(AQUA).append(allowed).append(RESET).append(", ");
                            }
                        }
                        if (helpers.length() > 2) {
                            helpers.delete(helpers.length() - 2, helpers.length());
                        }

                        if (plot.getOwner().equalsIgnoreCase(p.getName())) {
                            p.sendMessage("  " + plot.getId() + " -> " + AQUA + C("WordYours") + RESET + addition + ", " + C("WordHelpers") + ": " + helpers);
                        } else {
                            p.sendMessage("  " + plot.getId() + " -> " + AQUA + plot.getOwner() + C("WordPossessive") + RESET + addition + ", " + C("WordHelpers") + ": " + helpers);
                        }
                    }
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
