package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotSellChangeEvent;

public class CmdSell extends PlotCommand {

    public CmdSell(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "sell";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        IPlayer player = (IPlayer) sender;
        IWorld world = player.getWorld();
        if (manager.isPlotWorld(world)) {
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isEconomyEnabled(pmi)) {

                if (pmi.isCanPutOnSale()) {
                    if (player.hasPermission(PermissionNames.USER_SELL) || player.hasPermission(PermissionNames.ADMIN_SELL)) {
                        PlotId id = manager.getPlotId(player);

                        if (id == null) {
                            player.sendMessage(C("MsgNoPlotFound"));
                            return true;
                        } else if (!manager.isPlotAvailable(id, pmi)) {
                            Plot plot = manager.getPlotById(id, pmi);

                            if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_SELL)) {

                                InternalPlotSellChangeEvent event;

                                if (plot.isForSale()) {
                                    event = new InternalPlotSellChangeEvent(world, plot, player, plot.getPrice(), false);
                                    serverBridge.getEventBus().post(event);

                                    if (!event.isCancelled()) {
                                        plot.setPrice(0.0);
                                        plot.setForSale(false);

                                        plot.updateField("customprice", 0);
                                        plot.updateField("forsale", false);

                                        manager.adjustWall(player);
                                        manager.removeSellSign(id);

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
                                        } catch (NumberFormatException e) {
                                            player.sendMessage("Invalid price.");
                                            return true;
                                        }
                                    }

                                    if (price < 0.0) {
                                        player.sendMessage(C("MsgInvalidAmount"));
                                    } else {
                                        event = new InternalPlotSellChangeEvent(world, plot, player, price, true);
                                        serverBridge.getEventBus().post(event);

                                        if (!event.isCancelled()) {
                                            plot.setPrice(price);
                                            plot.setForSale(true);

                                            plot.updateField("customprice", price);
                                            plot.updateField("forsale", true);

                                            manager.adjustWall(player);
                                            manager.setSellSign(plot);

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
                                player.sendMessage(C("MsgDoNotOwnPlot"));
                            }
                        } else {
                            player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                        }
                    } else {
                        player.sendMessage(C("MsgPermissionDenied"));
                        return false;
                    }
                } else {
                    player.sendMessage(C("MsgSellingPlotsIsDisabledWorld"));
                }
            } else {
                player.sendMessage(C("MsgEconomyDisabledWorld"));
            }
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme sell [" + C("WordAmount") + "]";
    }
}
