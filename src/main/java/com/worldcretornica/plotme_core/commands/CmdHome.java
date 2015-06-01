package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotTeleportHomeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CmdHome extends PlotCommand {

    public CmdHome(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "home";
    }

    @Override
    public List getAliases() {
        return Collections.singletonList("h");
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.USER_HOME)) {
            if (manager.isPlotWorld(player) || plugin.getConfig().getBoolean("allowWorldTeleport")) {
                UUID uuid = player.getUniqueId();
                IWorld world;
                if (manager.isPlotWorld(player)) {
                    world = player.getWorld();
                } else {
                    world = manager.getFirstWorld();
                }

                String worldName = world.getName();

                int nb = 1;
                if (args[1].contains(":")) {
                    if (args[1].split(":").length == 1 || args[0].split(":")[1].isEmpty()) {
                        player.sendMessage(getUsage());
                        return true;
                    }
                    try {
                        nb = Integer.parseInt(args[1].split(":")[1]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(getUsage());
                        return true;
                    }
                }

                String playerName = player.getName();
                if (args.length == 2) {
                    if (manager.getWorld(args[1]) == null) {
                        playerName = args[1];
                        uuid = null;
                    } else {
                        world = serverBridge.getWorld(args[1]);
                    }
                }

                if (args.length == 3) {
                    if (manager.getWorld(args[2]) == null) {
                        player.sendMessage(args[2] + C("MsgWorldNotPlot"));
                        return true;
                    }
                    world = serverBridge.getWorld(args[2]);
                    worldName = args[2];
                }
                if (world == null) {
                    return true;
                }
                PlotMapInfo pmi = manager.getMap(world);
                if (manager.isPlotWorld(world)) {
                    int i = nb - 1;

                    for (Plot plot : plugin.getSqlManager().getOwnedPlots(world, uuid)) {
                        ILocation location;
                        if (uuid == null) {
                            if (plot.getOwner().equals(playerName)) {
                                if (i == 0) {

                                    double price = 0.0;

                                    location = manager.getPlotHome(plot.getId(), player.getWorld());
                                    PlotTeleportHomeEvent event = new PlotTeleportHomeEvent(plot, player, location);

                                    if (manager.isEconomyEnabled(pmi)) {
                                        price = pmi.getPlotHomePrice();

                                        if (serverBridge.has(player, price)) {
                                            plugin.getEventBus().post(event);

                                            if (event.isCancelled()) {
                                                return true;
                                            }
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage(er.errorMessage);
                                                return true;
                                            }
                                        } else {
                                            player.sendMessage(
                                                    C("MsgNotEnoughTp") + " " + C("WordMissing") + " " + serverBridge.getEconomy().format(price));
                                            return true;
                                        }
                                    } else {
                                        plugin.getEventBus().post(event);
                                    }

                                    if (!event.isCancelled()) {
                                        player.setLocation(event.getHomeLocation());

                                        if (price != 0) {
                                            player.sendMessage(serverBridge.getEconomy().format(price));
                                        }
                                    }
                                    return true;
                                }
                                i--;
                            }
                        } else if (plot.getOwnerId().equals(uuid)) {
                            if (i == 0) {

                                double price = 0.0;

                                location = manager.getPlotHome(plot.getId(), player.getWorld());
                                PlotTeleportHomeEvent event = new PlotTeleportHomeEvent(plot, player, location);

                                if (manager.isEconomyEnabled(pmi)) {
                                    price = pmi.getPlotHomePrice();

                                    if (serverBridge.has(player, price)) {
                                        plugin.getEventBus().post(event);

                                        if (!event.isCancelled()) {
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage(
                                                C("MsgNotEnoughTp") + " " + C("WordMissing") + " " + serverBridge.getEconomy().format(price));
                                        return true;
                                    }
                                } else {
                                    plugin.getEventBus().post(event);
                                }

                                if (!event.isCancelled()) {
                                    player.teleport(event.getHomeLocation());

                                    if (price != 0) {
                                        player.sendMessage(serverBridge.getEconomy().format(price));
                                    }
                                }
                                return true;
                            }
                            i--;
                        }
                    }

                    if (nb > 0) {
                        if (playerName.equals(player.getName())) {
                            player.sendMessage(C("MsgPlotNotFound") + " #" + nb);
                        } else {
                            player.sendMessage(playerName + " " + C("MsgDoesNotHavePlot") + " #" + nb);
                        }
                    } else if (!playerName.equals(player.getName())) {
                        player.sendMessage(playerName + " " + C("MsgDoesNotHavePlot"));
                    } else {
                        player.sendMessage(C("MsgYouHaveNoPlot"));
                    }
                } else {
                    player.sendMessage(worldName + C("MsgWorldNotPlot"));
                }
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("CmdHomeUsage");
    }
}
