package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.*;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.api.event.InternalPlotResetEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdReset extends PlotCommand {

    public CmdReset(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player) {
        if (player.hasPermission(PermissionNames.ADMIN_RESET) || player.hasPermission("PlotMe.use.reset")) {
            World world = player.getWorld();
            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);
            if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                Plot plot = PlotMeCoreManager.getPlotById(player, pmi);

                if (plot == null) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (plot.isProtect()) {
                    player.sendMessage("§c" + C("MsgPlotProtectedCannotReset"));
                } else {
                    String playername = player.getName();
                    String id = plot.getId();

                    if (plot.getOwner().equalsIgnoreCase(playername) || player.hasPermission(PermissionNames.ADMIN_RESET)) {

                        InternalPlotResetEvent event = serverBridge.getEventFactory().callPlotResetEvent(plugin, world, plot, player);

                        if (!event.isCancelled()) {
                            plugin.getPlotMeCoreManager().setBiome(world, id, serverBridge.getBiome("PLAINS"));
                            plugin.getPlotMeCoreManager().clear(world, plot, player, ClearReason.Reset);

                            if (plugin.getPlotMeCoreManager().isEconomyEnabled(pmi)) {
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
                                            playerOwner.sendMessage(C("WordPlot") + " " + id + " " + C("MsgOwnedBy") + " " + plot.getOwner() + " " + C("MsgWasReset") + " " + Util().moneyFormat(pmi.getClaimPrice(), true));
                                        }
                                    } else {
                                        player.sendMessage("§c" + er.errorMessage);
                                        serverBridge.getLogger().warning(er.errorMessage);
                                        return true;
                                    }
                                }
                            }

                            if (!PlotMeCoreManager.isPlotAvailable(id, pmi)) {
                                PlotMeCoreManager.removePlot(pmi, id);
                            }

                            PlotMeCoreManager.removeOwnerSign(world, id);
                            PlotMeCoreManager.removeSellSign(world, id);
                            plugin.getSqlManager().deletePlot(PlotMeCoreManager.getIdX(id), PlotMeCoreManager.getIdZ(id), world.getName());

                            pmi.addFreed(id);

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
