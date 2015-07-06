package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotAddTrustedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.Collections;
import java.util.List;

public class CmdAdd extends PlotCommand {

    public CmdAdd(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "add";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(getUsage());
            return true;
        }
        if ("*".equals(args[1]) && plugin.getConfig().getBoolean("disableWildCard")) {
            sender.sendMessage(C("WildcardsDisabled"));
            return true;
        }

        //Start of the actual command
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_ADD) || player.hasPermission(PermissionNames.USER_ADD)) {
            IWorld world = player.getWorld();
            if (manager.isPlotWorld(player)) {
                PlotMapInfo pmi = manager.getMap(world);
                Plot plot = manager.getPlot(player);
                if (plot != null) {
                    String allowed;
                    if ("*".equals(args[1])) {
                        allowed = "*";
                    } else {
                        if (serverBridge.getPlayer(args[1]) != null) {
                            allowed = serverBridge.getPlayer(args[1]).getUniqueId().toString();
                        } else {
                            player.sendMessage(String.format("%s was not found. Are they online?", args[1]));
                            return true;
                        }
                    }
                    if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_ADD)) {
                        if (plot.isMember(allowed).isPresent()) {
                            player.sendMessage(C("MsgAlreadyAllowed", args[1]));
                        } else {
                            PlotAddTrustedEvent event = new PlotAddTrustedEvent(plot, player, allowed);
                            plugin.getEventBus().post(event);
                            double price = 0.0;
                            if (manager.isEconomyEnabled(pmi)) {
                                price = pmi.getAddPlayerPrice();

                                if (serverBridge.has(player, pmi.getAddPlayerPrice())) {
                                    player.sendMessage("It costs " + serverBridge.getEconomy().get().format(price) + " to add a player to "
                                            + "the plot.");
                                    return true;
                                } else if (!event.isCancelled()) {
                                    EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                    if (!er.transactionSuccess()) {
                                        player.sendMessage(er.errorMessage);
                                        serverBridge.getLogger().warning(er.errorMessage);
                                        return true;
                                    }
                                }
                            }

                            if (!event.isCancelled()) {
                                plot.addMember(allowed, Plot.AccessLevel.ALLOWED);
                                plot.removeDenied(allowed);
                                plugin.getSqlManager().savePlot(plot);
                                player.sendMessage(C("MsgNowAllowed", args[1]));

                                if (isAdvancedLogging()) {
                                    if (price == 0) {
                                        serverBridge.getLogger()
                                                .info(player.getName() + " " + C("MsgAddedPlayer") + " " + args[1] + " " + C("MsgToPlot")
                                                        + " "
                                                        + plot.getId());
                                    } else {
                                        serverBridge.getLogger()
                                                .info(player.getName() + " " + C("MsgAddedPlayer") + " " + args[1] + " " + C("MsgToPlot")
                                                        + " "
                                                        + plot.getId() + (" " + C("WordFor") + " " + price));
                                    }
                                }
                            }
                        }
                    } else {
                        player.sendMessage(C("MsgThisPlot") + "(" + plot.getId() + ") " + C("MsgNotYoursNotAllowedAdd"));
                    }
                } else {
                    player.sendMessage(C("NoPlotFound"));
                }
            } else {
                player.sendMessage(C("NotPlotWorld"));
                return true;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("+");
    }

    @Override
    public String getUsage() {
        return C("CmdAddUsage");
    }

}
