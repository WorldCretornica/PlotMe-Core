package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotBiomeChangeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBiome;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdBiome extends PlotCommand {

    public CmdBiome(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission(PermissionNames.USER_BIOME)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);
                if (id == null){
                    return true;
                } else if (!manager.isPlotAvailable(id, pmi)) {

                    if (args.length == 2) {

                        BukkitBiome biome = (BukkitBiome) serverBridge.getBiome(args[1]);

                        if (biome == null) {
                            player.sendMessage(args[1] + " " + C("MsgIsInvalidBiome"));
                            return true;
                        }
                        String biomeName = biome.getBiome().name();

                        Plot plot = manager.getPlotById(id, pmi);
                        String playerName = player.getName();

                        if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission("PlotMe.admin")) {

                            double price = 0.0;

                            InternalPlotBiomeChangeEvent event;

                            if (manager.isEconomyEnabled(pmi)) {
                                price = pmi.getBiomeChangePrice();
                                double balance = serverBridge.getBalance(player);

                                if (balance >= price) {
                                    event = serverBridge.getEventFactory().callPlotBiomeChangeEvent(world, plot, player, biome);
                                    if (event.isCancelled()) {
                                        return true;
                                    } else {
                                        EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                        if (!er.transactionSuccess()) {
                                            player.sendMessage(er.errorMessage);
                                            serverBridge.getLogger().warning(er.errorMessage);
                                            return true;
                                        }
                                    }
                                } else {
                                    player.sendMessage(C("MsgNotEnoughBiome") + " " + C("WordMissing") + " " + plugin
                                            .moneyFormat(price - balance, false));
                                    return true;
                                }
                            } else {
                                event = serverBridge.getEventFactory().callPlotBiomeChangeEvent(world, plot, player, biome);
                            }

                            if (!event.isCancelled()) {
                                plot.setBiome(biome);
                                manager.setBiome(world, id, biome);

                                player.sendMessage(C("MsgBiomeSet") + " " + biomeName + " " + plugin.moneyFormat(-price, true));

                                if (isAdvancedLogging()) {
                                    if (price == 0) {
                                        serverBridge.getLogger().info(playerName + " " + C("MsgChangedBiome") + " " + id + " " + C("WordTo") + " "
                                                + biomeName);
                                    } else {
                                        serverBridge.getLogger().info(playerName + " " + C("MsgChangedBiome") + " " + id + " " + C("WordTo") + " "
                                                + biomeName + (" " + C("WordFor") + " " + price));
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedBiome"));
                        }
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
                return true;
            }
        } else {
            player.sendMessage(C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}