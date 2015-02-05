package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.ClearReason;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotResetEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdReset extends PlotCommand {

    public CmdReset(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission(PermissionNames.ADMIN_RESET) || player.hasPermission("PlotMe.use.reset")) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                Plot plot = manager.getPlotById(player, pmi);

                if (plot == null) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (plot.isProtect()) {
                    player.sendMessage("§c" + C("MsgPlotProtectedCannotReset"));
                } else {
                    String playerName = player.getName();
                    String id = plot.getId();

                    if (plot.getOwnerId() == player.getUniqueId() || player.hasPermission(PermissionNames.ADMIN_RESET)) {

                        InternalPlotResetEvent event = serverBridge.getEventFactory().callPlotResetEvent(plugin, world, plot, player);

                        if (!event.isCancelled()) {
                            manager.setBiome(world, id, serverBridge.getBiome("PLAINS"));
                            manager.clear(world, plot, player, ClearReason.Reset);

                            if (manager.isEconomyEnabled(pmi)) {
                                if (plot.isAuctioned()) {
                                    if (plot.getCurrentBidderId() != null) {
                                        IOfflinePlayer offlinePlayer = serverBridge.getOfflinePlayer(plot.getCurrentBidderId());
                                        EconomyResponse economyResponse = serverBridge.depositPlayer(offlinePlayer, plot.getCurrentBid());

                                        if (economyResponse.transactionSuccess()) {
                                            player.sendMessage(plot.getCurrentBidder() + " was refunded their money for their plot bid.");
                                        } else {
                                            player.sendMessage(economyResponse.errorMessage);
                                            serverBridge.getLogger().warning(economyResponse.errorMessage);
                                        }
                                    }
                                }

                                if (pmi.isRefundClaimPriceOnReset() && plot.getOwnerId() != null) {
                                    IOfflinePlayer playerowner = serverBridge.getOfflinePlayer(plot.getOwnerId());

                                    EconomyResponse er = serverBridge.depositPlayer(playerowner, pmi.getClaimPrice());

                                    if (er.transactionSuccess()) {
                                        IPlayer playerOwner = serverBridge.getPlayer(playerowner.getUniqueId());
                                        if (playerOwner.getName().equalsIgnoreCase(plot.getOwner())) {
                                            playerOwner.sendMessage(
                                                    C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C("MsgWasReset")
                                                    + " " + Util().moneyFormat(pmi.getClaimPrice(), true));
                                        }
                                    } else {
                                        player.sendMessage("§c" + er.errorMessage);
                                        serverBridge.getLogger().warning(er.errorMessage);
                                        return true;
                                    }
                                }
                            }

                            if (!manager.isPlotAvailable(id, pmi)) {
                                manager.removePlot(pmi, id);
                            }

                            manager.removeOwnerSign(world, id);
                            manager.removeSellSign(world, id);
                            plugin.getSqlManager().deletePlot(manager.getIdX(id), manager.getIdZ(id), world.getName());

                            if (isAdvancedLogging()) {
                                serverBridge.getLogger().info(player.getName() + " " + C("MsgResetPlot") + " " + id);
                            }
                        }
                    } else {
                        player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedReset"));
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
