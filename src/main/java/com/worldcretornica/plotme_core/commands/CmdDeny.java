package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.*;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddDeniedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.List;

public class CmdDeny extends PlotCommand {

    public CmdDeny(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission(PermissionNames.ADMIN_DENY) || player.hasPermission(PermissionNames.USER_DENY)) {
            World world = player.getWorld();
            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);
            if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                String id = PlotMeCoreManager.getPlotId(player);
                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!PlotMeCoreManager.isPlotAvailable(id, pmi)) {
                    if (args.length < 2 || args[1].isEmpty()) {
                        player.sendMessage(C("WordUsage") + " §c/plotme deny <" + C("WordPlayer") + ">");
                    } else {
                        Plot plot = PlotMeCoreManager.getPlotById(id, pmi);
                        String playername = player.getName();
                        String denied = args[1];

                        if (plot.getOwner().equalsIgnoreCase(playername) || player.hasPermission(PermissionNames.ADMIN_DENY)) {
                            if (plot.getOwner().equalsIgnoreCase(denied)) {
                                //TODO output something using a caption that says like "Cannot deny owner"
                                return true;
                            }

                            if (plot.isDeniedConsulting(denied) || plot.isGroupDenied(denied)) {
                                player.sendMessage(C("WordPlayer") + " §c" + args[1] + "§r " + C("MsgAlreadyDenied"));
                            } else {


                                double price = 0.0;

                                InternalPlotAddDeniedEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(pmi)) {
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
                                        player.sendMessage("§c" + C("MsgNotEnoughDeny") + " " + C("WordMissing") + " §r" + Util().moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = serverBridge.getEventFactory().callPlotAddDeniedEvent(plugin, world, plot, player, denied);
                                }

                                if (!event.isCancelled()) {
                                    plot.addDenied(denied);
                                    plot.removeAllowed(denied);

                                    if ("*".equals(denied)) {
                                        List<IPlayer> deniedplayers = PlotMeCoreManager.getPlayersInPlot(world, id);

                                        for (IPlayer deniedplayer : deniedplayers) {
                                            if (!plot.isAllowed(deniedplayer.getName(), deniedplayer.getUniqueId())) {
                                                deniedplayer.teleport(PlotMeCoreManager.getPlotHome(world, plot.getId()));
                                            }
                                        }
                                    } else {
                                        IPlayer deniedplayer = serverBridge.getPlayerExact(denied);

                                        if (deniedplayer != null) {
                                            if (deniedplayer.getWorld().equals(world)) {
                                                String deniedid = PlotMeCoreManager.getPlotId(deniedplayer);

                                                if (deniedid.equalsIgnoreCase(id)) {
                                                    deniedplayer.teleport(PlotMeCoreManager.getPlotHome(world, plot.getId()));
                                                }
                                            }
                                        }
                                    }

                                    double price1 = -price;
                                    player.sendMessage(C("WordPlayer") + " §c" + denied + "§r " + C("MsgNowDenied") + " " + Util().moneyFormat(price1, true));

                                    if (isAdvancedLogging()) {
                                        if (price == 0)
                                            serverBridge.getLogger().info(playername + " " + C("MsgDeniedPlayer") + " " + denied + " " + C("MsgToPlot") + " " + id);
                                        else
                                            serverBridge.getLogger().info(playername + " " + C("MsgDeniedPlayer") + " " + denied + " " + C("MsgToPlot") + " " + id + (" " + C("WordFor") + " " + price));
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
