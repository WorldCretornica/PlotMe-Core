package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.event.PlotBuyEvent;
import com.worldcretornica.plotme_core.event.PlotMeEventFactory;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CmdBuy extends PlotCommand {

    public CmdBuy(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player p, String[] args) {
        if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
            if (plugin.cPerms(p, "PlotMe.use.buy") || plugin.cPerms(p, "PlotMe.admin.buy")) {
                Location l = p.getLocation();
                String id = plugin.getPlotMeCoreManager().getPlotId(l);

                if (id.equals("")) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                    if (!plot.isForSale()) {
                        p.sendMessage(RED + C("MsgPlotNotForSale"));
                    } else {
                        String buyer = p.getName();

                        if (plot.getOwner().equalsIgnoreCase(buyer)) {
                            p.sendMessage(RED + C("MsgCannotBuyOwnPlot"));
                        } else {
                            int plotlimit = plugin.getPlotLimit(p);

                            if (plotlimit != -1 && plugin.getPlotMeCoreManager().getNbOwnedPlot(p) >= plotlimit) {
                                p.sendMessage(C("MsgAlreadyReachedMaxPlots") + " ("
                                                      + plugin.getPlotMeCoreManager().getNbOwnedPlot(p) + "/" + plugin.getPlotLimit(p) + "). "
                                                      + C("WordUse") + " " + RED + "/plotme " + C("CommandHome") + RESET + " " + C("MsgToGetToIt"));
                            } else {
                                World w = p.getWorld();

                                double cost = plot.getCustomPrice();

                                if (plugin.getEconomy().getBalance(buyer) < cost) {
                                    p.sendMessage(RED + C("MsgNotEnoughBuy"));
                                } else {

                                    PlotBuyEvent event = PlotMeEventFactory.callPlotBuyEvent(plugin, w, plot, p, cost);

                                    if (!event.isCancelled()) {
                                        EconomyResponse er = plugin.getEconomy().withdrawPlayer(buyer, cost);

                                        if (er.transactionSuccess()) {
                                            String oldowner = plot.getOwner();

                                            if (!oldowner.equalsIgnoreCase("$Bank$")) {
                                                EconomyResponse er2 = plugin.getEconomy().depositPlayer(oldowner, cost);

                                                if (!er2.transactionSuccess()) {
                                                    p.sendMessage(RED + er2.errorMessage);
                                                    Util().warn(er2.errorMessage);
                                                } else {
                                                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                                                        if (player.getName().equalsIgnoreCase(oldowner)) {
                                                            player.sendMessage(C("WordPlot") + " " + id + " "
                                                                                       + C("MsgSoldTo") + " " + buyer + ". " + Util().moneyFormat(cost));
                                                            break;
                                                        }
                                                    }
                                                }
                                            }

                                            plot.setOwner(buyer);
                                            plot.setCustomPrice(0);
                                            plot.setForSale(false);

                                            plot.updateField("owner", buyer);
                                            plot.updateField("customprice", 0);
                                            plot.updateField("forsale", false);

                                            plugin.getPlotMeCoreManager().adjustWall(l);
                                            plugin.getPlotMeCoreManager().setSellSign(w, plot);
                                            plugin.getPlotMeCoreManager().setOwnerSign(w, plot);

                                            p.sendMessage(C("MsgPlotBought") + " " + Util().moneyFormat(-cost));

                                            plugin.getLogger().info(LOG + buyer + " " + C("MsgBoughtPlot") + " " + id + " " + C("WordFor") + " " + cost);
                                        } else {
                                            p.sendMessage(RED + er.errorMessage);
                                            Util().warn(er.errorMessage);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                p.sendMessage(RED + C("MsgPermissionDenied"));
            }
        } else {
            p.sendMessage(RED + C("MsgEconomyDisabledWorld"));
        }
        return true;
    }
}
