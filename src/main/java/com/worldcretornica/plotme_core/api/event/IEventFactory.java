package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.CommandSender;
import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;

import java.util.Map;

public interface IEventFactory {

    InternalPlotCreateEvent callPlotCreatedEvent(PlotMe_Core plugin, World world, String plotId, Player creator);

    InternalPlotClearEvent callPlotClearEvent(PlotMe_Core plugin, World world, Plot plot, Player clearer);

    InternalPlotLoadEvent callPlotLoadedEvent(PlotMe_Core plugin, World world, Plot plot);

    InternalPlotMoveEvent callPlotMoveEvent(PlotMe_Core plugin, World world, String idFrom, String idTo, Player mover);

    InternalPlotResetEvent callPlotResetEvent(PlotMe_Core plugin, World world, Plot plot, CommandSender cs);

    InternalPlotBidEvent callPlotBidEvent(PlotMe_Core plugin, World world, Plot plot, Player bidder, double bid);

    InternalPlotAuctionEvent callPlotAuctionEvent(PlotMe_Core plugin, World world, Plot plot, Player player, double minimumbid);

    InternalPlotBiomeChangeEvent callPlotBiomeChangeEvent(PlotMe_Core plugin, World world, Plot plot, Player player, IBiome biome);

    InternalPlotBuyEvent callPlotBuyEvent(PlotMe_Core plugin, World world, Plot plot, Player player, double price);

    InternalPlotWorldCreateEvent callPlotWorldCreateEvent(String worldname, Map<String, String> parameters);

    InternalPlotDisposeEvent callPlotDisposeEvent(PlotMe_Core plugin, World world, Plot plot, Player disposer);

    InternalPlotDoneChangeEvent callPlotDoneEvent(PlotMe_Core plugin, World world, Plot plot, Player player, boolean done);

    InternalPlotTeleportHomeEvent callPlotTeleportHomeEvent(PlotMe_Core plugin, World world, Plot plot, Player player);

    InternalPlotProtectChangeEvent callPlotProtectChangeEvent(PlotMe_Core plugin, World world, Plot plot, Player player, boolean protect);

    InternalPlotReloadEvent callPlotReloadEvent();

    InternalPlotAddAllowedEvent callPlotAddAllowedEvent(PlotMe_Core plugin, World world, Plot plot, Player player, String allowed);

    InternalPlotRemoveAllowedEvent callPlotRemoveAllowedEvent(PlotMe_Core plugin, World world, Plot plot, Player player, String allowed);

    InternalPlotAddDeniedEvent callPlotAddDeniedEvent(PlotMe_Core plugin, World world, Plot plot, Player player, String denied);

    InternalPlotRemoveDeniedEvent callPlotRemoveDeniedEvent(PlotMe_Core plugin, World world, Plot plot, Player player, String denied);

    InternalPlotSellChangeEvent callPlotSellChangeEvent(PlotMe_Core plugin, World world, Plot plot, Player seller, double price, boolean isForSale);

    InternalPlotOwnerChangeEvent callPlotOwnerChangeEvent(PlotMe_Core plugin, World world, Plot plot, Player player, String newowner);

    InternalPlotTeleportEvent callPlotTeleportEvent(PlotMe_Core plugin, World world, Plot plot, Player player, Location location, String PlotId);

    InternalPlotWorldLoadEvent callPlotWorldLoadEvent(String worldname, int nbPlots);
}
