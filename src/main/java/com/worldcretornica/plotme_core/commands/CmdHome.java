package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.event.PlotMeEventFactory;
import com.worldcretornica.plotme_core.event.PlotTeleportHomeEvent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CmdHome extends PlotCommand {

    public CmdHome(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.use.home") || plugin.cPerms(p, "PlotMe.admin.home.other")) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(p) && !plugin.getConfig().getBoolean("allowWorldTeleport")) {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
            } else {
                String playername = p.getName();
                int nb = 1;
                World w;

                if (!plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                    w = plugin.getPlotMeCoreManager().getFirstWorld();
                } else {
                    w = p.getWorld();
                }

                if (args[0].contains(":")) {
                    try {
                        if (args[0].split(":").length == 1 || args[0].split(":")[1].equals("")) {
                            p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandHome") + ":# "
                                    + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandHome") + ":1");
                            return true;
                        } else {
                            nb = Integer.parseInt(args[0].split(":")[1]);
                        }
                    } catch (Exception ex) {
                        p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandHome") + ":# "
                                + RESET + C("WordExample") + ": " + RED + "/plotme " + C("CommandHome") + ":1");
                        return true;
                    }
                }

                if (args.length >= 2) {
                    if (Bukkit.getWorld(args[1]) == null) {
                        if (plugin.cPerms(p, "PlotMe.admin.home.other")) {
                            playername = args[1];
                        }
                    } else {
                        w = Bukkit.getWorld(args[1]);
                    }
                }

                if (args.length == 3) {
                    if (Bukkit.getWorld(args[2]) == null) {
                        p.sendMessage(RED + args[2] + C("MsgWorldNotPlot"));
                        return true;
                    } else {
                        w = Bukkit.getWorld(args[2]);
                    }
                }

                if (!plugin.getPlotMeCoreManager().isPlotWorld(w)) {
                    p.sendMessage(RED + w.getName() + C("MsgWorldNotPlot"));
                } else {
                    int i = nb - 1;

                    for (Plot plot : plugin.getSqlManager().getOwnedPlots(w.getName(), playername)) {
                        if (plot.getOwner().equalsIgnoreCase(playername)) {
                            if (i == 0) {
                                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(w);

                                double price = 0;

                                PlotTeleportHomeEvent event;

                                if (plugin.getPlotMeCoreManager().isEconomyEnabled(w)) {
                                    price = pmi.getPlotHomePrice();
                                    double balance = plugin.getEconomy().getBalance(playername);

                                    if (balance >= price) {
                                        event = PlotMeEventFactory.callPlotTeleportHomeEvent(plugin, w, plot, p);

                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = plugin.getEconomy().withdrawPlayer(playername, price);

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
                                    event = PlotMeEventFactory.callPlotTeleportHomeEvent(plugin, w, plot, p);
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
                        if (!playername.equalsIgnoreCase(p.getName())) {
                            p.sendMessage(RED + playername + " " + C("MsgDoesNotHavePlot") + " #" + nb);
                        } else {
                            p.sendMessage(RED + C("MsgPlotNotFound") + " #" + nb);
                        }
                    } else if (!playername.equalsIgnoreCase(p.getName())) {
                        p.sendMessage(RED + playername + " " + C("MsgDoesNotHavePlot"));
                    } else {
                        p.sendMessage(RED + C("MsgYouHaveNoPlot"));
                    }
                }
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
