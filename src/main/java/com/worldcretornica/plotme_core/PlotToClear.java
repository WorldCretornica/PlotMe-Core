package com.worldcretornica.plotme_core;

import org.bukkit.command.CommandSender;

public class PlotToClear {

    private String world;
    private String plotid;
    private CommandSender commandsender; //TODO Make it work if player relogs
    private ClearReason reason;

    public PlotToClear(String w, String id, CommandSender cs, ClearReason r) {
        setWorld(w);
        setPlotId(id);
        setCommandSender(cs);
        setReason(r);
    }

    public final String getWorld() {
        return world;
    }

    public final void setWorld(String world) {
        this.world = world;
    }

    public final String getPlotId() {
        return plotid;
    }

    public final void setPlotId(String plotid) {
        this.plotid = plotid;
    }

    public final CommandSender getCommandSender() {
        return commandsender;
    }

    public final void setCommandSender(CommandSender commandsender) {
        this.commandsender = commandsender;
    }

    public final ClearReason getReason() {
        return reason;
    }

    public final void setReason(ClearReason reason) {
        this.reason = reason;
    }
}
