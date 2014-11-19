package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportHomeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.UUID;

public class CmdHome extends PlotCommand {

    public CmdHome(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission("PlotMe.use.home") || player.hasPermission("PlotMe.admin.home.other")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player) || sob.getConfig().getBoolean("allowWorldTeleport")) {
                String playername = player.getName();
                UUID uuid = player.getUniqueId();
                IWorld world;

                if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                    world = player.getWorld();
                } else {
                    world = plugin.getPlotMeCoreManager().getFirstWorld();
                }

                String worldname = "";
                if (world != null) {
                    worldname = world.getName();
                }

                int nb = 1;
                if (args[0].contains(":")) {
                    try {
                        if (args[0].split(":").length == 1 || args[0].split(":")[1].isEmpty()) {
                            player.sendMessage(C("WordUsage") + ": §c/plotme home:# §r" + C("WordExample") + ": §c/plotme home:1");
                            return true;
                        } else {
                            nb = Integer.parseInt(args[0].split(":")[1]);
                        }
                    } catch (Exception ex) {
                        player.sendMessage(C("WordUsage") + ": §c/plotme home:# §r" + C("WordExample") + ": §c/plotme home:1");
                        return true;
                    }
                }

                if (args.length >= 2) {
                    if (sob.getWorld(args[1]) == null) {
                        if (player.hasPermission("PlotMe.admin.home.other")) {
                            playername = args[1];
                            uuid = null;
                        }
                    } else {
                        world = sob.getWorld(args[1]);
                    }
                }

                if (args.length == 3) {
                    if (sob.getWorld(args[2]) == null) {
                        player.sendMessage("§c" + args[2] + C("MsgWorldNotPlot"));
                        return true;
                    } else {
                        world = sob.getWorld(args[2]);
                        worldname = args[2];
                    }
                }

                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);
                if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                    int i = nb - 1;

                    for (Plot plot : plugin.getSqlManager().getOwnedPlots(world.getName(), uuid, playername)) {
                        if (uuid == null && plot.getOwner().equalsIgnoreCase(playername) || uuid != null && plot.getOwnerId() != null && plot.getOwnerId().equals(uuid)) {
                            if (i == 0) {

                                double price = 0;

                                InternalPlotTeleportHomeEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(pmi)) {
                                    price = pmi.getPlotHomePrice();
                                    double balance = sob.getBalance(player);

                                    if (balance >= price) {
                                        event = sob.getEventFactory().callPlotTeleportHomeEvent(plugin, world, plot, player);

                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = sob.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage("§c" + er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage("§c" + C("MsgNotEnoughTp") + " " + C("WordMissing") + " §r" + Util().moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = sob.getEventFactory().callPlotTeleportHomeEvent(plugin, world, plot, player);
                                }

                                if (!event.isCancelled()) {
                                    player.teleport(event.getHomeLocation());

                                    if (price != 0) {
                                        player.sendMessage(Util().moneyFormat(-price));
                                    }
                                }
                                return true;
                            } else {
                                i--;
                            }
                        }
                    }

                    if (nb > 0) {
                        if (playername.equalsIgnoreCase(player.getName())) {
                            player.sendMessage("§c" + C("MsgPlotNotFound") + " #" + nb);
                        } else {
                            player.sendMessage("§c" + playername + " " + C("MsgDoesNotHavePlot") + " #" + nb);
                        }
                    } else if (!playername.equalsIgnoreCase(player.getName())) {
                        player.sendMessage("§c" + playername + " " + C("MsgDoesNotHavePlot"));
                    } else {
                        player.sendMessage("§c" + C("MsgYouHaveNoPlot"));
                    }
                } else {
                    player.sendMessage("§c" + worldname + C("MsgWorldNotPlot"));
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
