package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Location;
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
    public List<String> getAliases() {
        return Arrays.asList("center", "mid");
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(getUsage());
            return true;
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.USER_MIDDLE) || player.hasPermission(PermissionNames.ADMIN_MIDDLE_OTHER)) {
            if (manager.isPlotWorld(player)) {
                IWorld world = player.getWorld();
                Plot plot = manager.getPlot(player);
                if (plot == null) {
                    player.sendMessage(C("NoPlotFound"));
                    return true;
                }
                if (plot.isMember(player.getUniqueId()).isPresent() || player.hasPermission(PermissionNames.ADMIN_MIDDLE_OTHER)) {
                    Vector middleloc = manager.getPlotMiddle(world, plot.getId());
                    Location location = new Location(world, middleloc);
                    PlotTeleportMiddleEvent event = new PlotTeleportMiddleEvent(plot, player, location);
                    plugin.getEventBus().post(event);
                    if (!event.isCancelled()) {
                        player.setLocation(event.getMiddleLocation());
                    }
                }
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
        return C("CmdMiddleUsage");
    }
}
