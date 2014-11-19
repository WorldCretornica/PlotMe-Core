package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
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

                    IWorld world;

                    if (args.length == 3) {

                        world = sob.getWorld(args[2]);

                        if (world == null) {
                            for (IWorld bworld : sob.getWorlds()) {
                                if (bworld.getName().startsWith(args[2])) {
                                    world = bworld;
                                    break;
                                }
                            }
                        }
                    } else if (!plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                        world = plugin.getPlotMeCoreManager().getFirstWorld();
                    } else {
                        world = player.getWorld();
                    }

                    if (!plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                        player.sendMessage("§c" + C("MsgNoPlotworldFound"));
                    } else if (!PlotMeCoreManager.isValidId(world, id)) {
                        if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                            player.sendMessage(C("WordUsage") + ": §c/plotme tp <ID> [" + C("WordWorld") + "] §r" + C("WordExample") + ": §c/plotme tp 5;-1 ");
                        } else {
                            player.sendMessage(C("WordUsage") + ": §c/plotme tp <ID> §r" + C("WordExample") + ": §c/plotme tp 5;-1 ");
                        }
                        return true;
                    } else {
                        ILocation loc = PlotMeCoreManager.getPlotHome(world, id);

                        InternalPlotTeleportEvent event = sob.getEventFactory().callPlotTeleportEvent(plugin, world, player, loc, id);

                        if (!event.isCancelled()) {
                            player.teleport(loc);
                        }
                    }
                } else if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage(C("WordUsage") + ": §c/plotme tp <ID> [" + C("WordWorld") + "] §r" + C("WordExample") + ": §c/plotme tp 5;-1 ");
                } else {
                    player.sendMessage(C("WordUsage") + ": §c/plotme tp <ID> §r" + C("WordExample") + ": §c/plotme tp 5;-1 ");
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
