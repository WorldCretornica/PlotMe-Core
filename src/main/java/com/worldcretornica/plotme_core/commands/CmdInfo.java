package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBiome;

public class CmdInfo extends PlotCommand {

    public CmdInfo(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission(PermissionNames.USER_INFO)) {
            IWorld world = player.getWorld();
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);

                if (id == null) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                    return true;
                }
                if (!manager.isPlotAvailable(id, world)) {
                    Plot plot = manager.getPlotById(id, world);

                    player.sendMessage("§aID: §b" + id + "§a " + C("InfoOwner") + ": §b" + plot.getOwner()
                            + "§a " + C("InfoBiome") + ": §b" + ((BukkitBiome) plot.getBiome()).getBiome().name());

                    if (plot.getExpiredDate() == null) {
                        if (plot.isFinished()) {
                            if (plot.isProtect()) {
                                player.sendMessage("§a" + C("InfoExpire") + ": §b" + C("WordNever")
                                        + "§a " + C("InfoFinished") + ": §b" + C("WordYes")
                                        + "§a " + C("InfoProtected") + ": §b" + C("WordYes"));
                            } else {
                                player.sendMessage("§a" + C("InfoExpire") + ": §b" + C("WordNever")
                                        + "§a " + C("InfoFinished") + ": §b" + C("WordYes")
                                        + "§a " + C("InfoProtected") + ": §b" + C("WordNo"));
                            }
                        } else if (plot.isProtect()) {
                            player.sendMessage("§a" + C("InfoExpire") + ": §b" + C("WordNever")
                                    + "§a " + C("InfoFinished") + ": §b" + C("WordNo")
                                    + "§a " + C("InfoProtected") + ": §b" + C("WordYes"));
                        } else {
                            player.sendMessage("§a" + C("InfoExpire") + ": §b" + C("WordNever")
                                    + "§a " + C("InfoFinished") + ": §b" + C("WordNo")
                                    + "§a " + C("InfoProtected") + ": §b" + C("WordNo"));
                        }
                    } else if (plot.isProtect()) {
                        if (plot.isFinished()) {
                            player.sendMessage("§a" + C("InfoExpire") + ": §b" + plot.getExpiredDate()
                                    + "§a " + C("InfoFinished") + ": §b" + C("WordYes")
                                    + "§a " + C("InfoProtected") + ": §b" + C("WordYes"));
                        } else {
                            player.sendMessage("§a" + C("InfoExpire") + ": §b" + plot.getExpiredDate()
                                    + "§a " + C("InfoFinished") + ": §b" + C("WordNo")
                                    + "§a " + C("InfoProtected") + ": §b" + C("WordYes"));
                        }
                    } else if (plot.isFinished()) {
                        player.sendMessage("§a" + C("InfoExpire") + ": §b" + plot.getExpiredDate()
                                + "§a " + C("InfoFinished") + ": §b" + C("WordYes")
                                + "§a " + C("InfoProtected") + ": §b" + C("WordNo"));
                    } else {
                        player.sendMessage("§a" + C("InfoExpire") + ": §b" + plot.getExpiredDate()
                                + "§a " + C("InfoFinished") + ": §b" + C("WordNo")
                                + "§a " + C("InfoProtected") + ": §b" + C("WordNo"));
                    }

                    if (plot.allowedcount() > 0) {
                        player.sendMessage("§a" + C("InfoHelpers") + ": §b" + plot.getAllowed());
                    }

                    if (plot.deniedcount() > 0) {
                        player.sendMessage("§a" + C("InfoDenied") + ": §b" + plot.getDenied());
                    }

                    if (manager.isEconomyEnabled(world)) {
                        if (plot.isForSale()) {
                            player.sendMessage("§a " + C("InfoForSale") + ": §b" + ("§b" + Math.round(plot.getPrice())));
                        } else {
                            player.sendMessage("§a " + C("InfoForSale") + ": §b" + C("WordNo"));
                        }
                    }
                    int bottomX = manager.bottomX(id, world);
                    int bottomZ = manager.bottomZ(id, world);
                    int topX = manager.topX(id, world);
                    int topZ = manager.topZ(id, world);

                    player.sendMessage("§b" + C("WordBottom") + ": §r" + bottomX + "§9,§r" + bottomZ);
                    player.sendMessage("§b" + C("WordTop") + ": §r" + topX + "§9,§r" + topZ);

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
