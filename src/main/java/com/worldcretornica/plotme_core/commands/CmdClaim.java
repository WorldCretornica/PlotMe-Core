package com.worldcretornica.plotme_core.commands;

import java.util.UUID;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.event.PlotCreateEvent;
import com.worldcretornica.plotme_core.event.PlotMeEventFactory;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class CmdClaim extends PlotCommand {

    public CmdClaim(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.use.claim") || plugin.cPerms(p, "PlotMe.admin.claim.other")) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
            } else {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());

                if (id.equals("")) {
                    p.sendMessage(RED + C("MsgCannotClaimRoad"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    p.sendMessage(RED + C("MsgThisPlotOwned"));
                } else {
                    String playername = p.getName();
                    UUID uuid = p.getUniqueId();

                    if (args.length == 2) {
                        if (plugin.cPerms(p, "PlotMe.admin.claim.other")) {
                            playername = args[1];
                            uuid = null;
                        }
                    }

                    int plotlimit = plugin.getPlotLimit(p);

                    if (playername == p.getName() && plotlimit != -1 && plugin.getPlotMeCoreManager().getNbOwnedPlot(p) >= plotlimit) {
                        p.sendMessage(RED + C("MsgAlreadyReachedMaxPlots") + " ("
                                + plugin.getPlotMeCoreManager().getNbOwnedPlot(p) + "/" + plugin.getPlotLimit(p) + "). " + C("WordUse") + " " + RED + "/plotme " + C("CommandHome") + RESET + " " + C("MsgToGetToIt"));
                    } else {
                        World w = p.getWorld();
                        PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(w);

                        double price = 0;

                        PlotCreateEvent event;

                        if (plugin.getPlotMeCoreManager().isEconomyEnabled(w)) {
                            price = pmi.getClaimPrice();
                            double balance = plugin.getEconomy().getBalance(p);

                            if (balance >= price) {
                                event = PlotMeEventFactory.callPlotCreatedEvent(plugin, w, id, p);

                                if (event.isCancelled()) {
                                    return true;
                                } else {
                                    EconomyResponse er = plugin.getEconomy().withdrawPlayer(p, price);

                                    if (!er.transactionSuccess()) {
                                        p.sendMessage(RED + er.errorMessage);
                                        Util().warn(er.errorMessage);
                                        return true;
                                    }
                                }
                            } else {
                                p.sendMessage(RED + C("MsgNotEnoughBuy") + " " + C("WordMissing") + " " + RESET + (price - balance) + RED + " " + plugin.getEconomy().currencyNamePlural());
                                return true;
                            }
                        } else {
                            event = PlotMeEventFactory.callPlotCreatedEvent(plugin, w, id, p);
                        }

                        if (!event.isCancelled()) {
                            Plot plot = plugin.getPlotMeCoreManager().createPlot(w, id, playername, uuid);

                            //plugin.getPlotMeCoreManager().adjustLinkedPlots(id, w);
                            if (plot == null) {
                                p.sendMessage(RED + C("ErrCreatingPlotAt") + " " + id);
                            } else {
                                if (playername.equalsIgnoreCase(p.getName())) {
                                    p.sendMessage(C("MsgThisPlotYours") + " " + C("WordUse") + " " + RED + "/plotme " + C("CommandHome") + RESET + " " + C("MsgToGetToIt") + " " + Util().moneyFormat(-price));
                                } else {
                                    p.sendMessage(C("MsgThisPlotIsNow") + " " + playername + C("WordPossessive") + ". " + C("WordUse") + " " + RED + "/plotme " + C("CommandHome") + RESET + " " + C("MsgToGetToIt") + " " + Util().moneyFormat(-price));
                                }

                                plugin.getLogger().info(LOG + playername + " " + C("MsgClaimedPlot") + " " + id + ((price != 0) ? " " + C("WordFor") + " " + price : ""));
                            }
                        }
                    }
                }
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
