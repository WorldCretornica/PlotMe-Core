package com.worldcretornica.plotme_core.bukkit.event;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.event.InternalPlotWorldCreateEvent;
import com.worldcretornica.plotme_core.bukkit.api.*;

public class PlotWorldCreateEvent extends Event implements Cancellable {

    protected static final HandlerList handlers = new HandlerList();
    private InternalPlotWorldCreateEvent event;

    public PlotWorldCreateEvent(String worldname, CommandSender cs, Map<String, String> parameters) {
        event = new InternalPlotWorldCreateEvent(worldname, new BukkitCommandSender(cs), parameters);
    }

    public PlotWorldCreateEvent(String worldname, ICommandSender cs, Map<String, String> parameters) {
        event = new InternalPlotWorldCreateEvent(worldname, cs, parameters);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean canceled) {
        event.setCancelled(canceled);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getWorldName() {
        return event.getWorldName();
    }

    public CommandSender getCreator() {
        return ((BukkitCommandSender) event.getCreator()).getCommandSender();
    }

    public Map<String, String> getParameters() {
        return event.getParameters();
    }
    
    public InternalPlotWorldCreateEvent getInternal() {
        return event;
    }

}
