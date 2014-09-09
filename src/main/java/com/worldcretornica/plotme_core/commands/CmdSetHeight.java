package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.entity.Player;

public class CmdSetHeight extends PlotCommand {

    public CmdSetHeight(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player p, String[] args) {
        if (!plugin.cPerms(p, "PlotMe.admin.setheight")) {
            return true;
        }
        if (!plugin.getPlotMeCoreManager().isPlotWorld(p)) {
            p.sendMessage(RED + C("MsgNotPlotWorld"));
            return true;
        }
        String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());
        if (id.equals("")) {
            p.sendMessage(RED + C("MsgNoPlotFound"));
        }
        if (args.length < 2 || args[1].equals("")) {
            p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandSetHeight") + " <" + C("WordHeight") + ">");
            return true;
        }
        int height;
        try {
            height = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            p.sendMessage(args[1] + " " + C("ErrNotANumber"));
            return true;
        }
        if (height > 256) {
            height = 256;
        } else if (height < 0) {
            height = 0;
        }
        Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);
        plot.setHeight(height);
        plot.updateField("height", height);
        p.sendMessage(C("MsgHeightChangedTo") + " " + height);
        return true;
    }

    public boolean add(Player p, String[] args) {
        String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());
        if (id.equals("")) {
            p.sendMessage(RED + C("MsgNoPlotFound"));
        }
        Integer height;
        try {
            height = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            p.sendMessage(args[1] + " " + C("ErrNotANumber"));
            return true;
        }
        Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);
        height += plot.getHeight();
        args[1] = height.toString();
        return exec(p, args);
    }

    public boolean subtract(Player p, String[] args) {
        args[1] = "-" + args[1];
        return add(p, args);
    }
}
