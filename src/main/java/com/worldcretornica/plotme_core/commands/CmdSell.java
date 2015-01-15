package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotSellChangeEvent;

public class CmdSell extends PlotCommand {

    public CmdSell(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        IWorld world = player.getWorld();
        if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);
            if (plugin.getPlotMeCoreManager().isEconomyEnabled(pmi)) {

                if (pmi.isCanPutOnSale()) {
                    if (player.hasPermission(PermissionNames.USER_SELL) || player.hasPermission(PermissionNames.ADMIN_SELL)) {
                        String id = PlotMeCoreManager.getPlotId(player);

                        if (id.isEmpty()) {
                            player.sendMessage("§c" + C("MsgNoPlotFound"));
                        } else if (!PlotMeCoreManager.isPlotAvailable(id, pmi)) {
                            Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

                            if (plot.getOwnerId().equals(player.getUniqueId()) || player.hasPermission(PermissionNames.ADMIN_SELL)) {

                                InternalPlotSellChangeEvent event;

                                if (plot.isForSale()) {
                                    event =
                                            serverBridge.getEventFactory()
                                                    .callPlotSellChangeEvent(plugin, world, plot, player, plot.getCustomPrice(), false);

                                    if (!event.isCancelled()) {
                                        plot.setCustomPrice(0.0);
                                        plot.setForSale(false);

                                        plot.updateField("customprice", 0);
                                        plot.updateField("forsale", false);

                                        plugin.getPlotMeCoreManager().adjustWall(player);
                                        plugin.getPlotMeCoreManager().setSellSign(world, plot);

                                        player.sendMessage(C("MsgPlotNoLongerSale"));

                                        if (isAdvancedLogging()) {
                                            serverBridge.getLogger()
                                                    .info(player.getName() + " " + C("MsgRemovedPlot") + " " + id + " " + C("MsgFromBeingSold"));
                                        }
                                    }
                                } else {
                                    double price = pmi.getSellToPlayerPrice();
                                    boolean bank = false;

                                    if (args.length == 2) {
                                        if ("bank".equalsIgnoreCase(args[1])) {
                                            bank = true;
                                        } else {
                                            try {
                                                price = Double.parseDouble(args[1]);
                                            } catch (Exception e) {
                                                player.sendMessage(
                                                        C("WordUsage") + ": §c /plotme sell <" + C("WordAmount") + ">§r " + C("WordExample")
                                                        + ": §c/plotme sell 200");
                                            }
                                        }
                                    }

                                    if (bank) {
                                        player.sendMessage("§c" + C("MsgCannotSellToBank"));
                                    } else if (price < 0.0) {
                                        player.sendMessage("§c" + C("MsgInvalidAmount"));
                                    } else {
                                        event = serverBridge.getEventFactory().callPlotSellChangeEvent(plugin, world, plot, player, price, true);

                                        if (!event.isCancelled()) {
                                            plot.setCustomPrice(price);
                                            plot.setForSale(true);

                                            plot.updateField("customprice", price);
                                            plot.updateField("forsale", true);

                                            plugin.getPlotMeCoreManager().adjustWall(player);
                                            plugin.getPlotMeCoreManager().setSellSign(world, plot);

                                            player.sendMessage(C("MsgPlotForSale"));

                                            if (isAdvancedLogging()) {
                                                serverBridge.getLogger()
                                                        .info(player.getName() + " " + C("MsgPutOnSalePlot") + " " + id + " " + C("WordFor") + " "
                                                              + price);
                                            }
                                        }
                                    }
                                }
                            } else {
                                player.sendMessage("§c" + C("MsgDoNotOwnPlot"));
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                        }
                    } else {
                        player.sendMessage("§c" + C("MsgPermissionDenied"));
                        return false;
                    }
                } else {
                    player.sendMessage("§c" + C("MsgSellingPlotsIsDisabledWorld"));
                }
            } else {
                player.sendMessage("§c" + C("MsgEconomyDisabledWorld"));
            }
        }
        return true;
    }
}
