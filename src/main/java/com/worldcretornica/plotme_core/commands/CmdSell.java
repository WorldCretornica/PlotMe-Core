package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotSellChangeEvent;

public class CmdSell extends PlotCommand {

    public CmdSell(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "sell";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        IWorld world = player.getWorld();
        if (manager.isPlotWorld(world)) {
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isEconomyEnabled(pmi)) {
                if (pmi.isCanPutOnSale()) {
                    if (player.hasPermission(PermissionNames.USER_SELL) || player.hasPermission(PermissionNames.ADMIN_SELL)) {

                        Plot plot = manager.getPlot(player);
                        if (plot == null) {
                            player.sendMessage(C("NoPlotFound"));
                            return true;
                        } else {
                            if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_SELL)) {

                                PlotSellChangeEvent event;
                                if (plot.isForSale()) {
                                    event = new PlotSellChangeEvent(plot, player, plot.getPrice(), false);
                                    plugin.getEventBus().post(event);

                                    if (!event.isCancelled()) {
                                        plot.setPrice(0.0);
                                        plot.setForSale(false);
                                        plugin.getSqlManager().savePlot(plot);
                                        manager.adjustWall(player);
                                        manager.removeSellSign(plot);

                                        player.sendMessage(C("MsgPlotNoLongerSale"));

                                        if (isAdvancedLogging()) {
                                            serverBridge.getLogger()
                                                    .info(player.getName() + " " + C("MsgRemovedPlot") + " " + plot.getId() + " " + C(
                                                            "MsgFromBeingSold"));
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

                                    event = new PlotSellChangeEvent(plot, player, price, true);
                                    plugin.getEventBus().post(event);

                                    if (!event.isCancelled()) {
                                        plot.setPrice(price);
                                        plot.setForSale(true);
                                        plugin.getSqlManager().savePlot(plot);
                                        manager.getGenManager(world).adjustPlotFor(plot, true, plot.isProtected(), plot.isForSale());
                                        manager.setSellSign(plot);

                                        player.sendMessage(C("MsgPlotForSale"));
                                    }
                                }
                            } else {
                                player.sendMessage(C("MsgDoNotOwnPlot"));
                            }
                        }
                    }
                } else {
                    player.sendMessage(C("PlotSellingDisabled"));
                }
            } else {
                player.sendMessage(C("EconomyDisabled"));
            }
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("CmdSellUsage");
    }
}
