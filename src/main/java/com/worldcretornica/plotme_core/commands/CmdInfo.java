package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.utils.NameFetcher;

import java.util.Collections;
import java.util.List;

public class CmdInfo extends PlotCommand {

    public CmdInfo(PlotMe_Core instance) {
        super(instance);
    }

    @Override
    public List getAliases() {
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
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                }
                player.sendMessage("Internal ID: " + plot.getInternalID());
                player.sendMessage("ID: " + plot + " " + C("InfoOwner") + ": " + plot.getOwner()
                        + " " + C("InfoBiome") + ": " + plot.getBiome());
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

                if (plot.getMembers().size() > 0) {
                    player.sendMessage(C("InfoAllowed") + ": " + plot.getMembers().keySet().toString());
                }

                if (plot.getDenied().size() > 0) {
                    if (plot.getDenied().contains("*")) {
                        player.sendMessage(C("InfoDenied") + ": " + plot.getDenied().toString());
                    }

                    NameFetcher nameFetcher = new NameFetcher(plot.getDenied());
                    player.sendMessage(C("InfoDenied") + ": " + nameFetcher.call().toString());
                }

                if (manager.isEconomyEnabled(world)) {
                    if (plot.isForSale()) {
                        player.sendMessage(C("InfoForSale") + ": " + (Math.round(plot.getPrice())));
                    } else {
                        player.sendMessage(C("InfoForSale") + ": " + C("WordNo"));
                    }
                }
                int bottomX = plot.getBottomX();
                int bottomZ = plot.getBottomZ();
                int topX = plot.getTopX();
                int topZ = plot.getTopZ();

                player.sendMessage(C("WordBottom") + ": " + bottomX + "," + bottomZ);
                player.sendMessage(C("WordTop") + ": " + topX + "," + topZ);

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
        return C("WordUsage") + ": /plotme info";
    }
}
