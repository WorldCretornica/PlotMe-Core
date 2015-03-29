package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportEvent;

import java.util.Collections;
import java.util.List;

public class CmdTP extends PlotCommand {

    public CmdTP(PlotMe_Core instance) {
        super(instance);
    }

    @Override
    public List getAliases() {
        return Collections.singletonList("teleport");
    }

    public String getName() {
        return "tp";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_TP)) {
            if (manager.isPlotWorld(player) || plugin.getConfig().getBoolean("allowWorldTeleport")) {
                if (args.length == 2 || args.length == 3) {
                    IWorld world;
                    if (args.length == 3) {

                        world = serverBridge.getWorld(args[2]);

                        if (world == null) {
                            player.sendMessage(C("MsgNoPlotworldFound"));
                            return true;
                        }
                    } else if (manager.isPlotWorld(player)) {
                        world = player.getWorld();
                    } else {
                        world = manager.getFirstWorld();
                    }

                    if (PlotId.isValidID(args[1])) {
                        PlotId id2 = new PlotId(args[1]);
                        if (!manager.isPlotWorld(world)) {
                            player.sendMessage(C("MsgNoPlotworldFound"));
                        } else {
                            ILocation location = manager.getPlotHome(world, id2);
                            Plot plot = manager.getPlotById(id2, world);
                            InternalPlotTeleportEvent event =
                                    serverBridge.getEventFactory().callPlotTeleportEvent(world, plot, player, location, id2);

                            if (!event.isCancelled()) {
                                player.setLocation(location);
                            }
                        }
                    }
                } else {
                    player.sendMessage(getUsage());
                    return true;
                }
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
                return true;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("CmdTeleportUsage");
    }

}
