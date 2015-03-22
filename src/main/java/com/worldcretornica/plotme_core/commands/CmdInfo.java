package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class CmdInfo extends PlotCommand implements CommandBase {

    public CmdInfo(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "info";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.USER_INFO)) {
            IWorld world = player.getWorld();
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);

                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                }
                if (!manager.isPlotAvailable(id, world)) {
                    Plot plot = manager.getPlotById(id, world);

                    player.sendMessage("Internal ID: " + plot.getInternalID());
                    player.sendMessage("ID: " + id + " " + C("InfoOwner") + ": " + plot.getOwner()
                            + " " + C("InfoBiome") + ": " + plot.getBiome());

                    if (plot.getExpiredDate() == null) {
                        if (plot.isFinished()) {
                            if (plot.isProtect()) {
                                player.sendMessage(C("InfoExpire") + ": " + C("WordNever")
                                        + " " + C("InfoFinished") + ": " + C("WordYes")
                                        + " " + C("InfoProtected") + ": " + C("WordYes"));
                            } else {
                                player.sendMessage(C("InfoExpire") + ": " + C("WordNever")
                                        + " " + C("InfoFinished") + ": " + C("WordYes")
                                        + " " + C("InfoProtected") + ": " + C("WordNo"));
                            }
                        } else if (plot.isProtect()) {
                            player.sendMessage(C("InfoExpire") + ": " + C("WordNever")
                                    + " " + C("InfoFinished") + ": " + C("WordNo")
                                    + " " + C("InfoProtected") + ": " + C("WordYes"));
                        } else {
                            player.sendMessage(C("InfoExpire") + ": " + C("WordNever")
                                    + " " + C("InfoFinished") + ": " + C("WordNo")
                                    + " " + C("InfoProtected") + ": " + C("WordNo"));
                        }
                    } else if (plot.isProtect()) {
                        if (plot.isFinished()) {
                            player.sendMessage(C("InfoExpire") + ": " + plot.getExpiredDate()
                                    + " " + C("InfoFinished") + ": " + C("WordYes")
                                    + " " + C("InfoProtected") + ": " + C("WordYes"));
                        } else {
                            player.sendMessage(C("InfoExpire") + ": " + plot.getExpiredDate()
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

                    if (plot.allowed().size() > 0) {
                        player.sendMessage(C("InfoHelpers") + ": " + plot.getAllowed());
                    }

                    if (plot.denied().size() > 0) {
                        player.sendMessage(C("InfoDenied") + ": " + plot.getDenied());
                    }

                    if (manager.isEconomyEnabled(world)) {
                        if (plot.isForSale()) {
                            player.sendMessage(C("InfoForSale") + ": " + (Math.round(plot.getPrice())));
                        } else {
                            player.sendMessage(C("InfoForSale") + ": " + C("WordNo"));
                        }
                    }
                    int bottomX = manager.bottomX(id, world);
                    int bottomZ = manager.bottomZ(id, world);
                    int topX = manager.topX(id, world);
                    int topZ = manager.topZ(id, world);

                    player.sendMessage(C("WordBottom") + ": " + bottomX + "§9," + bottomZ);
                    player.sendMessage(C("WordTop") + ": " + topX + "§9," + topZ);

                } else {
                    player.sendMessage(C("MsgThisPlot") + " (" + id + ") " + C("MsgHasNoOwner"));
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
        return C("WordUsage") + ": /plotme info";
    }
}
