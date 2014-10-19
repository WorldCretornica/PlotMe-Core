package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotBuyEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdBuy extends PlotCommand {

    public CmdBuy(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p) {
        if (plugin.getPlotMeCoreManager().isEconomyEnabled(p)) {
            if (plugin.cPerms(p, "PlotMe.use.buy") || plugin.cPerms(p, "PlotMe.admin.buy")) {
                ILocation l = p.getLocation();
                String id = plugin.getPlotMeCoreManager().getPlotId(l);

                if (id.isEmpty()) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                    if (plot.isForSale()) {
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
                                IWorld world = p.getWorld();

                                double cost = plot.getCustomPrice();

                                if (sob.getBalance(p) < cost) {
                                    p.sendMessage(RED + C("MsgNotEnoughBuy"));
                                } else {

                                    InternalPlotBuyEvent event = sob.getEventFactory().callPlotBuyEvent(plugin, world, plot, p, cost);

                                    if (!event.isCancelled()) {
                                        EconomyResponse er = sob.withdrawPlayer(p, cost);

                                        if (er.transactionSuccess()) {
                                            String oldowner = plot.getOwner();
                                            IOfflinePlayer playercurrentbidder = null;

                                            if (plot.getOwnerId() != null) {
                                                playercurrentbidder = sob.getOfflinePlayer(plot.getOwnerId());
                                            }

                                            if (!oldowner.equalsIgnoreCase("$Bank$") && playercurrentbidder != null) {
                                                EconomyResponse er2 = sob.depositPlayer(playercurrentbidder, cost);

                                                if (er2.transactionSuccess()) {
                                                    for (IPlayer player : sob.getOnlinePlayers()) {
                                                        if (player.getName().equalsIgnoreCase(oldowner)) {
                                                            player.sendMessage(C("WordPlot") + " " + id + " "
                                                                                       + C("MsgSoldTo") + " " + buyer + ". " + Util().moneyFormat(cost));
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    p.sendMessage(RED + er2.errorMessage);
                                                    Util().warn(er2.errorMessage);
                                                }
                                            }

                                            plot.setOwner(buyer);
                                            plot.setCustomPrice(0);
                                            plot.setForSale(false);

                                            plot.updateField("owner", buyer);
                                            plot.updateField("customprice", 0);
                                            plot.updateField("forsale", false);

                                            plugin.getPlotMeCoreManager().adjustWall(l);
                                            plugin.getPlotMeCoreManager().setSellSign(world, plot);
                                            plugin.getPlotMeCoreManager().setOwnerSign(world, plot);

                                            p.sendMessage(C("MsgPlotBought") + " " + Util().moneyFormat(-cost));

                                            if (isAdvancedLogging()) {
                                                plugin.getLogger().info(LOG + buyer + " " + C("MsgBoughtPlot") + " " + id + " " + C("WordFor") + " " + cost);
                                            }
                                        } else {
                                            p.sendMessage(RED + er.errorMessage);
                                            Util().warn(er.errorMessage);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        p.sendMessage(RED + C("MsgPlotNotForSale"));
                    }
                } else {
                    p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                p.sendMessage(RED + C("MsgPermissionDenied"));
                return false;
            }
        } else {
            p.sendMessage(RED + C("MsgEconomyDisabledWorld"));
        }
        return true;
    }
}
