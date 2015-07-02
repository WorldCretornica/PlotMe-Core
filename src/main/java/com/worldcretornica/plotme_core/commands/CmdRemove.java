package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotRemoveAllowedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CmdRemove extends PlotCommand {

    public CmdRemove(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "remove";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(getUsage());
            return true;
        }
        if ("*".equals(args[1]) && plugin.getConfig().getBoolean("disableWildCard")) {
            sender.sendMessage("Wildcards are disabled.");
            return true;
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_ADD) || player.hasPermission(PermissionNames.USER_ADD) || player.hasPermission
                (PermissionNames.ADMIN_TRUST) || player.hasPermission(PermissionNames.USER_TRUST)) {
            IWorld world = player.getWorld();
            if (manager.isPlotWorld(world)) {
                PlotMapInfo pmi = manager.getMap(world);
                Plot plot = manager.getPlot(player);
                if (plot == null) {
                    player.sendMessage(C("NoPlotFound"));
                    return true;
                } else {
                    UUID playerUniqueId = player.getUniqueId();
                    String allowed;

                    if (plot.getOwnerId().equals(playerUniqueId) || player.hasPermission(PermissionNames.ADMIN_TRUST) || player.hasPermission
                            (PermissionNames.ADMIN_ADD)) {
                        if (args[1].equals("*")) {
                            allowed = "*";
                        } else {
                            IOfflinePlayer offlinePlayer = serverBridge.getOfflinePlayer(args[1]);
                            if (offlinePlayer == null) {
                                player.sendMessage("An error occured while trying to remove " + args[1]);
                                return true;
                            } else {
                                allowed = offlinePlayer.getUniqueId().toString();
                            }
                        }
                        if (plot.isMember(allowed).isPresent()) {
                            double price = 0.0;

                            PlotRemoveAllowedEvent event = new PlotRemoveAllowedEvent(plot, player, allowed);
                            plugin.getEventBus().post(event);

                            if (manager.isEconomyEnabled(pmi) && !event.isCancelled()) {
                                price = pmi.getRemovePlayerPrice();

                                if (serverBridge.has(player, price)) {
                                    EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                    if (!er.transactionSuccess()) {
                                        player.sendMessage(er.errorMessage);
                                        serverBridge.getLogger().warning(er.errorMessage);
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(C("MsgNotEnoughRemove") + " " + C("WordMissing") + " " + serverBridge.getEconomy().get()
                                            .format(
                                                    price));
                                    return true;
                                }
                            }

                            if (!event.isCancelled()) {
                                if ("*".equals(allowed)) {
                                    plot.removeAllMembers();
                                } else {
                                    plot.removeMembers(allowed);
                                }
                                player.sendMessage(
                                        C("WordPlayer") + " " + args[1] + " " + C("WordRemoved") + ". " + serverBridge.getEconomy().get().format
                                                (price));
                                plugin.getSqlManager().savePlot(plot);

                                if (isAdvancedLogging()) {
                                    if (price == 0) {
                                        serverBridge.getLogger()
                                                .info(args[1] + " " + C("MsgRemovedPlayer") + " " + args[1] + " " + C("MsgFromPlot") + " " + plot
                                                        .getId());
                                    } else {
                                        serverBridge.getLogger()
                                                .info(args[1] + " " + C("MsgRemovedPlayer") + " " + args[1] + " " + C("MsgFromPlot") + " " + plot
                                                        .getId()
                                                        + (" " + C("WordFor") + " " + price));
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(C("WordPlayer") + " " + args[1] + " " + C("MsgWasNotAllowed"));
                        }
                    } else {
                        player.sendMessage(C("MsgThisPlot") + "(" + plot.getId() + ") " + C("MsgNotYoursNotAllowedRemove"));
                    }
                }
            } else {
                player.sendMessage(C("NotPlotWorld"));
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("-");
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme remove <" + C("WordPlayer") + ">";
    }
}
