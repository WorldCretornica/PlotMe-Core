package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotCreateEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdAuto extends PlotCommand {

    public CmdAuto(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (PlotMe_Core.cPerms(player, "PlotMe.use.auto")) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player) || sob.getConfig().getBoolean("allowWorldTeleport")) {
                IWorld world;
                if (!plugin.getPlotMeCoreManager().isPlotWorld(player) && sob.getConfig().getBoolean("allowWorldTeleport")) {
                    if (args.length == 2) {
                        world = sob.getWorld(args[1]);
                    } else {
                        world = plugin.getPlotMeCoreManager().getFirstWorld();
                    }

                    if (!plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                        player.sendMessage(RED + args[1] + " " + C("MsgWorldNotPlot"));
                        return true;
                    }
                } else {
                    world = player.getWorld();
                }

                int playerlimit = plugin.getPlotLimit(player);

                if (playerlimit != -1 && plugin.getPlotMeCoreManager().getNbOwnedPlot(player, world) >= playerlimit && !PlotMe_Core.cPerms(player, "PlotMe.admin")) {
                    player.sendMessage(RED + C("MsgAlreadyReachedMaxPlots") + " ("
                                               + plugin.getPlotMeCoreManager().getNbOwnedPlot(player, world) + "/" + playerlimit + "). " + C("WordUse") + " " + RED + "/plotme " + C("CommandHome") + RESET + " " + C("MsgToGetToIt"));
                } else {
                    PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);
                    int limit = pmi.getPlotAutoLimit();

                    String next = pmi.getNextFreed();
                    String id = "";

                    if (plugin.getPlotMeCoreManager().isPlotAvailable(next, world)) {
                        id = next;
                    } else {
                        int x = PlotMeCoreManager.getIdX(next);
                        int z = PlotMeCoreManager.getIdZ(next);

                        toploop:
                        for (int i = Math.max(Math.abs(x), Math.abs(z)); i < limit; ) {
                            for (; x <= i; x++) {
                                for (; z <= i; z++) {
                                    id = "" + x + ";" + z;

                                    if (plugin.getPlotMeCoreManager().isPlotAvailable(id, world)) {
                                        pmi.setNextFreed(id);
                                        break toploop;
                                    }
                                }
                            }

                            i++;
                            x = -i;
                            z = -i;

                            if (i >= limit) {
                                player.sendMessage(RED + C("MsgNoPlotFound1") + " " + (limit ^ 2) + " " + C("MsgNoPlotFound2"));
                                return false;
                            }
                        }
                    }

                    double price = 0;

                    InternalPlotCreateEvent event;

                    if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                        price = pmi.getClaimPrice();
                        double balance = sob.getBalance(player);

                        if (balance >= price) {
                            event = sob.getEventFactory().callPlotCreatedEvent(plugin, world, id, player);

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
                            player.sendMessage(RED + C("MsgNotEnoughAuto") + " " + C("WordMissing") + " " + RESET + Util().moneyFormat(price - balance, false));
                            return true;
                        }
                    } else {
                        event = sob.getEventFactory().callPlotCreatedEvent(plugin, world, id, player);
                    }

                    if (!event.isCancelled()) {
                        plugin.getPlotMeCoreManager().createPlot(world, id, player.getName(), player.getUniqueId());
                        pmi.removeFreed(id);

                        //plugin.getPlotMeCoreManager().adjustLinkedPlots(id, world);
                        player.teleport(plugin.getPlotMeCoreManager().getPlotHome(world, id));

                        player.sendMessage(C("MsgThisPlotYours") + " " + C("WordUse") + " " + RED + "/plotme " + C("CommandHome") + RESET + " " + C("MsgToGetToIt") + " " + Util().moneyFormat(-price));

                        if (isAdvancedLogging()) {
                            plugin.getLogger().info(LOG + player.getName() + " " + C("MsgClaimedPlot") + " " + id + (price != 0 ? " " + C("WordFor") + " " + price : ""));
                        }

                    }
                    return true;
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
