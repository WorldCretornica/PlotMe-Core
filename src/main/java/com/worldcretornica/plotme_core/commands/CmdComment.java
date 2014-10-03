package com.worldcretornica.plotme_core.commands;

import java.util.UUID;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.api.event.InternalPlotCommentEvent;

import net.milkbowl.vault.economy.EconomyResponse;

import org.apache.commons.lang.StringUtils;

public class CmdComment extends PlotCommand {

    public CmdComment(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.use.comment")) {
            if (!plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                p.sendMessage(RED + C("MsgNotPlotWorld"));
            } else if (args.length < 2) {
                p.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandComment") + " <" + C("WordText") + ">");
            } else {
                String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());

                if (id.isEmpty()) {
                    p.sendMessage(RED + C("MsgNoPlotFound"));
                } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, p)) {
                    IWorld w = p.getWorld();
                    PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(w);

                    String text = StringUtils.join(args, " ");
                    text = text.substring(text.indexOf(" "));

                    double price = 0;

                    Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                    InternalPlotCommentEvent event;

                    if (plugin.getPlotMeCoreManager().isEconomyEnabled(w)) {
                        price = pmi.getAddCommentPrice();
                        double balance = sob.getBalance(p);

                        if (balance >= price) {
                            event = sob.getEventFactory().callPlotCommentEvent(plugin, p.getWorld(), plot, p, text);

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
                            p.sendMessage(RED + C("MsgNotEnoughComment") + " " + C("WordMissing") + " " + RESET + Util().moneyFormat(price - balance, false));
                            return true;
                        }
                    } else {
                        event = sob.getEventFactory().callPlotCommentEvent(plugin, p.getWorld(), plot, p, text);
                    }

                    if (!event.isCancelled()) {
                        String playername = p.getName();
                        UUID uuid = p.getUniqueId();
                        
                        String[] comment = new String[3];
                        comment[0] = playername;
                        comment[1] = text;
                        comment[2] = uuid.toString();

                        plot.addComment(comment);
                        plugin.getSqlManager().addPlotComment(comment, plot.getCommentsCount(), plugin.getPlotMeCoreManager().getIdX(id), plugin.getPlotMeCoreManager().getIdZ(id), plot.getWorld(), uuid);

                        p.sendMessage(C("MsgCommentAdded") + " " + Util().moneyFormat(-price));

                        if (isAdvancedLogging()) {
                            plugin.getLogger().info(LOG + playername + " " + C("MsgCommentedPlot") + " " + id + ((price != 0) ? " " + C("WordFor") + " " + price : ""));
                        }
                    }
                } else {
                    p.sendMessage(RED + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
