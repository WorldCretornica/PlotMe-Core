package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
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
        if (manager.isPlotWorld(world)) {
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isEconomyEnabled(pmi)) {

                if (pmi.isCanPutOnSale()) {
                    if (player.hasPermission(PermissionNames.USER_SELL) || player.hasPermission(PermissionNames.ADMIN_SELL)) {
                        String id = manager.getPlotId(player);

                        if (id.isEmpty()) {
                            player.sendMessage("§c" + C("MsgNoPlotFound"));
                        } else if (!manager.isPlotAvailable(id, pmi)) {
                            Plot plot = manager.getPlotById(id, pmi);

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

                                        manager.adjustWall(player);
                                        manager.removeSellSign(world, id);

                                        player.sendMessage(C("MsgPlotNoLongerSale"));

                                        if (isAdvancedLogging()) {
                                            serverBridge.getLogger()
                                                    .info(player.getName() + " " + C("MsgRemovedPlot") + " " + id + " " + C("MsgFromBeingSold"));
                                        }
                                    }
                                } else {
                                    double price = pmi.getSellToPlayerPrice();

                                    if (args.length == 2) {
                                        try {
                                            price = Double.parseDouble(args[1]);
                                        } catch (Exception e) {
                                            player.sendMessage(
                                                    C("WordUsage") + ": §c /plotme sell <" + C("WordAmount") + ">§r " + C("WordExample")
                                                    + ": §c/plotme sell 200");
                                            return true;
                                        }
                                    }

                                    if (price < 0.0) {
                                        player.sendMessage("§c" + C("MsgInvalidAmount"));
                                    } else {
                                        event = serverBridge.getEventFactory().callPlotSellChangeEvent(plugin, world, plot, player, price, true);

                                        if (!event.isCancelled()) {
                                            plot.setCustomPrice(price);
                                            plot.setForSale(true);

                                            plot.updateField("customprice", price);
                                            plot.updateField("forsale", true);

                                            manager.adjustWall(player);
                                            manager.setSellSign(world, plot);

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
