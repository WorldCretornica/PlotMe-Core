package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.sql.Date;
import java.util.Calendar;
import java.util.UUID;

public class CmdPlotList extends PlotCommand {

    //todo rewrite this command
    public CmdPlotList(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "list";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.USER_LIST)) {
            if (manager.isPlotWorld(player)) {
                String name;
                UUID uuid;

                if (args.length == 2) {
                    name = args[1];
                    uuid = null;
                    player.sendMessage(C("MsgListOfPlotsWhere") + " " + name + " " + C("MsgCanBuild"));
                } else {
                    name = player.getName();
                    uuid = player.getUniqueId();
                    player.sendMessage(C("MsgListOfPlotsWhereYou"));
                }

                String oldWorld = "";

                // Get plots of that player
                for (Plot plot : plugin.getSqlManager().getPlayerPlots(uuid)) {
                    IWorld world = serverBridge.getWorld(plot.getWorld());
                    if (world != null) {
                        manager.getMap(world).addPlot(plot.getId(), plot);
                    }

                    StringBuilder addition = new StringBuilder();

                    // Display worlds
                    if (!oldWorld.equalsIgnoreCase(plot.getWorld())) {
                        oldWorld = plot.getWorld();
                        player.sendMessage("World: " + plot.getWorld());
                    }

                    // Is it expired?
                    if (plot.getExpiredDate() != null) {
                        Date expiredDate = plot.getExpiredDate();

                        if (expiredDate.before(Calendar.getInstance().getTime())) {
                            addition.append(" @" + plot.getExpiredDate());
                        } else {
                            addition.append(" @" + plot.getExpiredDate());
                        }
                    }

                    // Is it for sale?
                    if (plot.isForSale()) {
                        addition.append(C("WordSell") + ": " + Math.round(plot.getPrice()));
                    }

                    // Is the plot owner the name?

                    if (plot.getOwner().equals(name)) {
                        if (plot.getMembers().size() == 0) {
                            // Is the name the current player too?
                            if (name.equals(player.getName())) {
                                player.sendMessage(plot.getId() + " -> " + C("WordYours") + addition);
                            } else {
                                player.sendMessage(plot.getId() + " -> " + plot.getOwner() + addition);
                            }
                        } else if (plot.getOwner().equals(player.getName())) {
                            player.sendMessage(plot.getId() + " -> " + C("WordYours") + addition + ", " + C("WordHelpers") + ": " + plot
                                    .getMembers().toString()); //todo fix this to work with allowed and trusted
                        } else {
                            player.sendMessage(plot.getId() + " -> " + plot.getOwner() + addition + ", " + C("WordHelpers") + ": " + plot
                                    .getMembers().toString()); //todo fix this to work with allowed and trusted
                        }

                        // Is the name allowed to build there?
                    } else if (plot.isAllowedConsulting(name)) {
                        StringBuilder helpers = new StringBuilder();
                        for (String allowed : plot.getMembers().keySet()) {
                            if (player.getName().equals(allowed)) {
                                if (name.equals(player.getName())) {
                                    helpers.append("You").append(", ");
                                } else {
                                    helpers.append(args[1]).append(", ");
                                }
                            } else {
                                helpers.append(allowed).append(", ");
                            }
                        }
                        if (helpers.length() > 2) {
                            helpers.delete(helpers.length() - 2, helpers.length());
                        }

                        if (plot.getOwner().equals(player.getName())) {
                            player.sendMessage(plot.getId() + " -> " + C("WordYours") + addition + ", " + C("WordHelpers") + ": " + helpers);
                        } else {
                            player.sendMessage(plot.getId() + " -> " + plot.getOwner() + C("WordPossessive") + addition + ", " + C("WordHelpers")
                                            + ": " + helpers);
                        }
                    }

                }
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme list <" + C("WordPlayer") + ">";
    }
}
