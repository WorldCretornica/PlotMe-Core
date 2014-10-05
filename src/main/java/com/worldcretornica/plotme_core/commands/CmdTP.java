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

    public boolean exec(IPlayer p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.admin.tp")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p) || sob.getConfig().getBoolean("allowWorldTeleport")) {
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
                    } else if (!plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                        w = plugin.getPlotMeCoreManager().getFirstWorld();
                    } else {
                        w = p.getWorld();
                    }

                    if (w == null || !plugin.getPlotMeCoreManager().isPlotWorld(w)) {
                        p.sendMessage(RED + C("MsgNoPlotworldFound"));
                    } else if (!plugin.getPlotMeCoreManager().isValidId(w, id)) {
                        if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                            p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandTp") + " <" + C("WordId") + "> [" + C("WordWorld") + "] " + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandTp") + " 5;-1 ");
                        } else {
                            p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandTp") + " <" + C("WordId") + "> " + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandTp") + " 5;-1 ");
                        }
                        return true;
                    } else {
                        ILocation loc = plugin.getPlotMeCoreManager().getPlotHome(w, id);

                        InternalPlotTeleportEvent event = sob.getEventFactory().callPlotTeleportEvent(plugin, w, p, loc, id);

                        if (!event.isCancelled()) {
                            p.teleport(loc);
                        }
                    }
                } else if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                    p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandTp") + " <" + C("WordId") + "> [" + C("WordWorld") + "] " + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandTp") + " 5;-1 ");
                } else {
                    p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandTp") + " <" + C("WordId") + "> " + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandTp") + " 5;-1 ");
                }
            } else {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
