package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
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
        if (player.hasPermission("PlotMe.admin.deny") || player.hasPermission("PlotMe.use.deny")) {
            IWorld world = player.getWorld();
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

                        if (plot.getOwner().equalsIgnoreCase(playername) || player.hasPermission("PlotMe.admin.deny")) {
                            if (plot.getOwner().equalsIgnoreCase(denied)) {
                                //TODO output something using a caption that says like "Cannot deny owner"
                                return true;
                            }

                            if (plot.isDeniedConsulting(denied) || plot.isGroupDenied(denied)) {
                                player.sendMessage(C("WordPlayer") + " §c" + args[1] + "§r " + C("MsgAlreadyDenied"));
                            } else {


                                double price = 0;

                                InternalPlotAddDeniedEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(pmi)) {
                                    price = pmi.getDenyPlayerPrice();
                                    double balance = sob.getBalance(player);

                                    if (balance >= price) {
                                        event = sob.getEventFactory().callPlotAddDeniedEvent(plugin, world, plot, player, denied);

                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = sob.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage("§c" + er.errorMessage);
                                                warn(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage("§c" + C("MsgNotEnoughDeny") + " " + C("WordMissing") + " §r" + Util().moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = sob.getEventFactory().callPlotAddDeniedEvent(plugin, world, plot, player, denied);
                                }

                                if (!event.isCancelled()) {
                                    plot.addDenied(denied);
                                    plot.removeAllowed(denied);

                                    if ("*".equals(denied)) {
                                        List<IPlayer> deniedplayers = PlotMeCoreManager.getPlayersInPlot(world, id);

                                        for (IPlayer deniedplayer : deniedplayers) {
                                            if (!plot.isAllowed(deniedplayer.getUniqueId())) {
                                                deniedplayer.teleport(PlotMeCoreManager.getPlotHome(world, plot.getId()));
                                            }
                                        }
                                    } else {
                                        IPlayer deniedplayer = sob.getPlayerExact(denied);

                                        if (deniedplayer != null) {
                                            if (deniedplayer.getWorld().equals(world)) {
                                                String deniedid = PlotMeCoreManager.getPlotId(deniedplayer);

                                                if (deniedid.equalsIgnoreCase(id)) {
                                                    deniedplayer.teleport(PlotMeCoreManager.getPlotHome(world, plot.getId()));
                                                }
                                            }
                                        }
                                    }

                                    player.sendMessage(C("WordPlayer") + " §c" + denied + "§r " + C("MsgNowDenied") + " " + Util().moneyFormat(-price));

                                    if (isAdvancedLogging()) {
                                        sob.getLogger().info(LOG + playername + " " + C("MsgDeniedPlayer") + " " + denied + " " + C("MsgToPlot") + " " + id + (price != 0 ? " " + C("WordFor") + " " + price : ""));
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
