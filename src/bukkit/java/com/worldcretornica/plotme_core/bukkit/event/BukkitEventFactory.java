package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.IEventFactory;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddAllowedEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddDeniedEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotBiomeChangeEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotBuyEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotClearEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotCreateEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotDisposeEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotDoneChangeEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotLoadEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotMoveEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotOwnerChangeEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotProtectChangeEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotRemoveAllowedEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotRemoveDeniedEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotResetEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotSellChangeEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportHomeEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportMiddleEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotWorldCreateEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotWorldLoadEvent;
import org.bukkit.Bukkit;

import java.util.Map;

public class BukkitEventFactory implements IEventFactory {

    @Override
    public InternalPlotCreateEvent callPlotCreatedEvent(IWorld world, PlotId id, IPlayer creator) {
        PlotCreateEvent event = new PlotCreateEvent(world, id, creator);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotClearEvent callPlotClearEvent(IWorld world, Plot plot, IPlayer clearer) {
        PlotClearEvent event = new PlotClearEvent(world, plot, clearer);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotLoadEvent callPlotLoadedEvent(IWorld world, Plot plot) {
        PlotLoadEvent event = new PlotLoadEvent(world, plot);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotMoveEvent callPlotMoveEvent(IWorld world, PlotId idFrom, PlotId idTo, IPlayer mover) {
        PlotMoveEvent event = new PlotMoveEvent(world, idFrom, idTo, mover);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotResetEvent callPlotResetEvent(IWorld world, Plot plot, ICommandSender commandSender) {
        PlotResetEvent event = new PlotResetEvent(world, plot, commandSender);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotBiomeChangeEvent callPlotBiomeChangeEvent(IWorld world, Plot plot, IPlayer player, String biome) {
        PlotBiomeChangeEvent event = new PlotBiomeChangeEvent(world, plot, player, biome);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotBuyEvent callPlotBuyEvent(IWorld world, Plot plot, IPlayer player, double price) {
        PlotBuyEvent event = new PlotBuyEvent(world, plot, player, price);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotWorldCreateEvent callPlotWorldCreateEvent(String worldName, Map<String, String> parameters) {
        PlotWorldCreateEvent event = new PlotWorldCreateEvent(worldName, parameters);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotDisposeEvent callPlotDisposeEvent(IWorld world, Plot plot, IPlayer disposer) {
        PlotDisposeEvent event = new PlotDisposeEvent(world, plot, disposer);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotDoneChangeEvent callPlotDoneEvent(IWorld world, Plot plot, IPlayer player, boolean done) {
        PlotDoneChangeEvent event = new PlotDoneChangeEvent(world, plot, player, done);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotTeleportHomeEvent callPlotTeleportHomeEvent(IWorld world, Plot plot, IPlayer player, ILocation location) {
        PlotTeleportHomeEvent event = new PlotTeleportHomeEvent(world, plot, player, location);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotTeleportMiddleEvent callPlotTeleportMiddleEvent(IWorld world, Plot plot, IPlayer player,
            ILocation middle) {
        PlotTeleportMiddleEvent event = new PlotTeleportMiddleEvent(world, plot, player, middle);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotProtectChangeEvent callPlotProtectChangeEvent(IWorld world, Plot plot, IPlayer player, boolean protect) {
        PlotProtectChangeEvent event = new PlotProtectChangeEvent(world, plot, player, protect);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotAddAllowedEvent callPlotAddAllowedEvent(IWorld world, Plot plot, IPlayer player, String allowed) {
        PlotAddAllowedEvent event = new PlotAddAllowedEvent(world, plot, player, allowed);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotRemoveAllowedEvent callPlotRemoveAllowedEvent(IWorld world, Plot plot, IPlayer player, String allowed) {
        PlotRemoveAllowedEvent event = new PlotRemoveAllowedEvent(world, plot, player, allowed);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotAddDeniedEvent callPlotAddDeniedEvent(IWorld world, Plot plot, IPlayer player, String denied) {
        PlotAddDeniedEvent event = new PlotAddDeniedEvent(world, plot, player, denied);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotRemoveDeniedEvent callPlotRemoveDeniedEvent(IWorld world, Plot plot, IPlayer player, String denied) {
        PlotRemoveDeniedEvent event = new PlotRemoveDeniedEvent(world, plot, player, denied);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotSellChangeEvent callPlotSellChangeEvent(IWorld world, Plot plot, IPlayer seller, double price,
            boolean isForSale) {
        PlotSellChangeEvent event = new PlotSellChangeEvent(world, plot, seller, price, isForSale);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotOwnerChangeEvent callPlotOwnerChangeEvent(IWorld world, Plot plot, IPlayer player, String newOwner) {
        PlotOwnerChangeEvent event = new PlotOwnerChangeEvent(world, plot, player, newOwner);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotTeleportEvent callPlotTeleportEvent(IWorld world, Plot plot, IPlayer player, ILocation location,
            PlotId plotId) {
        PlotTeleportEvent event = new PlotTeleportEvent(world, plot, player, location, plotId);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotWorldLoadEvent callPlotWorldLoadEvent(String worldName, int nbPlots) {
        PlotWorldLoadEvent event = new PlotWorldLoadEvent(worldName, nbPlots);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }
}
