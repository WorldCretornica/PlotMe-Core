package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;

import java.util.Map;

public interface IEventFactory {

    InternalPlotCreateEvent callPlotCreatedEvent(PlotMe_Core plugin, IWorld world, String plotId, IPlayer creator);

    InternalPlotClearEvent callPlotClearEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer clearer);

    InternalPlotLoadEvent callPlotLoadedEvent(PlotMe_Core plugin, IWorld world, Plot plot);

    InternalPlotMoveEvent callPlotMoveEvent(PlotMe_Core plugin, IWorld worldFrom, IWorld worldTo, String idFrom, String idTo, IPlayer mover);

    InternalPlotResetEvent callPlotResetEvent(PlotMe_Core plugin, IWorld world, Plot plot, ICommandSender cs);

    InternalPlotBidEvent callPlotBidEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer bidder, double bid);

    InternalPlotAuctionEvent callPlotAuctionEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, double minimumbid);

    InternalPlotBiomeChangeEvent callPlotBiomeChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, IBiome biome);

    InternalPlotBuyEvent callPlotBuyEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, double price);

    InternalPlotWorldCreateEvent callPlotWorldCreateEvent(String worldname, Map<String, String> parameters);

    InternalPlotDisposeEvent callPlotDisposeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer disposer);

    InternalPlotDoneChangeEvent callPlotDoneEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, boolean done);

    InternalPlotTeleportHomeEvent callPlotTeleportHomeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player);

    InternalPlotProtectChangeEvent callPlotProtectChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, boolean protect);

    InternalPlotReloadEvent callPlotReloadEvent();

    InternalPlotAddAllowedEvent callPlotAddAllowedEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String allowed);

    InternalPlotRemoveAllowedEvent callPlotRemoveAllowedEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String allowed);

    InternalPlotAddDeniedEvent callPlotAddDeniedEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String denied);

    InternalPlotRemoveDeniedEvent callPlotRemoveDeniedEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String denied);

    InternalPlotSellChangeEvent callPlotSellChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer seller, double price, boolean soldToBank, boolean isForSale);

    InternalPlotOwnerChangeEvent callPlotOwnerChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String newowner);

    InternalPlotTeleportEvent callPlotTeleportEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, ILocation location, String plotId);

    InternalPlotTeleportEvent callPlotTeleportEvent(PlotMe_Core plugin, IWorld world, IPlayer player, ILocation location, String PlotId);

    InternalPlotWorldLoadEvent callPlotWorldLoadEvent(String worldname, int nbPlots);
}
