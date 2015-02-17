package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.util.Map;

public interface IEventFactory {

    InternalPlotCreateEvent callPlotCreatedEvent(IWorld world, PlotId id, IPlayer creator);

    InternalPlotClearEvent callPlotClearEvent(IWorld world, Plot plot, IPlayer clearer);

    InternalPlotLoadEvent callPlotLoadedEvent(IWorld world, Plot plot);

    InternalPlotMoveEvent callPlotMoveEvent(IWorld world, PlotId idFrom, PlotId idTo, IPlayer mover);

    InternalPlotResetEvent callPlotResetEvent(IWorld world, Plot plot, ICommandSender commandSender);

    InternalPlotBiomeChangeEvent callPlotBiomeChangeEvent(IWorld world, Plot plot, IPlayer player, IBiome biome);

    InternalPlotBuyEvent callPlotBuyEvent(IWorld world, Plot plot, IPlayer player, double price);

    InternalPlotWorldCreateEvent callPlotWorldCreateEvent(String worldName, Map<String, String> parameters);

    InternalPlotDisposeEvent callPlotDisposeEvent(IWorld world, Plot plot, IPlayer disposer);

    InternalPlotDoneChangeEvent callPlotDoneEvent(IWorld world, Plot plot, IPlayer player, boolean done);

    InternalPlotTeleportHomeEvent callPlotTeleportHomeEvent(IWorld world, Plot plot, IPlayer player);

    InternalPlotTeleportMiddleEvent callPlotTeleportMiddleEvent(IWorld world, Plot plot, IPlayer player, ILocation location);

    InternalPlotProtectChangeEvent callPlotProtectChangeEvent(IWorld world, Plot plot, IPlayer player, boolean protect);

    InternalPlotReloadEvent callPlotReloadEvent();

    InternalPlotAddAllowedEvent callPlotAddAllowedEvent(IWorld world, Plot plot, IPlayer player, String allowed);

    InternalPlotRemoveAllowedEvent callPlotRemoveAllowedEvent(IWorld world, Plot plot, IPlayer player, String allowed);

    InternalPlotAddDeniedEvent callPlotAddDeniedEvent(IWorld world, Plot plot, IPlayer player, String denied);

    InternalPlotRemoveDeniedEvent callPlotRemoveDeniedEvent(IWorld world, Plot plot, IPlayer player, String denied);

    InternalPlotSellChangeEvent callPlotSellChangeEvent(IWorld world, Plot plot, IPlayer seller, double price, boolean isForSale);

    InternalPlotOwnerChangeEvent callPlotOwnerChangeEvent(IWorld world, Plot plot, IPlayer player, String newOwner);

    InternalPlotTeleportEvent callPlotTeleportEvent(IWorld world, Plot plot, IPlayer player, ILocation location, String PlotId);

    InternalPlotWorldLoadEvent callPlotWorldLoadEvent(String worldName, int nbPlots);
}
