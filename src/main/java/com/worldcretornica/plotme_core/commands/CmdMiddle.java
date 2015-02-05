package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportMiddleEvent;

public class CmdMiddle extends PlotCommand {

    public CmdMiddle(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission(PermissionNames.USER_MIDDLE) || player.hasPermission(PermissionNames.ADMIN_MIDDLE_OTHER)) {
            if (manager.isPlotWorld(player)) {
                IWorld world = player.getWorld();
                
                Plot plot = manager.getPlotById(player);
                
                if (plot == null) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                    
                } else if (plot.isAllowed(player.getUniqueId()) || player.hasPermission(PermissionNames.ADMIN_MIDDLE_OTHER)) {
                    ILocation middleloc = manager.getPlotMiddle(world, plot.getId());
                    InternalPlotTeleportMiddleEvent event = serverBridge.getEventFactory().callPlotTeleportMiddleEvent(plugin, world, plot, player, middleloc);
                                    
                    if (!event.isCancelled()) {
                        player.setLocation(event.getMiddleLocation());
                    }
                } else {
                    player.sendMessage("§c" + C("MsgPermissionDenied"));
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
