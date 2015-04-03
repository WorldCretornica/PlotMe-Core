package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotRemoveDeniedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdUndeny extends PlotCommand {

    public CmdUndeny(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "undeny";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args[1].length() > 16 || !validUserPattern.matcher(args[1]).matches()) {
            throw new IllegalArgumentException(C("InvalidCommandInput"));
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_DENY) || player.hasPermission(PermissionNames.USER_DENY)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);
                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                } else if (!manager.isPlotAvailable(id, pmi)) {
                    if (args.length < 2 && args.length >= 3) {
                        player.sendMessage(getUsage());
                    } else {
                        Plot plot = manager.getPlotById(id, pmi);
                        String playerName = player.getName();
                        String denied = args[1];

                        if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_DENY)) {
                            if (plot.isDeniedConsulting(denied)) {

                                double price = 0.0;

                                InternalPlotRemoveDeniedEvent event;

                                if (manager.isEconomyEnabled(pmi)) {
                                    price = pmi.getUndenyPlayerPrice();
                                    double balance = serverBridge.getBalance(player);

                                    if (balance >= price) {
                                        event = new InternalPlotRemoveDeniedEvent(world, plot, player, denied);
                                        serverBridge.getEventBus().post(event);

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
                                                C("MsgNotEnoughUndeny") + " " + C("WordMissing") + " " + plugin.moneyFormat(price - balance,
                                                        false));
                                        return true;
                                    }
                                } else {
                                    event = new InternalPlotRemoveDeniedEvent(world, plot, player, denied);
                                    serverBridge.getEventBus().post(event);

                                }

                                if (!event.isCancelled()) {
                                    plot.removeDenied(denied);

                                    player.sendMessage(
                                            C("WordPlayer") + " " + denied + " " + C("MsgNowUndenied") + " " + plugin.moneyFormat(-price, true));

                                    if (isAdvancedLogging()) {
                                        if (price != 0) {
                                            serverBridge.getLogger()
                                                    .info(playerName + " " + C("MsgUndeniedPlayer") + " " + denied + " " + C("MsgFromPlot") + " " + id
                                                            + (" " + C("WordFor") + " " + price));
                                        } else {
                                            serverBridge.getLogger()
                                                    .info(playerName + " " + C("MsgUndeniedPlayer") + " " + denied + " " + C("MsgFromPlot") + " "
                                                            + id);
                                        }
                                    }
                                }
                            } else {
                                player.sendMessage(C("WordPlayer") + " " + args[1] + " " + C("MsgWasNotDenied"));
                            }
                        } else {
                            player.sendMessage(C("MsgThisPlot") + " (" + id + ") " + C("MsgNotYoursNotAllowedUndeny"));
                        }
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + " (" + id + ") " + C("MsgHasNoOwner"));
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
        return C("WordUsage") + ": /plotme undeny <" + C("WordPlayer") + ">";
    }

}
