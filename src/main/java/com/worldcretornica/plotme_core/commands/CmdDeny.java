package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddDeniedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.List;

public class CmdDeny extends PlotCommand {

    public CmdDeny(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission(PermissionNames.ADMIN_DENY) || player.hasPermission(PermissionNames.USER_DENY)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                String id = manager.getPlotId(player);
                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!manager.isPlotAvailable(id, pmi)) {
                    if (args.length < 2 || args[1].isEmpty()) {
                        player.sendMessage(C("WordUsage") + " §c/plotme deny <" + C("WordPlayer") + ">");
                    } else {
                        Plot plot = manager.getPlotById(id, pmi);
                        String denied = args[1];

                        if (plot.getOwnerId() == player.getUniqueId() || player.hasPermission(PermissionNames.ADMIN_DENY)) {
                            if (plot.getOwner().equalsIgnoreCase(denied)) {
                                player.sendMessage(C("MsgCannotDenyOwner"));
                                return true;
                            }

                            if (plot.isDeniedConsulting(denied) || plot.isGroupDenied(denied)) {
                                player.sendMessage(C("WordPlayer") + " §c" + args[1] + "§r " + C("MsgAlreadyDenied"));
                            } else {

                                double price = 0.0;

                                InternalPlotAddDeniedEvent event;

                                if (manager.isEconomyEnabled(pmi)) {
                                    price = pmi.getDenyPlayerPrice();
                                    double balance = serverBridge.getBalance(player);

                                    if (balance >= price) {
                                        event = serverBridge.getEventFactory().callPlotAddDeniedEvent(plugin, world, plot, player, denied);

                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage("§c" + er.errorMessage);
                                                serverBridge.getLogger().warning(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage("§c" + C("MsgNotEnoughDeny") + " " + C("WordMissing") + " §r" + Util()
                                                .moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = serverBridge.getEventFactory().callPlotAddDeniedEvent(plugin, world, plot, player, denied);
                                }

                                if (!event.isCancelled()) {
                                    plot.addDenied(denied);
                                    plot.removeAllowed(denied);

                                    if ("*".equals(denied)) {
                                        List<IPlayer> playersInPlot = manager.getPlayersInPlot(world, id);

                                        for (IPlayer iPlayer : playersInPlot) {
                                            if (!plot.isAllowed(iPlayer.getName(), iPlayer.getUniqueId())) {
                                                iPlayer.setLocation(manager.getPlotHome(world, plot.getId()));
                                            }
                                        }
                                    } else {
                                        IPlayer deniedPlayer = serverBridge.getPlayerExact(denied);

                                        if (deniedPlayer != null) {
                                            if (deniedPlayer.getWorld().equals(world)) {
                                                String plotId = manager.getPlotId(deniedPlayer);

                                                if (plotId.equalsIgnoreCase(id)) {
                                                    deniedPlayer.setLocation(manager.getPlotHome(world, plot.getId()));
                                                }
                                            }
                                        }
                                    }

                                    player.sendMessage(
                                            C("WordPlayer") + " §c" + denied + "§r " + C("MsgNowDenied") + " " + Util().moneyFormat(-price, true));

                                    if (isAdvancedLogging()) {
                                        if (price == 0) {
                                            serverBridge.getLogger()
                                                    .info(player.getName() + " " + C("MsgDeniedPlayer") + " " + denied + " " + C("MsgToPlot") + " "
                                                          + id);
                                        } else {
                                            serverBridge.getLogger()
                                                    .info(player.getName() + " " + C("MsgDeniedPlayer") + " " + denied + " " + C("MsgToPlot") + " "
                                                          + id + (
                                                            " " + C("WordFor") + " " + price));
                                        }
                                    }
                                }
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedDeny"));
                        }
                    }
                } else {
                    player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
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
