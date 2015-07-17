package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CmdInfo extends PlotCommand {

    public CmdInfo(PlotMe_Core instance) {
        super(instance);
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("i");
    }

    public String getName() {
        return "info";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(getUsage());
            return true;
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.USER_INFO)) {
            IWorld world = player.getWorld();
            if (manager.isPlotWorld(world)) {
                Plot plot = manager.getPlot(player);

                if (plot == null) {
                    player.sendMessage(C("NoPlotFound"));
                    return true;
                }
                player.sendMessage("Internal ID: " + plot.getInternalID());
                player.sendMessage(
                        "ID: " + plot.getId().getID() + " " + C("InfoOwner", serverBridge.getOfflinePlayer(plot.getOwnerId()).getName()) + " " + C
                                ("InfoBiome", plot
                                        .getBiome()));
                player.sendMessage("Likes: " + plot.getLikes());
                player.sendMessage("Created: " + plot.getCreatedDate());
                final String neverExpire = C("InfoExpire") + ": " + C("WordNever");
                if (plot.getExpiredDate() == null) {
                    if (plot.isFinished()) {
                        if (plot.isProtected()) {
                            player.sendMessage(neverExpire
                                    + " " + C("InfoFinished") + ": " + C("WordYes")
                                    + " " + C("InfoProtected") + ": " + C("WordYes"));
                        } else {
                            player.sendMessage(neverExpire
                                    + " " + C("InfoFinished") + ": " + C("WordYes")
                                    + " " + C("InfoProtected") + ": " + C("WordNo"));
                        }
                    } else {
                        if (plot.isProtected()) {
                            player.sendMessage(neverExpire
                                    + " " + C("InfoFinished") + ": " + C("WordNo")
                                    + " " + C("InfoProtected") + ": " + C("WordYes"));
                        } else {
                            player.sendMessage(neverExpire
                                    + " " + C("InfoFinished") + ": " + C("WordNo")
                                    + " " + C("InfoProtected") + ": " + C("WordNo"));
                        }
                    }
                } else if (plot.isProtected()) {
                    if (plot.isFinished()) {
                        player.sendMessage(neverExpire
                                + " " + C("InfoFinished") + ": " + C("WordYes")
                                + " " + C("InfoProtected") + ": " + C("WordYes"));
                    } else {
                        player.sendMessage(neverExpire
                                + " " + C("InfoFinished") + ": " + C("WordNo")
                                + " " + C("InfoProtected") + ": " + C("WordYes"));
                    }
                } else if (plot.isFinished()) {
                    player.sendMessage(C("InfoExpire") + ": " + plot.getExpiredDate()
                            + " " + C("InfoFinished") + ": " + C("WordYes")
                            + " " + C("InfoProtected") + ": " + C("WordNo"));
                } else {
                    player.sendMessage(C("InfoExpire") + ": " + plot.getExpiredDate()
                            + " " + C("InfoFinished") + ": " + C("WordNo")
                            + " " + C("InfoProtected") + ": " + C("WordNo"));
                }

                if (!plot.getMembers().isEmpty()) {
                    StringBuilder builder = new StringBuilder("Members: ");
                    if (!plot.getMembers().containsKey("*")) {
                        for (Map.Entry<String, Plot.AccessLevel> member : plot.getMembers().entrySet()) {
                            builder.append(plugin.getServerBridge().getOfflinePlayer(UUID.fromString(member.getKey())).getName()).append(" (")
                                    .append(member.getValue().toString()).append(")   ");
                        }
                    } else {
                        builder.append("*");
                    }
                    player.sendMessage(builder.toString());
                }

                if (!plot.getDenied().isEmpty()) {
                    StringBuilder builder = new StringBuilder(C("InfoDenied"));
                    builder.append(": ");
                    if (!plot.getDenied().contains("*")) {
                        for (String s : plot.getDenied()) {
                            builder.append(plugin.getServerBridge().getOfflinePlayer(UUID.fromString(s)).getName()).append("  ");
                        }
                    } else {
                        builder.append('*');
                    }

                    player.sendMessage(builder.toString());
                }

                if (manager.isEconomyEnabled(world)) {
                    if (plot.isForSale()) {
                        player.sendMessage(C("InfoForSale") + ": " + plot.getPrice());
                    } else {
                        player.sendMessage(C("InfoForSale") + ": " + C("WordNo"));
                    }
                }

                player.sendMessage(C("WordBottom") + ": " + plot.getPlotBottomLoc().toString());
                player.sendMessage(C("WordTop") + ": " + plot.getPlotTopLoc().toString());

            } else {
                player.sendMessage(C("NotPlotWorld"));
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("CmdInfoUsage");
    }
}
