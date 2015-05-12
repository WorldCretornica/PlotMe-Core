package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.api.event.PlotTeleportMiddleEvent;

import java.util.Arrays;
import java.util.List;

public class CmdMiddle extends PlotCommand {

    public CmdMiddle(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "middle";
    }

    @Override
    public List getAliases() {
        return Arrays.asList("center", "mid");
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        if (args.length > 1) {
            throw new BadUsageException(getUsage());
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.USER_MIDDLE) || player.hasPermission(PermissionNames.ADMIN_MIDDLE_OTHER)) {
            if (manager.isPlotWorld(player)) {
                IWorld world = player.getWorld();
                PlotId id = manager.getPlotId(player);
                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                }
                Plot plot = manager.getPlotById(id, world);

                if (plot == null) {
                    player.sendMessage(C("MsgNoPlotFound"));

                } else if (plot.isAllowed(player.getUniqueId()) || player.hasPermission(PermissionNames.ADMIN_MIDDLE_OTHER)) {
                    Vector middleloc = manager.getPlotMiddle(world, plot.getId());
                    PlotTeleportMiddleEvent event = new PlotTeleportMiddleEvent(world, plot, player, middleloc);
                    serverBridge.getEventBus().post(event);
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
