package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotBuyEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdBuy extends PlotCommand {

    public CmdBuy(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "buy";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        if (args.length > 1) {
            throw new BadUsageException(getUsage());
        }
        IPlayer player = (IPlayer) sender;
        IWorld world = player.getWorld();
        if (manager.isPlotWorld(world)) {
            if (manager.isEconomyEnabled(world)) {
                if (player.hasPermission(PermissionNames.USER_BUY)) {
                    Plot plot = manager.getPlot(player.getLocation());

                    if (plot != null) {
                        if (plot.isForSale()) {
                            String buyer = player.getName();

                            if (player.getUniqueId().equals(plot.getOwnerId())) {
                                player.sendMessage(C("MsgCannotBuyOwnPlot"));
                            } else {
                                int plotLimit = getPlotLimit(player);

                                int plotsOwned = manager.getOwnedPlotCount(player.getUniqueId(), world.getName());

                                if (plotLimit != -1 && plotsOwned >= plotLimit) {
                                    player.sendMessage(C("MsgAlreadyReachedMaxPlots") + " ("
                                            + plotsOwned + "/" + getPlotLimit(player) + "). "
                                            + C("WordUse") + " /plotme home " + C("MsgToGetToIt"));
                                } else {
                                    double cost = plot.getPrice();

                                    if (serverBridge.has(player, cost)) {
                                        player.sendMessage(C("MsgNotEnoughBuy"));
                                    } else {
                                        PlotBuyEvent event = new PlotBuyEvent(world, plot, player, cost);
                                        serverBridge.getEventBus().post(event);

                                        if (!event.isCancelled()) {
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, cost);

                                            if (er.transactionSuccess()) {
                                                String oldOwner = plot.getOwner();

                                                IOfflinePlayer currBuyer = serverBridge.getOfflinePlayer(plot.getOwnerId());

                                                if (currBuyer != null) {
                                                    EconomyResponse er2 = serverBridge.depositPlayer(currBuyer, cost);

                                                    if (er2.transactionSuccess()) {
                                                        for (IPlayer onlinePlayers : serverBridge.getOnlinePlayers()) {
                                                            if (onlinePlayers.getName().equals(oldOwner)) {
                                                                onlinePlayers.sendMessage(C("WordPlot") + " " + plot.getId() + " "
                                                                        + C("MsgSoldTo") + " " + buyer + ". " + serverBridge.getEconomy().get().format
                                                                        (cost));
                                                                break;
                                                            }
                                                        }
                                                    } else {
                                                        player.sendMessage(er2.errorMessage);
                                                        serverBridge.getLogger().warning(er2.errorMessage);
                                                    }
                                                }

                                                plot.setOwner(buyer);
                                                plot.setOwnerId(player.getUniqueId());
                                                plot.setPrice(0.0);
                                                plot.setForSale(false);

                                                plot.updateField("owner", buyer);
                                                plot.updateField("ownerid", player.getUniqueId());
                                                plot.updateField("customprice", 0);
                                                plot.updateField("forsale", false);

                                                manager.adjustWall(plot, world, true);
                                                manager.removeSellSign(plot, world);
                                                manager.setOwnerSign(world, plot);

                                                player.sendMessage(C("MsgPlotBought") + " " + serverBridge.getEconomy().get().format(cost));

                                                if (isAdvancedLogging()) {
                                                    plugin.getLogger()
                                                            .info(buyer + " " + C("MsgBoughtPlot") + " " + plot.getId().toString() + " " + C(
                                                                    "WordFor") + " "
                                                                    + cost);
                                                }
                                            } else {
                                                player.sendMessage(er.errorMessage);
                                                serverBridge.getLogger().warning(er.errorMessage);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(C("MsgPlotNotForSale"));
                        }
                    } else {
                        player.sendMessage("No plot found or on road."); //todo caption this
                    }
                } else {
                    return false;
                }
            } else {
                player.sendMessage(C("MsgEconomyDisabledWorld"));
            }
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme buy";
    }

}
