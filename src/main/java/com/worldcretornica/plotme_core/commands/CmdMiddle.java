package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportMiddleEvent;

public class CmdMiddle extends PlotCommand implements CommandBase {

    public CmdMiddle(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "middle";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.USER_MIDDLE) || player.hasPermission(PermissionNames.ADMIN_MIDDLE_OTHER)) {
            if (manager.isPlotWorld(player)) {
                IWorld world = player.getWorld();
                PlotId id = manager.getPlotId(player);
                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                }
                Plot plot = manager.getPlotById(id, player);

                if (plot == null) {
                    player.sendMessage(C("MsgNoPlotFound"));

                } else if (plot.isAllowed(player.getUniqueId()) || player.hasPermission(PermissionNames.ADMIN_MIDDLE_OTHER)) {
                    ILocation middleloc = manager.getPlotMiddle(world, plot.getId());
                    InternalPlotTeleportMiddleEvent event =
                            serverBridge.getEventFactory().callPlotTeleportMiddleEvent(world, plot, player, middleloc);

                    if (!event.isCancelled()) {
                        player.setLocation(event.getMiddleLocation());
                    }
                } else {
                    player.sendMessage(C("MsgPermissionDenied"));
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
        return C("WordUsage") + ": /plotme middle";
    }
}
