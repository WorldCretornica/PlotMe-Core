package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.IEventFactory;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddAllowedEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddDeniedEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotAuctionEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotBidEvent;
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
import com.worldcretornica.plotme_core.api.event.InternalPlotReloadEvent;
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
    public InternalPlotCreateEvent callPlotCreatedEvent(PlotMe_Core plugin, IWorld world, PlotId id, IPlayer creator) {
        PlotCreateEvent event = new PlotCreateEvent(plugin, world, id, creator);
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
    public InternalPlotMoveEvent callPlotMoveEvent(PlotMe_Core plugin, IWorld world, PlotId idFrom, PlotId idTo, IPlayer mover) {
        PlotMoveEvent event = new PlotMoveEvent(plugin, world, idFrom, idTo, mover);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotResetEvent callPlotResetEvent(PlotMe_Core plugin, IWorld world, Plot plot, ICommandSender commandSender) {
        PlotResetEvent event = new PlotResetEvent(plugin, world, plot, commandSender);
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
    public InternalPlotAuctionEvent callPlotAuctionEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, double minimumBid) {
        PlotAuctionEvent event = new PlotAuctionEvent(plugin, world, plot, player, minimumBid);
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
    public InternalPlotWorldCreateEvent callPlotWorldCreateEvent(String worldName, Map<String, String> parameters) {
        PlotWorldCreateEvent event = new PlotWorldCreateEvent(worldName, parameters);
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
    public InternalPlotTeleportMiddleEvent callPlotTeleportMiddleEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player,
            ILocation middle) {
        PlotTeleportMiddleEvent event = new PlotTeleportMiddleEvent(plugin, world, plot, player, middle);
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
    public InternalPlotSellChangeEvent callPlotSellChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer seller, double price,
            boolean isForSale) {
        PlotSellChangeEvent event = new PlotSellChangeEvent(plugin, world, plot, seller, price, isForSale);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotOwnerChangeEvent callPlotOwnerChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String newOwner) {
        PlotOwnerChangeEvent event = new PlotOwnerChangeEvent(plugin, world, plot, player, newOwner);
        Bukkit.getPluginManager().callEvent(event);
        return event.getInternal();
    }

    @Override
    public InternalPlotTeleportEvent callPlotTeleportEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, ILocation location,
            String plotId) {
        PlotTeleportEvent event = new PlotTeleportEvent(plugin, world, plot, player, location, plotId);
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
