package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.ClearReason;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotClearEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdClear extends PlotCommand {

    public CmdClear(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "clear";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(getUsage());
            return true;
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_CLEAR) || player.hasPermission(PermissionNames.USER_CLEAR)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                Plot plot = manager.getPlot(player);
                if (plot == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                } else if (plot.isProtected()) {
                    player.sendMessage(C("MsgPlotProtectedCannotClear"));
                } else {
                    String playerName = player.getName();

                    if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_CLEAR)) {

                        double price = 0.0;

                        PlotClearEvent event = new PlotClearEvent(world, plot, player);

                        if (manager.isEconomyEnabled(pmi)) {
                            price = pmi.getClearPrice();

                            if (serverBridge.has(player, price)) {
                                plugin.getEventBus().post(event);
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
                                player.sendMessage(
                                        C("MsgNotEnoughClear") + " " + C("WordMissing") + " " + serverBridge.getEconomy().get().format(price));
                                return true;
                            }
                        } else {
                            plugin.getEventBus().post(event);
                        }

                        if (!event.isCancelled()) {
                            manager.clear(plot, world, player, ClearReason.Clear);

                            if (isAdvancedLogging()) {
                                if (price == 0) {
                                    serverBridge.getLogger().info(playerName + " " + C("MsgClearedPlot") + " " + plot.getId().getID());
                                } else {
                                    serverBridge.getLogger()
                                            .info(playerName + " " + C("MsgClearedPlot") + " " + plot.getId().getID() + (" " + C("WordFor") + " "
                                                    + price));
                                }
                            }
                        }
                    } else {
                        player.sendMessage(C("MsgThisPlot") + "(" + plot.getId().getID() + ") " + C("MsgNotYoursNotAllowedClear"));
                    }
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
        return C("WordUsage") + ": /plotme clear";
    }
}
