package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CmdPlotList extends PlotCommand {

    public CmdPlotList(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission(PermissionNames.USER_LIST)) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                String name;
                UUID uuid;

                if (player.hasPermission(PermissionNames.ADMIN_LIST) && args.length == 2) {
                    name = args[1];
                    uuid = null;
                    player.sendMessage(C("MsgListOfPlotsWhere") + " §b" + name + "§r " + C("MsgCanBuild"));
                } else {
                    name = player.getName();
                    uuid = player.getUniqueId();
                    player.sendMessage(C("MsgListOfPlotsWhereYou"));
                }

                String oldworld = "";

                // Get plots of that player
                for (Plot plot : plugin.getSqlManager().getPlayerPlots(name, uuid)) {
                    IWorld world = serverBridge.getWorld(plot.getWorld());
                    if (world != null) {
                        plugin.getPlotMeCoreManager().getMap(world).addPlot(plot.getId(), plot);
                    }

                    StringBuilder addition = new StringBuilder();

                    // Display worlds
                    if (!oldworld.equalsIgnoreCase(plot.getWorld())) {
                        oldworld = plot.getWorld();
                        player.sendMessage("  World: " + plot.getWorld());
                    }

                    // Is it expired?
                    if (plot.getExpiredDate() != null) {
                        Date tempdate = plot.getExpiredDate();

                        if (tempdate.before(Calendar.getInstance().getTime())) {
                            addition.append("§c @" + plot.getExpiredDate() + "§r");
                        } else {
                            addition.append(" @" + plot.getExpiredDate());
                        }
                    }

                    // Is it auctionned?
                    if (plot.isAuctioned()) {
                        if (plot.getCurrentBidder() != null) {
                            addition.append(
                                    " " + C("WordAuction") + ": §a" + Math.round(plot.getCurrentBid()) + "§r" + (" " + plot.getCurrentBidder()));
                        } else {
                            addition.append(" " + C("WordAuction") + ": §a" + Math.round(plot.getCurrentBid()) + "§r");
                        }
                    }

                    // Is it for sale?
                    if (plot.isForSale()) {
                        addition.append(" " + C("WordSell") + ": §a" + Math.round(plot.getCustomPrice()) + "§r");
                    }

                    // Is the plot owner the name?
                    if (plot.getOwner().equalsIgnoreCase(name)) {
                        if (plot.allowedcount() == 0) {
                            // Is the name the current player too?
                            if (name.equalsIgnoreCase(player.getName())) {
                                player.sendMessage("  " + plot.getId() + " -> §b§o" + C("WordYours") + "§r" + addition);
                            } else {
                                player.sendMessage("  " + plot.getId() + " -> §b§o" + plot.getOwner() + "§r" + addition);
                            }
                        } else {
                            // Is the owner the current player?
                            if (plot.getOwner().equalsIgnoreCase(player.getName())) {
                                player.sendMessage(
                                        "  " + plot.getId() + " -> §b§o" + C("WordYours") + "§r" + addition + ", " + C("WordHelpers") + ": §b" + plot
                                                .getAllowed().replace(",", "§r,§b"));
                            } else {
                                player.sendMessage(
                                        "  " + plot.getId() + " -> §b§o" + plot.getOwner() + "§r" + addition + ", " + C("WordHelpers") + ": §b" + plot
                                                .getAllowed().replace(",", "§r,§b"));
                            }
                        }

                        // Is the name allowed to build there?
                    } else if (plot.isAllowedConsulting(name)) {
                        StringBuilder helpers = new StringBuilder();
                        for (String allowed : plot.allowed().getPlayers()) {
                            if (player.getName().equalsIgnoreCase(allowed)) {
                                if (name.equalsIgnoreCase(player.getName())) {
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

                        if (plot.getOwner().equalsIgnoreCase(player.getName())) {
                            player.sendMessage(
                                    "  " + plot.getId() + " -> §b" + C("WordYours") + "§r" + addition + ", " + C("WordHelpers") + ": " + helpers);
                        } else {
                            player.sendMessage(
                                    "  " + plot.getId() + " -> §b" + plot.getOwner() + C("WordPossessive") + "§r" + addition + ", " + C("WordHelpers")
                                    + ": " + helpers);
                        }
                    }
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
