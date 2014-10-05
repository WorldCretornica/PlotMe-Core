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

    public boolean exec(IPlayer p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.admin.deny") || plugin.cPerms(p, "PlotMe.use.deny")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());
                if (id.isEmpty()) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    if (args.length < 2 || args[1].isEmpty()) {
                        p.sendMessage(C("WordUsage") + " " + RED + "/plotme " + C("CommandDeny") + " <" + C("WordPlayer") + ">");
                    } else {

                        Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);
                        String playername = p.getName();
                        String denied = args[1];

                        if (plot.getOwner().equalsIgnoreCase(playername) || plugin.cPerms(p, "PlotMe.admin.deny")) {
                            if (plot.getOwner().equalsIgnoreCase(denied)) {
                                //TODO output something using a caption that says like "Cannot deny owner"
                                return true;
                            }

                            if (plot.isDeniedConsulting(denied) || plot.isGroupDenied(denied)) {
                                p.sendMessage(C("WordPlayer") + " " + RED + args[1] + RESET + " " + C("MsgAlreadyDenied"));
                            } else {
                                IWorld w = p.getWorld();

                                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(w);

                                double price = 0;

                                InternalPlotAddDeniedEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(w)) {
                                    price = pmi.getDenyPlayerPrice();
                                    double balance = sob.getBalance(p);

                                    if (balance >= price) {
                                        event = sob.getEventFactory().callPlotAddDeniedEvent(plugin, w, plot, p, denied);

                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = sob.withdrawPlayer(p, price);

                                            if (!er.transactionSuccess()) {
                                                p.sendMessage(RED + er.errorMessage);
                                                Util().warn(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        p.sendMessage(RED + C("MsgNotEnoughDeny") + " " + C("WordMissing") + " " + RESET + Util().moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = sob.getEventFactory().callPlotAddDeniedEvent(plugin, w, plot, p, denied);
                                }

                                if (!event.isCancelled()) {
                                    plot.addDenied(denied);
                                    plot.removeAllowed(denied);

                                    if (denied.equals("*")) {
                                        List<IPlayer> deniedplayers = plugin.getPlotMeCoreManager().getPlayersInPlot(w, id);

                                        for (IPlayer deniedplayer : deniedplayers) {
                                            if (!plot.isAllowed(deniedplayer.getUniqueId())) {
                                                deniedplayer.teleport(plugin.getPlotMeCoreManager().getPlotHome(w, plot.getId()));
                                            }
                                        }
                                    } else {
                                        IPlayer deniedplayer = sob.getPlayerExact(denied);

                                        if (deniedplayer != null) {
                                            if (deniedplayer.getWorld().equals(w)) {
                                                String deniedid = plugin.getPlotMeCoreManager().getPlotId(deniedplayer.getLocation());

                                                if (deniedid.equalsIgnoreCase(id)) {
                                                    deniedplayer.teleport(plugin.getPlotMeCoreManager().getPlotHome(w, plot.getId()));
                                                }
                                            }
                                        }
                                    }

                                    p.sendMessage(C("WordPlayer") + " " + RED + denied + RESET + " " + C("MsgNowDenied") + " " + Util().moneyFormat(-price));

                                    if (isAdvancedLogging()) {
                                        plugin.getLogger().info(LOG + playername + " " + C("MsgDeniedPlayer") + " " + denied + " " + C("MsgToPlot") + " " + id + (price != 0 ? " " + C("WordFor") + " " + price : ""));
                                    }
                                }
                            }
                        } else {
                            p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedDeny"));
                        }
                    }
                } else {
                    p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
