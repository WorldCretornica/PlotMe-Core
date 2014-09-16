package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlotMeEventFactory {

    public static PlotCreateEvent callPlotCreatedEvent(PlotMe_Core plugin, World world, String plotId, Player creator) {
        PlotCreateEvent event = new PlotCreateEvent(plugin, world, plotId, creator);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotClearEvent callPlotClearEvent(PlotMe_Core plugin, World world, Plot plot, Player clearer) {
        PlotClearEvent event = new PlotClearEvent(plugin, world, plot, clearer);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotLoadEvent callPlotLoadedEvent(PlotMe_Core plugin, World world, Plot plot) {
        PlotLoadEvent event = new PlotLoadEvent(plugin, world, plot);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotMoveEvent callPlotMoveEvent(PlotMe_Core plugin, World worldFrom, World worldTo, String idFrom, String idTo, Player mover) {
        PlotMoveEvent event = new PlotMoveEvent(plugin, worldFrom, worldTo, idFrom, idTo, mover);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotResetEvent callPlotResetEvent(PlotMe_Core plugin, World world, Plot plot, CommandSender cs) {
        PlotResetEvent event = new PlotResetEvent(plugin, world, plot, cs);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotBidEvent callPlotBidEvent(PlotMe_Core plugin, World world, Plot plot, Player bidder, double bid) {
        PlotBidEvent event = new PlotBidEvent(plugin, world, plot, bidder, bid);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotAuctionEvent callPlotAuctionEvent(PlotMe_Core plugin, World world, Plot plot, Player player, double minimumbid) {
        PlotAuctionEvent event = new PlotAuctionEvent(plugin, world, plot, player, minimumbid);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotBiomeChangeEvent callPlotBiomeChangeEvent(PlotMe_Core plugin, World world, Plot plot, Player player, Biome biome) {
        PlotBiomeChangeEvent event = new PlotBiomeChangeEvent(plugin, world, plot, player, biome);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotBuyEvent callPlotBuyEvent(PlotMe_Core plugin, World world, Plot plot, Player player, double price) {
        PlotBuyEvent event = new PlotBuyEvent(plugin, world, plot, player, price);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotCommentEvent callPlotCommentEvent(PlotMe_Core plugin, World world, Plot plot, Player commenter, String comment) {
        PlotCommentEvent event = new PlotCommentEvent(plugin, world, plot, commenter, comment);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotWorldCreateEvent callPlotWorldCreateEvent(String worldname, CommandSender cs, Map<String, String> parameters) {
        PlotWorldCreateEvent event = new PlotWorldCreateEvent(worldname, cs, parameters);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotDisposeEvent callPlotDisposeEvent(PlotMe_Core plugin, World world, Plot plot, Player disposer) {
        PlotDisposeEvent event = new PlotDisposeEvent(plugin, world, plot, disposer);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotDoneChangeEvent callPlotDoneEvent(PlotMe_Core plugin, World world, Plot plot, Player player, boolean done) {
        PlotDoneChangeEvent event = new PlotDoneChangeEvent(plugin, world, plot, player, done);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotTeleportHomeEvent callPlotTeleportHomeEvent(PlotMe_Core plugin, World world, Plot plot, Player player) {
        PlotTeleportHomeEvent event = new PlotTeleportHomeEvent(plugin, world, plot, player);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotProtectChangeEvent callPlotProtectChangeEvent(PlotMe_Core plugin, World world, Plot plot, Player player, boolean protect) {
        PlotProtectChangeEvent event = new PlotProtectChangeEvent(plugin, world, plot, player, protect);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotReloadEvent callPlotReloadEvent() {
        PlotReloadEvent event = new PlotReloadEvent();
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotAddAllowedEvent callPlotAddAllowedEvent(PlotMe_Core plugin, World world, Plot plot, Player player, String allowed) {
        PlotAddAllowedEvent event = new PlotAddAllowedEvent(plugin, world, plot, player, allowed);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotRemoveAllowedEvent callPlotRemoveAllowedEvent(PlotMe_Core plugin, World world, Plot plot, Player player, String allowed) {
        PlotRemoveAllowedEvent event = new PlotRemoveAllowedEvent(plugin, world, plot, player, allowed);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotAddDeniedEvent callPlotAddDeniedEvent(PlotMe_Core plugin, World world, Plot plot, Player player, String denied) {
        PlotAddDeniedEvent event = new PlotAddDeniedEvent(plugin, world, plot, player, denied);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotRemoveDeniedEvent callPlotRemoveDeniedEvent(PlotMe_Core plugin, World world, Plot plot, Player player, String denied) {
        PlotRemoveDeniedEvent event = new PlotRemoveDeniedEvent(plugin, world, plot, player, denied);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotSellChangeEvent callPlotSellChangeEvent(PlotMe_Core plugin, World world, Plot plot, Player seller, double price, boolean soldToBank, boolean isForSale) {
        PlotSellChangeEvent event = new PlotSellChangeEvent(plugin, world, plot, seller, price, soldToBank, isForSale);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotOwnerChangeEvent callPlotOwnerChangeEvent(PlotMe_Core plugin, World world, Plot plot, Player player, String newowner) {
        PlotOwnerChangeEvent event = new PlotOwnerChangeEvent(plugin, world, plot, player, newowner);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotTeleportEvent callPlotTeleportEvent(PlotMe_Core plugin, World world, Plot plot, Player player, Location location, String PlotId) {
        PlotTeleportEvent event = new PlotTeleportEvent(plugin, world, plot, player, location, PlotId);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotTeleportEvent callPlotTeleportEvent(PlotMe_Core plugin, World world, Player player, Location location, String PlotId) {
        PlotTeleportEvent event = new PlotTeleportEvent(plugin, world, null, player, location, PlotId);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlotWorldLoadEvent callPlotWorldLoadEvent(PlotMe_Core plugin, String worldname, int NbPlots) {
        PlotWorldLoadEvent event = new PlotWorldLoadEvent(plugin, worldname, NbPlots);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
}
