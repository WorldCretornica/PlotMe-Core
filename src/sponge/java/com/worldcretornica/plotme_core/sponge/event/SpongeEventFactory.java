package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.IBiome;
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

import java.util.Map;

public class SpongeEventFactory implements IEventFactory {

    @Override
    public InternalPlotCreateEvent callPlotCreatedEvent(IWorld world, PlotId id, IPlayer creator) {
        return null;
    }

    @Override
    public InternalPlotClearEvent callPlotClearEvent(IWorld world, Plot plot, IPlayer clearer) {
        return null;
    }

    @Override
    public InternalPlotLoadEvent callPlotLoadedEvent(IWorld world, Plot plot) {
        return null;
    }

    @Override
    public InternalPlotMoveEvent callPlotMoveEvent(IWorld world, PlotId idFrom, PlotId idTo, IPlayer mover) {
        return null;
    }

    @Override
    public InternalPlotResetEvent callPlotResetEvent(IWorld world, Plot plot, ICommandSender commandSender) {
        return null;
    }

    @Override
    public InternalPlotBiomeChangeEvent callPlotBiomeChangeEvent(IWorld world, Plot plot, IPlayer player, IBiome biome) {
        return null;
    }

    @Override
    public InternalPlotBuyEvent callPlotBuyEvent(IWorld world, Plot plot, IPlayer player, double price) {
        return null;
    }

    @Override
    public InternalPlotWorldCreateEvent callPlotWorldCreateEvent(String worldName, Map<String, String> parameters) {
        return null;
    }

    @Override
    public InternalPlotDisposeEvent callPlotDisposeEvent(IWorld world, Plot plot, IPlayer disposer) {
        return null;
    }

    @Override
    public InternalPlotDoneChangeEvent callPlotDoneEvent(IWorld world, Plot plot, IPlayer player, boolean done) {
        return null;
    }

    @Override
    public InternalPlotTeleportHomeEvent callPlotTeleportHomeEvent(IWorld world, Plot plot, IPlayer player, ILocation location) {
        return null;
    }

    @Override
    public InternalPlotTeleportMiddleEvent callPlotTeleportMiddleEvent(IWorld world, Plot plot, IPlayer player, ILocation location) {
        return null;
    }

    @Override
    public InternalPlotProtectChangeEvent callPlotProtectChangeEvent(IWorld world, Plot plot, IPlayer player, boolean protect) {
        return null;
    }

    @Override
    public InternalPlotReloadEvent callPlotReloadEvent() {
        return null;
    }

    @Override
    public InternalPlotAddAllowedEvent callPlotAddAllowedEvent(IWorld world, Plot plot, IPlayer player, String allowed) {
        return null;
    }

    @Override
    public InternalPlotRemoveAllowedEvent callPlotRemoveAllowedEvent(IWorld world, Plot plot, IPlayer player, String allowed) {
        return null;
    }

    @Override
    public InternalPlotAddDeniedEvent callPlotAddDeniedEvent(IWorld world, Plot plot, IPlayer player, String denied) {
        return null;
    }

    @Override
    public InternalPlotRemoveDeniedEvent callPlotRemoveDeniedEvent(IWorld world, Plot plot, IPlayer player, String denied) {
        return null;
    }

    @Override
    public InternalPlotSellChangeEvent callPlotSellChangeEvent(IWorld world, Plot plot, IPlayer seller, double price, boolean isForSale) {
        return null;
    }

    @Override
    public InternalPlotOwnerChangeEvent callPlotOwnerChangeEvent(IWorld world, Plot plot, IPlayer player, String newOwner) {
        return null;
    }

    @Override
    public InternalPlotTeleportEvent callPlotTeleportEvent(IWorld world, Plot plot, IPlayer player, ILocation location, PlotId PlotId) {
        return null;
    }

    @Override
    public InternalPlotWorldLoadEvent callPlotWorldLoadEvent(String worldName, int nbPlots) {
        return null;
    }
}
