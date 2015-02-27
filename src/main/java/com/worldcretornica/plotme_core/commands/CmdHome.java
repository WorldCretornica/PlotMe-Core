package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportHomeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.UUID;

public class CmdHome extends PlotCommand {

    public CmdHome(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission(PermissionNames.USER_HOME)) {
            if (manager.isPlotWorld(player) || plugin.getConfig().getBoolean("allowWorldTeleport")) {
                UUID uuid = player.getUniqueId();
                IWorld world;
                if (manager.isPlotWorld(player)) {
                    world = player.getWorld();
                } else {
                    world = manager.getFirstWorld();
                }

                String worldName = null;
                if (world != null) {
                    worldName = world.getName();
                }

                int nb = 1;
                if (args[0].contains(":")) {
                    if (args[0].split(":").length == 1 || args[0].split(":")[1].isEmpty()) {
                        player.sendMessage(C("WordUsage") + ": §c/plotme home:# §r" + C("WordExample") + ": §c/plotme home:1");
                        return true;
                    }
                    try {
                        nb = Integer.parseInt(args[0].split(":")[1]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(C("WordUsage") + ": §c/plotme home:# §r" + C("WordExample") + ": §c/plotme home:1");
                        return true;
                    }
                }

                String playerName = player.getName();
                if (args.length == 2) {
                    if (serverBridge.getWorld(args[1]) == null) {
                        playerName = args[1];
                        uuid = null;
                    } else {
                        world = serverBridge.getWorld(args[1]);
                    }
                }

                if (args.length == 3) {
                    if (serverBridge.getWorld(args[2]) == null) {
                        player.sendMessage("§c" + args[2] + C("MsgWorldNotPlot"));
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

                    for (Plot plot : plugin.getSqlManager().getOwnedPlots(world.getName(), uuid, playerName)) {
                        BukkitLocation location;
                        if (uuid == null) {
                            if (plot.getOwner().equalsIgnoreCase(playerName)) {
                                if (i == 0) {

                                    double price = 0.0;

                                    InternalPlotTeleportHomeEvent event;

                                    if (manager.isEconomyEnabled(pmi)) {
                                        price = pmi.getPlotHomePrice();
                                        double balance = serverBridge.getBalance(player);

                                        if (balance >= price) {
                                            location = (BukkitLocation) manager.getPlotHome(world, plot.getId());
                                            event = serverBridge.getEventFactory().callPlotTeleportHomeEvent(world, plot, player, location);

                                            if (event.isCancelled()) {
                                                return true;
                                            }
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage("§c" + er.errorMessage);
                                                return true;
                                            }
                                        } else {
                                            player.sendMessage("§c" + C("MsgNotEnoughTp") + " " + C("WordMissing") + " §r" + Util()
                                                    .moneyFormat(price - balance, false));
                                            return true;
                                        }
                                    } else {
                                        location = (BukkitLocation) manager.getPlotHome(world, plot.getId());
                                        event = serverBridge.getEventFactory().callPlotTeleportHomeEvent(world, plot, player, location);
                                    }

                                    if (!event.isCancelled()) {
                                        player.setLocation(event.getHomeLocation());

                                        if (price != 0) {
                                            player.sendMessage(Util().moneyFormat(-price, true));
                                        }
                                    }
                                    return true;
                                }
                                i--;
                            }
                        } else if (plot.getOwnerId().equals(uuid)) {
                            if (i == 0) {

                                double price = 0.0;

                                InternalPlotTeleportHomeEvent event;

                                if (manager.isEconomyEnabled(pmi)) {
                                    price = pmi.getPlotHomePrice();
                                    double balance = serverBridge.getBalance(player);

                                    if (balance >= price) {
                                        location = (BukkitLocation) manager.getPlotHome(world, plot.getId());
                                        event = serverBridge.getEventFactory().callPlotTeleportHomeEvent(world, plot, player, location);

                                        if (!event.isCancelled()) {
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage("§c" + er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage("§c" + C("MsgNotEnoughTp") + " " + C("WordMissing") + " §r" + Util()
                                                .moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    location = (BukkitLocation) manager.getPlotHome(world, plot.getId());
                                    event = serverBridge.getEventFactory().callPlotTeleportHomeEvent(world, plot, player, location);
                                }

                                if (!event.isCancelled()) {
                                    player.setLocation(event.getHomeLocation());

                                    if (price != 0) {
                                        player.sendMessage(Util().moneyFormat(-price, true));
                                    }
                                }
                                return true;
                            }
                            i--;
                        }
                    }

                    if (nb > 0) {
                        if (playerName.equalsIgnoreCase(player.getName())) {
                            player.sendMessage("§c" + C("MsgPlotNotFound") + " #" + nb);
                        } else {
                            player.sendMessage("§c" + playerName + " " + C("MsgDoesNotHavePlot") + " #" + nb);
                        }
                    } else if (!playerName.equalsIgnoreCase(player.getName())) {
                        player.sendMessage("§c" + playerName + " " + C("MsgDoesNotHavePlot"));
                    } else {
                        player.sendMessage("§c" + C("MsgYouHaveNoPlot"));
                    }
                } else {
                    player.sendMessage("§c" + worldName + C("MsgWorldNotPlot"));
                }
            } else {
                player.sendMessage("§c" + C("MsgNotPlotWorld"));
            }
        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
