package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
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
        if (player.hasPermission(PermissionNames.USER_AUTO)) {
            if (plugin.getPlotMeCoreManager().isPlotWorld(player) || serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                IWorld world;
                if (!plugin.getPlotMeCoreManager().isPlotWorld(player) && serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                    if (args.length == 2) {
                        world = serverBridge.getWorld(args[1]);
                    } else {
                        world = plugin.getPlotMeCoreManager().getFirstWorld();
                    }

                    if (!plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                        player.sendMessage("§c" + world + " " + C("MsgWorldNotPlot"));
                        return true;
                    }
                } else {
                    world = player.getWorld();
                }

                PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);
                int playerlimit = getPlotLimit(player);

                if (playerlimit != -1 && plugin.getPlotMeCoreManager().getNbOwnedPlot(player.getUniqueId(), player.getName(), world.getName()) >= playerlimit && !player.hasPermission("PlotMe.admin")) {
                    player.sendMessage("§c" + C("MsgAlreadyReachedMaxPlots") + " ("
                                               + plugin.getPlotMeCoreManager().getNbOwnedPlot(player.getUniqueId(), player.getName(), world.getName()) + "/" + playerlimit + "). " + C("WordUse") + " §c/plotme home§r " + C("MsgToGetToIt"));
                } else {
                    int limit = pmi.getPlotAutoLimit();

                    String next = pmi.getNextFreed();
                    String id = "";

                    if (PlotMeCoreManager.isPlotAvailable(next, pmi)) {
                        id = next;
                    } else {
                        int x = PlotMeCoreManager.getIdX(next);
                        int z = PlotMeCoreManager.getIdZ(next);

                        toploop:
                        for (int i = Math.max(Math.abs(x), Math.abs(z)); i < limit; ) {
                            for (; x <= i; x++) {
                                for (; z <= i; z++) {
                                    id = x + ";" + z;

                                    if (PlotMeCoreManager.isPlotAvailable(id, pmi)) {
                                        pmi.setNextFreed(id);
                                        break toploop;
                                    }
                                }
                            }

                            i++;
                            x = -i;
                            z = -i;

                            if (i >= limit) {
                                player.sendMessage("§c" + C("MsgNoPlotFound1") + " " + (limit ^ 2) + " " + C("MsgNoPlotFound2"));
                                return false;
                            }
                        }
                    }

                    double price = 0.0;

                    InternalPlotCreateEvent event;

                    if (plugin.getPlotMeCoreManager().isEconomyEnabled(pmi)) {
                        price = pmi.getClaimPrice();
                        double balance = serverBridge.getBalance(player);

                        if (balance >= price) {
                            event = serverBridge.getEventFactory().callPlotCreatedEvent(plugin, world, id, player);

                            if (event.isCancelled()) {
                                return true;
                            } else {
                                EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                if (!er.transactionSuccess()) {
                                    player.sendMessage("§c" + er.errorMessage);
                                    warn(er.errorMessage);
                                    return true;
                                }
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgNotEnoughAuto") + " " + C("WordMissing") + " §r" + Util().moneyFormat(price - balance, false));
                            return true;
                        }
                    } else {
                        event = serverBridge.getEventFactory().callPlotCreatedEvent(plugin, world, id, player);
                    }

                    if (!event.isCancelled()) {
                        plugin.getPlotMeCoreManager().createPlot(world, id, player.getName(), player.getUniqueId(), pmi);
                        pmi.removeFreed(id);

                        //plugin.getPlotMeCoreManager().adjustLinkedPlots(id, world);
                        player.teleport(PlotMeCoreManager.getPlotHome(world, id));

                        player.sendMessage(C("MsgThisPlotYours") + " " + C("WordUse") + " §c/plotme home§r " + C("MsgToGetToIt"));

                        if (isAdvancedLogging()) {
                            if (price == 0)
                                serverBridge.getLogger().info(player.getName() + " " + C("MsgClaimedPlot") + " " + id);
                            else
                                serverBridge.getLogger().info(player.getName() + " " + C("MsgClaimedPlot") + " " + id + (" " + C("WordFor") + " " + price));
                        }

                    }
                    return true;
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
