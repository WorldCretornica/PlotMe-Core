package com.worldcretornica.plotme_core.commands;

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
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.deny") || PlotMe_Core.cPerms(player, "PlotMe.use.deny")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(player.getLocation());
                if (id.isEmpty()) {
                    player.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, player)) {
                    if (args.length < 2 || args[1].isEmpty()) {
                        player.sendMessage(C("WordUsage") + " " + RED + "/plotme " + C("CommandDeny") + " <" + C("WordPlayer") + ">");
                    } else {

                        Plot plot = plugin.getPlotMeCoreManager().getPlotById(player, id);
                        String playername = player.getName();
                        String denied = args[1];

                        if (plot.getOwner().equalsIgnoreCase(playername) || PlotMe_Core.cPerms(player, "PlotMe.admin.deny")) {
                            if (plot.getOwner().equalsIgnoreCase(denied)) {
                                //TODO output something using a caption that says like "Cannot deny owner"
                                return true;
                            }

                            if (plot.isDeniedConsulting(denied) || plot.isGroupDenied(denied)) {
                                player.sendMessage(C("WordPlayer") + " " + RED + args[1] + RESET + " " + C("MsgAlreadyDenied"));
                            } else {
                                IWorld world = player.getWorld();

                                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);

                                double price = 0;

                                InternalPlotAddDeniedEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                                    price = pmi.getDenyPlayerPrice();
                                    double balance = sob.getBalance(player);

                                    if (balance >= price) {
                                        event = sob.getEventFactory().callPlotAddDeniedEvent(plugin, world, plot, player, denied);

                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = sob.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage(RED + er.errorMessage);
                                                Util().warn(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage(RED + C("MsgNotEnoughDeny") + " " + C("WordMissing") + " " + RESET + Util().moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = sob.getEventFactory().callPlotAddDeniedEvent(plugin, world, plot, player, denied);
                                }

                                if (!event.isCancelled()) {
                                    plot.addDenied(denied);
                                    plot.removeAllowed(denied);

                                    if (denied.equals("*")) {
                                        List<IPlayer> deniedplayers = plugin.getPlotMeCoreManager().getPlayersInPlot(world, id);

                                        for (IPlayer deniedplayer : deniedplayers) {
                                            if (!plot.isAllowed(deniedplayer.getUniqueId())) {
                                                deniedplayer.teleport(plugin.getPlotMeCoreManager().getPlotHome(world, plot.getId()));
                                            }
                                        }
                                    } else {
                                        IPlayer deniedplayer = sob.getPlayerExact(denied);

                                        if (deniedplayer != null) {
                                            if (deniedplayer.getWorld().equals(world)) {
                                                String deniedid = plugin.getPlotMeCoreManager().getPlotId(deniedplayer.getLocation());

                                                if (deniedid.equalsIgnoreCase(id)) {
                                                    deniedplayer.teleport(plugin.getPlotMeCoreManager().getPlotHome(world, plot.getId()));
                                                }
                                            }
                                        }
                                    }

                                    player.sendMessage(C("WordPlayer") + " " + RED + denied + RESET + " " + C("MsgNowDenied") + " " + Util().moneyFormat(-price));

                                    if (isAdvancedLogging()) {
                                        plugin.getLogger().info(LOG + playername + " " + C("MsgDeniedPlayer") + " " + denied + " " + C("MsgToPlot") + " " + id + (price != 0 ? " " + C("WordFor") + " " + price : ""));
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedDeny"));
                        }
                    }
                } else {
                    player.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                player.sendMessage(RED + C("MsgNotPlotWorld"));
            }
        } else {
            player.sendMessage(RED + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
