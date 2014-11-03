package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.api.event.*;
import org.bukkit.Bukkit;

import java.util.Map;

public class BukkitEventFactory implements IEventFactory {

    @Override
    public InternalPlotCreateEvent callPlotCreatedEvent(PlotMe_Core plugin, IWorld world, String plotId, IPlayer creator) {
        PlotCreateEvent event = new PlotCreateEvent(plugin, world, plotId, creator);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotClearEvent callPlotClearEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer clearer) {
        PlotClearEvent event = new PlotClearEvent(plugin, world, plot, clearer);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotLoadEvent callPlotLoadedEvent(PlotMe_Core plugin, IWorld world, Plot plot) {
        PlotLoadEvent event = new PlotLoadEvent(plugin, world, plot);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotMoveEvent callPlotMoveEvent(PlotMe_Core plugin, IWorld worldFrom, IWorld worldTo, String idFrom, String idTo, IPlayer mover) {
        PlotMoveEvent event = new PlotMoveEvent(plugin, worldFrom, worldTo, idFrom, idTo, mover);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotResetEvent callPlotResetEvent(PlotMe_Core plugin, IWorld world, Plot plot, ICommandSender cs) {
        PlotResetEvent event = new PlotResetEvent(plugin, world, plot, cs);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotBidEvent callPlotBidEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer bidder, double bid) {
        PlotBidEvent event = new PlotBidEvent(plugin, world, plot, bidder, bid);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotAuctionEvent callPlotAuctionEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, double minimumbid) {
        PlotAuctionEvent event = new PlotAuctionEvent(plugin, world, plot, player, minimumbid);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotBiomeChangeEvent callPlotBiomeChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, IBiome biome) {
        PlotBiomeChangeEvent event = new PlotBiomeChangeEvent(plugin, world, plot, player, biome);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotBuyEvent callPlotBuyEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, double price) {
        PlotBuyEvent event = new PlotBuyEvent(plugin, world, plot, player, price);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotCommentEvent callPlotCommentEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer commenter, String comment) {
        PlotCommentEvent event = new PlotCommentEvent(plugin, world, plot, commenter, comment);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotWorldCreateEvent callPlotWorldCreateEvent(String worldname, Map<String, String> parameters) {
        PlotWorldCreateEvent event = new PlotWorldCreateEvent(worldname, parameters);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotDisposeEvent callPlotDisposeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer disposer) {
        PlotDisposeEvent event = new PlotDisposeEvent(plugin, world, plot, disposer);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotDoneChangeEvent callPlotDoneEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, boolean done) {
        PlotDoneChangeEvent event = new PlotDoneChangeEvent(plugin, world, plot, player, done);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotTeleportHomeEvent callPlotTeleportHomeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player) {
        PlotTeleportHomeEvent event = new PlotTeleportHomeEvent(plugin, world, plot, player);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotProtectChangeEvent callPlotProtectChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, boolean protect) {
        PlotProtectChangeEvent event = new PlotProtectChangeEvent(plugin, world, plot, player, protect);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotReloadEvent callPlotReloadEvent() {
        PlotReloadEvent event = new PlotReloadEvent();
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotAddAllowedEvent callPlotAddAllowedEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String allowed) {
        PlotAddAllowedEvent event = new PlotAddAllowedEvent(plugin, world, plot, player, allowed);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotRemoveAllowedEvent callPlotRemoveAllowedEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String allowed) {
        PlotRemoveAllowedEvent event = new PlotRemoveAllowedEvent(plugin, world, plot, player, allowed);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotAddDeniedEvent callPlotAddDeniedEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String denied) {
        PlotAddDeniedEvent event = new PlotAddDeniedEvent(plugin, world, plot, player, denied);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotRemoveDeniedEvent callPlotRemoveDeniedEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String denied) {
        PlotRemoveDeniedEvent event = new PlotRemoveDeniedEvent(plugin, world, plot, player, denied);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotSellChangeEvent callPlotSellChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer seller, double price, boolean soldToBank, boolean isForSale) {
        PlotSellChangeEvent event = new PlotSellChangeEvent(plugin, world, plot, seller, price, soldToBank, isForSale);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotOwnerChangeEvent callPlotOwnerChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String newowner) {
        PlotOwnerChangeEvent event = new PlotOwnerChangeEvent(plugin, world, plot, player, newowner);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotTeleportEvent callPlotTeleportEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, ILocation location, String PlotId) {
        PlotTeleportEvent event = new PlotTeleportEvent(plugin, world, plot, player, location, PlotId);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotTeleportEvent callPlotTeleportEvent(PlotMe_Core plugin, IWorld world, IPlayer player, ILocation location, String PlotId) {
        PlotTeleportEvent event = new PlotTeleportEvent(plugin, world, null, player, location, PlotId);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotWorldLoadEvent callPlotWorldLoadEvent(PlotMe_Core plugin, String worldname, int NbPlots) {
        PlotWorldLoadEvent event = new PlotWorldLoadEvent(worldname, NbPlots);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }
}
