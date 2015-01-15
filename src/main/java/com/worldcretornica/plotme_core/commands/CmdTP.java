package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
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
        if (player.hasPermission(PermissionNames.ADMIN_TP)) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player) || serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                if (args.length == 2 || args.length == 3) {
                    String id = args[1];

                    IWorld world;

                    if (args.length == 3) {

                        world = serverBridge.getWorld(args[2]);

                        if (world == null) {
                            player.sendMessage("§c" + C("MsgNoPlotworldFound"));
                            return true;
                        }
                    } else if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                        world = player.getWorld();
                    } else {
                        world = plugin.getPlotMeCoreManager().getFirstWorld();
                    }

                    if (!plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                        player.sendMessage("§c" + C("MsgNoPlotworldFound"));
                    } else if (!PlotMeCoreManager.isValidId(world, id)) {
                        if (serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                            player.sendMessage(
                                    C("WordUsage") + ": §c/plotme tp <ID> [" + C("WordWorld") + "] §r" + C("WordExample") + ": §c/plotme tp 5;-1 ");
                        } else {
                            player.sendMessage(C("WordUsage") + ": §c/plotme tp <ID> §r" + C("WordExample") + ": §c/plotme tp 5;-1 ");
                        }
                        return true;
                    } else {
                        ILocation location = PlotMeCoreManager.getPlotHome(world, id);
                        Plot plot = plugin.getPlotMeCoreManager().getPlotById(id, world);
                        InternalPlotTeleportEvent
                                event =
                                serverBridge.getEventFactory().callPlotTeleportEvent(plugin, world, plot, player, location, id);

                        if (!event.isCancelled()) {
                            player.setLocation(location);
                        }
                    }
                } else if (serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage(
                            C("WordUsage") + ": §c/plotme tp <ID> [" + C("WordWorld") + "] §r" + C("WordExample") + ": §c/plotme tp 5;-1 ");
                } else {
                    player.sendMessage(C("WordUsage") + ": §c/plotme tp <ID> §r" + C("WordExample") + ": §c/plotme tp 5;-1 ");
                }
            } else {
                player.sendMessage("§c" + C("MsgNotPlotWorld"));
                return true;
            }
        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
