package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CmdPlotList extends PlotCommand {

    public CmdPlotList(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p, String[] args) {
        if (p.hasPermission("PlotMe.use.list")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                String name;
                UUID uuid = null;

                if (p.hasPermission("PlotMe.admin.list") && args.length == 2) {
                    name = args[1];
                    IOfflinePlayer op = sob.getPlayerExact(name);
                    if (op != null) {
                        uuid = op.getUniqueId();
                    }
                    p.sendMessage(C("MsgListOfPlotsWhere") + " §b" + name + "§r " + C("MsgCanBuild"));
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
                        Date tempdate = plot.getExpiredDate();

                        if (tempdate.compareTo(Calendar.getInstance().getTime()) < 0) {
                            addition.append("§c @" + plot.getExpiredDate() + "§r");
                        } else {
                            addition.append(" @" + plot.getExpiredDate());
                        }
                    }

                    // Is it auctionned?
                    if (plot.isAuctioned()) {
                        addition.append(" " + C("WordAuction") + ": §a" + Util().round(plot.getCurrentBid()) + "§r" + (!plot.getCurrentBidder().isEmpty() ? " " + plot.getCurrentBidder() : ""));
                    }

                    // Is it for sale?
                    if (plot.isForSale()) {
                        addition.append(" " + C("WordSell") + ": §a" + Util().round(plot.getCustomPrice()) + "§r");
                    }

                    // Is the plot owner the name?
                    if (plot.getOwner().equalsIgnoreCase(name)) {
                        if (plot.allowedcount() == 0) {
                            // Is the name the current player too?
                            if (name.equalsIgnoreCase(p.getName())) {
                                p.sendMessage("  " + plot.getId() + " -> §b§o" + C("WordYours") + "§r" + addition);
                            } else {
                                p.sendMessage("  " + plot.getId() + " -> §b§o" + plot.getOwner() + "§r" + addition);
                            }
                        } else {
                            // Is the owner the current player?
                            if (plot.getOwner().equalsIgnoreCase(p.getName())) {
                                p.sendMessage("  " + plot.getId() + " -> §b§o" + C("WordYours") + "§r" + addition + ", " + C("WordHelpers") + ": §b" + plot.getAllowed().replace(",", "§r,§b"));
                            } else {
                                p.sendMessage("  " + plot.getId() + " -> §b§o" + plot.getOwner() + "§r" + addition + ", " + C("WordHelpers") + ": §b" + plot.getAllowed().replace(",", "§r,§b"));
                            }
                        }

                        // Is the name allowed to build there?
                    } else if (plot.isAllowedConsulting(name)) {
                        StringBuilder helpers = new StringBuilder();
                        for (String allowed : plot.allowed().getPlayers()) {
                            if (p.getName().equalsIgnoreCase(allowed)) {
                                if (name.equalsIgnoreCase(p.getName())) {
                                    helpers.append("§b").append("§o").append("You").append("§r").append(", ");
                                } else {
                                    helpers.append("§b").append("§o").append(args[1]).append("§r").append(", ");
                                }
                            } else {
                                helpers.append("§b").append(allowed).append("§r").append(", ");
                            }
                        }
                        if (helpers.length() > 2) {
                            helpers.delete(helpers.length() - 2, helpers.length());
                        }

                        if (plot.getOwner().equalsIgnoreCase(p.getName())) {
                            p.sendMessage("  " + plot.getId() + " -> §b" + C("WordYours") + "§r" + addition + ", " + C("WordHelpers") + ": " + helpers);
                        } else {
                            p.sendMessage("  " + plot.getId() + " -> §b" + plot.getOwner() + C("WordPossessive") + "§r" + addition + ", " + C("WordHelpers") + ": " + helpers);
                        }
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
