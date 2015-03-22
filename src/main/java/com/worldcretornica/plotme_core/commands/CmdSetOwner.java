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
import com.worldcretornica.plotme_core.api.event.InternalPlotOwnerChangeEvent;
import com.worldcretornica.plotme_core.utils.UUIDFetcher;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.UUID;

public class CmdSetOwner extends PlotCommand implements CommandBase {

    public CmdSetOwner(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "setowner";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        IWorld world = player.getWorld();
        PlotMapInfo pmi = manager.getMap(world);
        if (player.hasPermission(PermissionNames.ADMIN_SETOWNER)) {
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);
                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                }
                String newOwner = args[1];
                UUID newOwnerId = UUIDFetcher.getUUIDOf(newOwner);
                String oldowner = "<" + C("WordNotApplicable") + ">";

                if (!manager.isPlotAvailable(id, pmi)) {
                    Plot plot = manager.getPlotById(id, pmi);

                    oldowner = plot.getOwner();

                    InternalPlotOwnerChangeEvent event;

                    if (manager.isEconomyEnabled(world)) {
                        if (pmi.isRefundClaimPriceOnSetOwner() && !newOwner.equals(oldowner)) {
                            event = serverBridge.getEventFactory().callPlotOwnerChangeEvent(world, plot, player, newOwner);

                            if (event.isCancelled()) {
                                return true;
                            }
                            IOfflinePlayer playeroldowner = serverBridge.getOfflinePlayer(plot.getOwnerId());
                            EconomyResponse er = serverBridge.depositPlayer(playeroldowner, pmi.getClaimPrice());

                            if (er.transactionSuccess()) {
                                IPlayer oldOwner = serverBridge.getPlayer(plot.getOwnerId());
                                if (oldOwner != null) {
                                    oldOwner.sendMessage(
                                            C("MsgYourPlot") + " " + id + " " + C("MsgNowOwnedBy") + " " + newOwner + ". " + plugin.moneyFormat(pmi
                                                    .getClaimPrice(), true));
                                }
                            } else {
                                player.sendMessage(er.errorMessage);
                                serverBridge.getLogger().warning(er.errorMessage);
                                return true;
                            }
                        } else {
                            event = serverBridge.getEventFactory().callPlotOwnerChangeEvent(world, plot, player, newOwner);
                        }

                    } else {
                        event = serverBridge.getEventFactory().callPlotOwnerChangeEvent(world, plot, player, newOwner);
                    }

                    if (!event.isCancelled()) {
                        plot.setForSale(false);

                        manager.removeSellSign(world, id);

                        plot.updateField("forsale", false);

                        plot.setOwner(newOwner);
                        plot.setOwnerId(newOwnerId);
                        manager.setOwnerSign(world, plot);

                        plot.updateField("owner", newOwner);
                    }
                } else {
                    manager.createPlot(world, id, newOwner, newOwnerId, pmi);
                }

                player.sendMessage(C("MsgOwnerChangedTo") + " " + newOwner);

                if (isAdvancedLogging()) {
                    serverBridge.getLogger()
                            .info(player.getName() + " " + C("MsgChangedOwnerOf") + " " + id + " " + C("WordFrom") + " " + oldowner + " " + C(
                                    "WordTo")
                                    + " " + newOwner);
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
        return null;
    }
}
