package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotCommentEvent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.commons.lang.StringUtils;

import java.util.UUID;

public class CmdComment extends PlotCommand {

    public CmdComment(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission("PlotMe.use.comment")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                if (args.length < 2) {
                    player.sendMessage(C("WordUsage") + ": §c/plotme comment <" + C("WordText") + ">");
                } else {
                    String id = PlotMeCoreManager.getPlotId(player);

                    if (id.isEmpty()) {
                        player.sendMessage("§c" + C("MsgNoPlotFound"));
                    } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, player)) {
                        IWorld w = player.getWorld();
                        PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(w);

                        String text = StringUtils.join(args, " ");
                        text = text.substring(text.indexOf(" "));

                        double price = 0;

                        Plot plot = plugin.getPlotMeCoreManager().getPlotById(player, id);

                        InternalPlotCommentEvent event;

                        if (plugin.getPlotMeCoreManager().isEconomyEnabled(player)) {
                            price = pmi.getAddCommentPrice();
                            double balance = sob.getBalance(player);

                            if (balance >= price) {
                                event = sob.getEventFactory().callPlotCommentEvent(plugin, player.getWorld(), plot, player, text);

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
                                player.sendMessage("§c" + C("MsgNotEnoughComment") + " " + C("WordMissing") + " §r" + Util().moneyFormat(price - balance, false));
                                return true;
                            }
                        } else {
                            event = sob.getEventFactory().callPlotCommentEvent(plugin, player.getWorld(), plot, player, text);
                        }

                        if (!event.isCancelled()) {
                            String playername = player.getName();
                            UUID uuid = player.getUniqueId();

                            String[] comment = new String[3];
                            comment[0] = playername;
                            comment[1] = text;
                            comment[2] = uuid.toString();

                            plot.addComment(comment);
                            plugin.getSqlManager().addPlotComment(comment, plot.getCommentsCount(), PlotMeCoreManager.getIdX(id), PlotMeCoreManager.getIdZ(id), plot.getWorld(), uuid);

                            player.sendMessage(C("MsgCommentAdded") + " " + Util().moneyFormat(-price));

                            if (isAdvancedLogging()) {
                                plugin.getLogger().info(LOG + playername + " " + C("MsgCommentedPlot") + " " + id + (price != 0 ? " " + C("WordFor") + " " + price : ""));
                            }
                        }
                    } else {
                        player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                    }
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
