package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;

import java.util.Map;

public interface IEventFactory {

    public InternalPlotCreateEvent callPlotCreatedEvent(PlotMe_Core plugin, IWorld world, String plotId, IPlayer creator);

    public InternalPlotClearEvent callPlotClearEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer clearer);

    public InternalPlotLoadEvent callPlotLoadedEvent(PlotMe_Core plugin, IWorld iWorld, Plot plot);

    public InternalPlotMoveEvent callPlotMoveEvent(PlotMe_Core plugin, IWorld worldFrom, IWorld worldTo, String idFrom, String idTo, IPlayer mover);

    public InternalPlotResetEvent callPlotResetEvent(PlotMe_Core plugin, IWorld world, Plot plot, ICommandSender cs);

    public InternalPlotBidEvent callPlotBidEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer bidder, double bid);

    public InternalPlotAuctionEvent callPlotAuctionEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, double minimumbid);

    public InternalPlotBiomeChangeEvent callPlotBiomeChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, IBiome biome);

    public InternalPlotBuyEvent callPlotBuyEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, double price);

    public InternalPlotCommentEvent callPlotCommentEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer commenter, String comment);

    public InternalPlotWorldCreateEvent callPlotWorldCreateEvent(String worldname, ICommandSender cs, Map<String, String> parameters);

    public InternalPlotDisposeEvent callPlotDisposeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer disposer);

    public InternalPlotDoneChangeEvent callPlotDoneEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, boolean done);

    public InternalPlotTeleportHomeEvent callPlotTeleportHomeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player);

    public InternalPlotProtectChangeEvent callPlotProtectChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, boolean protect);

    public InternalPlotReloadEvent callPlotReloadEvent();

    public InternalPlotAddAllowedEvent callPlotAddAllowedEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String allowed);

    public InternalPlotRemoveAllowedEvent callPlotRemoveAllowedEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String allowed);

    public InternalPlotAddDeniedEvent callPlotAddDeniedEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String denied);

    public InternalPlotRemoveDeniedEvent callPlotRemoveDeniedEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String denied);

    public InternalPlotSellChangeEvent callPlotSellChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer seller, double price, boolean soldToBank, boolean isForSale);

    public InternalPlotOwnerChangeEvent callPlotOwnerChangeEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, String newowner);

    public InternalPlotTeleportEvent callPlotTeleportEvent(PlotMe_Core plugin, IWorld world, Plot plot, IPlayer player, ILocation location, String PlotId);

    public InternalPlotTeleportEvent callPlotTeleportEvent(PlotMe_Core plugin, IWorld world, IPlayer player, ILocation location, String PlotId);

    public InternalPlotWorldLoadEvent callPlotWorldLoadEvent(PlotMe_Core plugin, String worldname, int NbPlots);
}
