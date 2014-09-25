package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.event.PlotMeEventFactory;
import com.worldcretornica.plotme_core.event.PlotTeleportEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CmdTP extends PlotCommand {

    public CmdTP(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.admin.tp")) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(p) && !plugin.getConfig().getBoolean("allowWorldTeleport")) {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
            } else if (args.length == 2 || args.length == 3) {
	            String id = args[1];

	            World w;

	            if (args.length == 3) {
		            String world = args[2];

		            w = Bukkit.getWorld(world);

		            if (w == null) {
			            for (World bworld : Bukkit.getWorlds()) {
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

	            if (!plugin.getPlotMeCoreManager().isPlotWorld(w)) {
		            p.sendMessage(RED + C("MsgNoPlotworldFound"));
	            } else if (!plugin.getPlotMeCoreManager().isValidId(w, id)) {
		            if (plugin.getConfig().getBoolean("allowWorldTeleport")) {
			            p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandTp") + " <" + C("WordId") + "> [" + C("WordWorld") + "] " + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandTp") + " 5;-1 ");
		            } else {
			            p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandTp") + " <" + C("WordId") + "> " + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandTp") + " 5;-1 ");
		            }
		            return true;
	            } else {
		            Location loc = plugin.getPlotMeCoreManager().getPlotHome(w, id);

		            PlotTeleportEvent event = PlotMeEventFactory.callPlotTeleportEvent(plugin, w, p, loc, id);

		            if (!event.isCancelled()) {
			            p.teleport(loc);
		            }
	            }
            } else if (plugin.getConfig().getBoolean("allowWorldTeleport")) {
	            p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandTp") + " <" + C("WordId") + "> [" + C("WordWorld") + "] " + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandTp") + " 5;-1 ");
            } else {
	            p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandTp") + " <" + C("WordId") + "> " + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandTp") + " 5;-1 ");
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
