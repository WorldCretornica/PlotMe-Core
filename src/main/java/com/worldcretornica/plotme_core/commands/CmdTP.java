package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportEvent;

public class CmdTP extends PlotCommand {

    public CmdTP(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission("PlotMe.admin.tp")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player) || sob.getConfig().getBoolean("allowWorldTeleport")) {
                if (args.length == 2 || args.length == 3) {
                    String id = args[1];

                    IWorld w;

                    if (args.length == 3) {
                        String world = args[2];

                        w = sob.getWorld(world);

                        if (w == null) {
                            for (IWorld bworld : sob.getWorlds()) {
                                if (bworld.getName().startsWith(world)) {
                                    w = bworld;
                                    break;
                                }
                            }
                        }
                    } else if (!plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                        w = plugin.getPlotMeCoreManager().getFirstWorld();
                    } else {
                        w = player.getWorld();
                    }

                    if (!plugin.getPlotMeCoreManager().isPlotWorld(w)) {
                        player.sendMessage("§c" + C("MsgNoPlotworldFound"));
                    } else if (!plugin.getPlotMeCoreManager().isValidId(w, id)) {
                        if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                            player.sendMessage(C("WordUsage") + ": §c/plotme " + C("CommandTp") + " <ID> [" + C("WordWorld") + "] §r" + C("WordExample") + ": §c/plotme " + C("CommandTp") + " 5;-1 ");
                        } else {
                            player.sendMessage(C("WordUsage") + ": §c/plotme " + C("CommandTp") + " <ID> §r" + C("WordExample") + ": §c/plotme " + C("CommandTp") + " 5;-1 ");
                        }
                        return true;
                    } else {
                        ILocation loc = plugin.getPlotMeCoreManager().getPlotHome(w, id);

                        InternalPlotTeleportEvent event = sob.getEventFactory().callPlotTeleportEvent(plugin, w, player, loc, id);

                        if (!event.isCancelled()) {
                            player.teleport(loc);
                        }
                    }
                } else if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage(C("WordUsage") + ": §c/plotme " + C("CommandTp") + " <ID> [" + C("WordWorld") + "] §r" + C("WordExample") + ": §c/plotme " + C("CommandTp") + " 5;-1 ");
                } else {
                    player.sendMessage(C("WordUsage") + ": §c/plotme " + C("CommandTp") + " <ID> §r" + C("WordExample") + ": §c/plotme " + C("CommandTp") + " 5;-1 ");
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
