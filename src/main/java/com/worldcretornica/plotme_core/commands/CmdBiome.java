package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.api.event.InternalPlotBiomeChangeEvent;

import net.milkbowl.vault.economy.EconomyResponse;

public class CmdBiome extends PlotCommand {

    public CmdBiome(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.use.biome")) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
            } else {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());
                if (id.equals("")) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    IWorld w = p.getWorld();

                    if (args.length == 2) {
                        IBiome biome = null;

                        biome = sob.getBiome(args[1]);
                                
                        if (biome == null) {
                            p.sendMessage(RED + args[1] + RESET + " " + C("MsgIsInvalidBiome"));
                        } else {
                            Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);
                            String playername = p.getName();

                            if (plot.getOwner().equalsIgnoreCase(playername) || plugin.cPerms(p, "PlotMe.admin")) {
                                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(w);

                                double price = 0;

                                InternalPlotBiomeChangeEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(w)) {
                                    price = pmi.getBiomeChangePrice();
                                    double balance = sob.getBalance(p);

                                    if (balance >= price) {
                                        event = sob.getEventFactory().callPlotBiomeChangeEvent(plugin, w, plot, p, biome);
                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = sob.withdrawPlayer(p, price);

                                            if (!er.transactionSuccess()) {
                                                p.sendMessage(RED + er.errorMessage);
                                                Util().warn(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        p.sendMessage(RED + C("MsgNotEnoughBiome") + " " + C("WordMissing") + " " + RESET + Util().moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = sob.getEventFactory().callPlotBiomeChangeEvent(plugin, w, plot, p, biome);
                                }

                                if (!event.isCancelled()) {
                                    plugin.getPlotMeCoreManager().setBiome(w, id, biome);
                                    plot.setBiome(biome);

                                    p.sendMessage(C("MsgBiomeSet") + " " + BLUE + Util().FormatBiome(biome.name()) + " " + Util().moneyFormat(-price));

                                    if (isAdvancedLogging()) {
                                        plugin.getLogger().info(LOG + playername + " " + C("MsgChangedBiome") + " " + id + " " + C("WordTo") + " "
                                                                        + Util().FormatBiome(biome.name()) + ((price != 0) ? " " + C("WordFor") + " " + price : ""));
                                    }
                                }
                            } else {
                                p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedBiome"));
                            }
                        }
                    } else {
                        Plot plot = plugin.getPlotMeCoreManager().getMap(w).getPlot(id);

                        p.sendMessage(C("MsgPlotUsingBiome") + " " + BLUE + Util().FormatBiome(plot.getBiome().name()));
                    }
                } else {
                    p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
