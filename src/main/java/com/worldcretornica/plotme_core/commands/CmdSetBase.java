package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.entity.Player;

public class CmdSetBase extends PlotCommand {

    public CmdSetBase(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player p, String[] args) {
        if (!plugin.cPerms(p, "PlotMe.admin.setbase")) {
            return true;
        }
        if (!plugin.getPlotMeCoreManager().isPlotWorld(p)) {
            p.sendMessage(RED + C("MsgNotPlotWorld"));
            return true;
        }
        String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());
        if (id.isEmpty()) {
            p.sendMessage(RED + C("MsgNoPlotFound"));
        }
        if (args.length < 2 || args[1].isEmpty()) {
            p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandSetBase") + " <" + C("WordHeight") + ">");
            return true;
        }
        int base;
        try {
            base = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            p.sendMessage(args[1] + " " + C("ErrNotANumber"));
            return true;
        }
        if (base > 256) {
            base = 256;
        } else if (base < 0) {
            base = 0;
        }
        Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);
        plot.setBaseY(base);
        plot.updateField("baseY", base);
        p.sendMessage(C("MsgBaseChangedTo") + " " + base);
        return true;
    }
}
