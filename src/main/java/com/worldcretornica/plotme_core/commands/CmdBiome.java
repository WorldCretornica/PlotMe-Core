package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotBiomeChangeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdBiome extends PlotCommand {

    public CmdBiome(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (PlotMe_Core.cPerms(player, "PlotMe.use.biome")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(player.getLocation());
                if (id.isEmpty()) {
                    player.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, player)) {
                    IWorld world = player.getWorld();

                    if (args.length == 2) {

                        IBiome biome = sob.getBiome(args[1]);

                        if (biome == null) {
                            player.sendMessage(RED + args[1] + RESET + " " + C("MsgIsInvalidBiome"));
                        } else {
                            Plot plot = plugin.getPlotMeCoreManager().getPlotById(player, id);
                            String playername = player.getName();

                            if (plot.getOwner().equalsIgnoreCase(playername) || PlotMe_Core.cPerms(player, "PlotMe.admin")) {
                                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);

                                double price = 0;

                                InternalPlotBiomeChangeEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                                    price = pmi.getBiomeChangePrice();
                                    double balance = sob.getBalance(player);

                                    if (balance >= price) {
                                        event = sob.getEventFactory().callPlotBiomeChangeEvent(plugin, world, plot, player, biome);
                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = sob.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage(RED + er.errorMessage);
                                                Util().warn(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage(RED + C("MsgNotEnoughBiome") + " " + C("WordMissing") + " " + RESET + Util().moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = sob.getEventFactory().callPlotBiomeChangeEvent(plugin, world, plot, player, biome);
                                }

                                if (!event.isCancelled()) {
                                    plugin.getPlotMeCoreManager().setBiome(world, id, biome);
                                    plot.setBiome(biome);

                                    player.sendMessage(C("MsgBiomeSet") + " " + BLUE + Util().FormatBiome(biome.name()) + " " + Util().moneyFormat(-price));

                                    if (isAdvancedLogging()) {
                                        plugin.getLogger().info(LOG + playername + " " + C("MsgChangedBiome") + " " + id + " " + C("WordTo") + " "
                                                                        + Util().FormatBiome(biome.name()) + (price != 0 ? " " + C("WordFor") + " " + price : ""));
                                    }
                                }
                            } else {
                                player.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedBiome"));
                            }
                        }
                    } else {
                        Plot plot = plugin.getPlotMeCoreManager().getMap(world).getPlot(id);

                        player.sendMessage(C("MsgPlotUsingBiome") + " " + BLUE + Util().FormatBiome(plot.getBiome().name()));
                    }
                } else {
                    player.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                player.sendMessage(RED + C("MsgNotPlotWorld"));
            }
        } else {
            player.sendMessage(RED + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
