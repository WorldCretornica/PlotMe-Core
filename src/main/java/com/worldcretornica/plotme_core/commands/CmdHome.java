package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotTeleportHomeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.UUID;

public class CmdHome extends PlotCommand {

    public CmdHome(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.use.home") || plugin.cPerms(p, "PlotMe.admin.home.other")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(p) || sob.getConfig().getBoolean("allowWorldTeleport")) {
                String playername = p.getName();
                UUID uuid = p.getUniqueId();
                IWorld w;

                if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                    w = p.getWorld();
                } else {
                    w = plugin.getPlotMeCoreManager().getFirstWorld();
                }

                String worldname = "";
                if (w != null) {
                    worldname = w.getName();
                }

                int nb = 1;
                if (args[0].contains(":")) {
                    try {
                        if (args[0].split(":").length == 1 || args[0].split(":")[1].isEmpty()) {
                            p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandHome") + ":# " + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandHome") + ":1");
                            return true;
                        } else {
                            nb = Integer.parseInt(args[0].split(":")[1]);
                        }
                    } catch (Exception ex) {
                        p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandHome") + ":# " + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandHome") + ":1");
                        return true;
                    }
                }

                if (args.length >= 2) {
                    if (sob.getWorld(args[1]) == null) {
                        if (plugin.cPerms(p, "PlotMe.admin.home.other")) {
                            playername = args[1];
                            uuid = null;
                        }
                    } else {
                        w = sob.getWorld(args[1]);
                    }
                }

                if (args.length == 3) {
                    if (sob.getWorld(args[2]) == null) {
                        p.sendMessage(RED + args[2] + C("MsgWorldNotPlot"));
                        return true;
                    } else {
                        w = sob.getWorld(args[2]);
                        worldname = args[2];
                    }
                }

                if (plugin.getPlotMeCoreManager().isPlotWorld(w)) {
                    int i = nb - 1;

                    for (Plot plot : plugin.getSqlManager().getOwnedPlots(w.getName(), uuid, playername)) {
                        if (uuid == null && plot.getOwner().equalsIgnoreCase(playername) || uuid != null && plot.getOwnerId() != null && plot.getOwnerId().equals(uuid)) {
                            if (i == 0) {
                                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(w);

                                double price = 0;

                                InternalPlotTeleportHomeEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(w)) {
                                    price = pmi.getPlotHomePrice();
                                    double balance = sob.getBalance(p);

                                    if (balance >= price) {
                                        event = sob.getEventFactory().callPlotTeleportHomeEvent(plugin, w, plot, p);

                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = sob.withdrawPlayer(p, price);

                                            if (!er.transactionSuccess()) {
                                                p.sendMessage(RED + er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        p.sendMessage(RED + C("MsgNotEnoughTp") + " " + C("WordMissing") + " " + RESET + Util().moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = sob.getEventFactory().callPlotTeleportHomeEvent(plugin, w, plot, p);
                                }

                                if (!event.isCancelled()) {
                                    p.teleport(event.getHomeLocation());

                                    if (price != 0) {
                                        p.sendMessage(Util().moneyFormat(-price));
                                    }
                                }
                                return true;
                            } else {
                                i--;
                            }
                        }
                    }

                    if (nb > 0) {
                        if (playername.equalsIgnoreCase(p.getName())) {
                            p.sendMessage(RED + C("MsgPlotNotFound") + " #" + nb);
                        } else {
                            p.sendMessage(RED + playername + " " + C("MsgDoesNotHavePlot") + " #" + nb);
                        }
                    } else if (!playername.equalsIgnoreCase(p.getName())) {
                        p.sendMessage(RED + playername + " " + C("MsgDoesNotHavePlot"));
                    } else {
                        p.sendMessage(RED + C("MsgYouHaveNoPlot"));
                    }
                } else {
                    p.sendMessage(RED + worldname + C("MsgWorldNotPlot"));
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
