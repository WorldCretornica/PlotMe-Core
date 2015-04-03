package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotCreateEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.UUID;

public class CmdClaim extends PlotCommand {

    public CmdClaim(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "claim";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.USER_CLAIM) || player.hasPermission(PermissionNames.ADMIN_CLAIM_OTHER)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);

                if (id == null) {
                    player.sendMessage(C("MsgCannotClaimRoad"));
                    return true;
                }
                if (!manager.isPlotAvailable(id, pmi)) {
                    player.sendMessage(C("MsgThisPlotOwned"));
                    return true;
                }
                String playerName = player.getName();
                UUID playerUniqueId = player.getUniqueId();

                if (args.length == 2 && player.hasPermission(PermissionNames.ADMIN_CLAIM_OTHER)) {
                    if (args[1].length() > 16 || !validUserPattern2.matcher(args[1]).matches()) {
                        throw new IllegalArgumentException(C("InvalidCommandInput"));
                    }
                    IOfflinePlayer offlinePlayer = serverBridge.getOfflinePlayer(args[1]);
                    playerName = args[1];
                    playerUniqueId = offlinePlayer.getUniqueId();
                }

                int plotLimit = getPlotLimit(player);

                int plotsOwned = manager.getOwnedPlotCount(player.getUniqueId(), world.getName().toLowerCase());

                if (playerName.equals(player.getName()) && plotLimit != -1 && plotsOwned >= plotLimit) {
                    player.sendMessage(C("MsgAlreadyReachedMaxPlots") + " (" + plotsOwned + "/" + getPlotLimit(player)
                            + "). " + C("WordUse") + " /plotme home " + C("MsgToGetToIt"));
                } else {

                    double price = 0.0;

                    InternalPlotCreateEvent event = new InternalPlotCreateEvent(world, id, player);

                    if (manager.isEconomyEnabled(pmi)) {
                        price = pmi.getClaimPrice();
                        double balance = serverBridge.getBalance(player);

                        if (balance >= price) {
                            serverBridge.getEventBus().post(event);
                            if (event.isCancelled()) {
                                return true;
                            }
                            EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                            if (!er.transactionSuccess()) {
                                player.sendMessage(er.errorMessage);
                                serverBridge.getLogger().warning(er.errorMessage);
                                return true;
                            }
                        } else {
                            player.sendMessage(
                                    C("MsgNotEnoughBuy") + " " + C("WordMissing") + " " + (price - balance) + " " + serverBridge.getEconomy()
                                            .currencyNamePlural());
                            return true;
                        }
                    } else {
                        serverBridge.getEventBus().post(event);
                    }

                    if (!event.isCancelled()) {
                        Plot plot = manager.createPlot(world, id, playerName, playerUniqueId, pmi);

                        //plugin.getPlotMeCoreManager().adjustLinkedPlots(id, world);
                        if (playerName.equalsIgnoreCase(player.getName())) {
                            player.sendMessage(
                                    C("MsgThisPlotYours") + " " + C("WordUse") + " /plotme home " + C("MsgToGetToIt") + " " + plugin.moneyFormat
                                            (-price, true));
                        } else {
                            player.sendMessage(C("MsgThisPlotIsNow") + " " + playerName + C("WordPossessive") + ". " + C("WordUse")
                                    + " /plotme home " + C("MsgToGetToIt") + " " + plugin.moneyFormat(-price, true));
                        }

                        if (isAdvancedLogging()) {
                            if (price == 0) {
                                serverBridge.getLogger().info(playerName + " " + C("MsgClaimedPlot") + " " + id);
                            } else {
                                serverBridge.getLogger()
                                        .info(playerName + " " + C("MsgClaimedPlot") + " " + id + (" " + C("WordFor") + " " + price));
                            }
                        }
                    }
                }
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme claim";
    }

}
