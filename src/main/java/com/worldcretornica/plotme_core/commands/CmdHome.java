package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.event.PlotTeleportHomeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CmdHome extends PlotCommand {

    public CmdHome(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "home";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("h");
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.USER_HOME)) {
            boolean plotWorld = manager.isPlotWorld(player);
            if (plotWorld || plugin.getConfig().getBoolean("allowWorldTeleport")) {
                UUID uuid = player.getUniqueId();
                IWorld world;
                if (plotWorld) {
                    world = player.getWorld();
                } else {
                    world = manager.getFirstWorld();
                }
                //noinspection ConstantConditions -- Stops IntelliJ from attempting to optimize this
                if (world == null) {
                    player.sendMessage(C("NotPlotWorld"));
                    return true;
                }
                int nb = 1;
                if (args.length == 2) {
                    try {
                        nb = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(getUsage());
                        return true;
                    }
                }

                if (args.length == 3) {
                    try {
                        nb = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(getUsage());
                    }
                    IOfflinePlayer offlinePlayer = serverBridge.getOfflinePlayer(args[2]);
                    if (offlinePlayer == null) {
                        player.sendMessage("Error in Home Command!");
                        return true;
                    }
                    uuid = offlinePlayer.getUniqueId();
                }
                PlotMapInfo pmi = manager.getMap(world);
                if (manager.isPlotWorld(world)) {
                    int i = nb - 1;

                    for (Plot plot : plugin.getSqlManager().getPlayerPlots(uuid)) {
                        Location location;
                        if (plot.getOwnerId().equals(uuid)) {
                            if (i == 0) {

                                double price = 0.0;

                                location = manager.getPlotHome(plot.getId(), plot.getWorld());
                                PlotTeleportHomeEvent event = new PlotTeleportHomeEvent(plot, player, location);

                                if (manager.isEconomyEnabled(pmi)) {
                                    price = pmi.getPlotHomePrice();

                                    if (serverBridge.has(player, price)) {
                                        plugin.getEventBus().post(event);

                                        if (!event.isCancelled()) {
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage(
                                                C("MsgNotEnoughTp") + " " + C("WordMissing") + " " + serverBridge.getEconomy().get().format(price));
                                        return true;
                                    }
                                } else {
                                    plugin.getEventBus().post(event);
                                }

                                if (!event.isCancelled()) {
                                    player.teleport(event.getHomeLocation());

                                    if (price != 0) {
                                        player.sendMessage(serverBridge.getEconomy().get().format(price));
                                    }
                                }
                                return true;
                            }
                            i--;
                        }
                    }

                } else {
                    player.sendMessage(C("MsgWorldNotPlot", world.getName()));
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
    public String getUsage() {
        return C("CmdHomeUsage");
    }
}
