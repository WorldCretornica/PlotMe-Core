package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.CommandExBase;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.util.ArrayList;
import java.util.List;

public class CmdShowHelp extends PlotCommand {

    public CmdShowHelp(PlotMe_Core instance, CommandExBase commandExBase) {
        super(instance);
    }

    public String getName() {
        return "help";
    }

    public boolean execute(ICommandSender player, String[] args) {
        int page = 1;
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        List<String> allowed_commands = new ArrayList<>();

        allowed_commands.add("limit");
        if (player.hasPermission(PermissionNames.USER_CLAIM)) {
            allowed_commands.add("claim");
        }
        if (player.hasPermission("PlotMe.use.claim.other")) {
            allowed_commands.add("claim.other");
        }
        if (player.hasPermission(PermissionNames.USER_AUTO)) {
            allowed_commands.add("auto");
        }
        if (player.hasPermission(PermissionNames.USER_HOME)) {
            allowed_commands.add("home");
        }
        if (player.hasPermission(PermissionNames.USER_INFO)) {
            allowed_commands.add("info");
        }
        if (player.hasPermission(PermissionNames.USER_LIST)) {
            allowed_commands.add("list");
        }
        if (player.hasPermission(PermissionNames.USER_BIOME)) {
            allowed_commands.add("biome");
            allowed_commands.add("biomes");
        }
        if (player.hasPermission(PermissionNames.USER_DONE) || player.hasPermission(PermissionNames.ADMIN_DONE)) {
            allowed_commands.add("done");
        }
        if (player.hasPermission(PermissionNames.ADMIN_TP)) {
            allowed_commands.add("tp");
        }
        if (player.hasPermission(PermissionNames.USER_CLEAR) || player.hasPermission(PermissionNames.ADMIN_CLEAR)) {
            allowed_commands.add("clear");
        }
        if (player.hasPermission(PermissionNames.ADMIN_DISPOSE) || player.hasPermission(PermissionNames.USER_DISPOSE)) {
            allowed_commands.add("dispose");
        }
        if (player.hasPermission(PermissionNames.ADMIN_RESET)) {
            allowed_commands.add("reset");
        }
        if (player.hasPermission(PermissionNames.USER_ADD) || player.hasPermission(PermissionNames.ADMIN_ADD)) {
            allowed_commands.add("add");
            allowed_commands.add("remove");
        }
        if (player.hasPermission(PermissionNames.USER_DENY) || player.hasPermission(PermissionNames.ADMIN_DENY)) {
            allowed_commands.add("deny");
        }
        if (player.hasPermission(PermissionNames.ADMIN_SETOWNER)) {
            allowed_commands.add("setowner");
        }
        if (player.hasPermission(PermissionNames.ADMIN_MOVE)) {
            allowed_commands.add("move");
        }
        if (player.hasPermission(PermissionNames.ADMIN_WEANYWHERE)) {
            allowed_commands.add("weanywhere");
        }
        if (player.hasPermission(PermissionNames.ADMIN_EXPIRED)) {
            allowed_commands.add("expired");
        }
        if (player.hasPermission(PermissionNames.ADMIN_ADDTIME)) {
            allowed_commands.add("addtime");
        }

        PlotMapInfo pmi = manager.getMap((IPlayer) player);

        boolean economyEnabled = manager.isEconomyEnabled(pmi);

        if (manager.isPlotWorld((IPlayer) player) && economyEnabled) {
            if (player.hasPermission(PermissionNames.USER_BUY)) {
                allowed_commands.add("buy");
            }
            if (player.hasPermission(PermissionNames.USER_SELL)) {
                allowed_commands.add("sell");
            }
        }

        int maxPage = (int) Math.ceil(allowed_commands.size() / 4);

        page = Math.min(maxPage, Math.max(1, page));

        player.sendMessage(C("HelpTitle", page, maxPage));

        for (int ctr = (page - 1) * 4; ctr < (page * 4) && ctr < allowed_commands.size(); ctr++) {
            String allowedCommand = allowed_commands.get(ctr);

            if ("limit".equalsIgnoreCase(allowedCommand)) {
                int plotLimit = getPlotLimit((IPlayer) player);
                if (manager.isPlotWorld((IPlayer) player)) {

                    IWorld world = ((IPlayer) player).getWorld();

                    int ownedPlots = manager.getOwnedPlotCount(((IPlayer) player).getUniqueId(), world);

                    if (plotLimit == -1) {
                        player.sendMessage(C("HelpInfinitePlotLimit"));
                    } else {
                        player.sendMessage(C("HelpPlayerPlotLimit", ownedPlots, plotLimit));
                    }
                } else if (plugin.getConfig().getBoolean("allowWorldTeleport")) {

                    IWorld world = manager.getFirstWorld();

                    int ownedPlots = manager.getOwnedPlotCount(((IPlayer) player).getUniqueId(), world);

                    if (plotLimit == -1) {
                        player.sendMessage(C("HelpInfinitePlotLimit"));
                    } else {
                        player.sendMessage(C("HelpPlayerPlotLimit", ownedPlots, plotLimit));
                    }
                } else {
                    player.sendMessage(C("NotPlotWorld"));
                }
            } else if ("claim".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdClaimUsage"));
                if (economyEnabled && pmi.getClaimPrice() != 0) {
                    player.sendMessage(C("HelpClaim") + " " + C("WordPrice",pmi.getClaimPrice()));
                } else {
                    player.sendMessage(C("HelpClaim"));
                }
            } else if ("claim.other".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdClaimOtherUsage"));
                if (economyEnabled && pmi.getClaimPrice() != 0) {
                    player.sendMessage(C("HelpClaimOther") + " " + C("WordPrice", pmi.getClaimPrice()));
                } else {
                    player.sendMessage(C("HelpClaimOther"));
                }
            } else if ("auto".equalsIgnoreCase(allowedCommand)) {
                if (plugin.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage(C("CmdAutoUsageWTP"));
                } else {
                    player.sendMessage(C("CmdAutoUsage"));
                }
                if (economyEnabled && pmi.getClaimPrice() != 0) {
                    player.sendMessage(C("HelpAuto") + " " + C("WordPrice",pmi.getClaimPrice()));
                } else {
                    player.sendMessage(C("HelpAuto"));
                }
            } else if ("home".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdHomeUsage"));
                if (economyEnabled && pmi.getPlotHomePrice() != 0) {
                    player.sendMessage(C("HelpHome") + " " + C("WordPrice",pmi.getPlotHomePrice()));
                } else {
                    player.sendMessage(C("HelpHome"));
                }
            } else if ("info".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdInfoUsage"));
                player.sendMessage(C("HelpInfo"));
            } else if ("list".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdListUsage"));
                player.sendMessage(C("HelpList"));
            } else if ("biome".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdBiomeUsage"));
                if (economyEnabled && pmi.getBiomeChangePrice() != 0) {
                    player.sendMessage(C("HelpBiome") + " " + C("WordPrice",pmi.getBiomeChangePrice()));
                } else {
                    player.sendMessage(C("HelpBiome"));
                }
                player.sendMessage(C("CmdBiomesUsage"));
                player.sendMessage(C("HelpBiomeList"));
            } else if ("done".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdDoneUsage"));
                player.sendMessage(C("HelpDone"));
                player.sendMessage(C("CmdDoneListUsage"));
                player.sendMessage(C("HelpDoneList"));
            } else if ("tp".equalsIgnoreCase(allowedCommand)) {
                if (plugin.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage(C("CmdTeleportUsageWTP"));
                } else {
                    player.sendMessage(C("CmdTeleportUsage"));
                }
                player.sendMessage(C("HelpTp"));
            } else if ("clear".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdClearUsage"));
                if (economyEnabled && pmi.getClearPrice() != 0) {
                    player.sendMessage(C("HelpClear") + " " + C("WordPrice",pmi.getClearPrice()));
                } else {
                    player.sendMessage(C("HelpClear"));
                }
            } else if ("reset".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdResetUsage"));
                player.sendMessage(C("HelpReset"));
            } else if ("add".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdAddUsage"));
                if (economyEnabled && pmi.getAddPlayerPrice() != 0) {
                    player.sendMessage(C("HelpAdd") + " " + C("WordPrice",pmi.getAddPlayerPrice()));
                } else {
                    player.sendMessage(C("HelpAdd"));
                }
                player.sendMessage(C("CmdRemoveUsage"));
                if (economyEnabled && pmi.getRemovePlayerPrice() != 0) {
                    player.sendMessage(C("HelpRemove") + " " + C("WordPrice",pmi.getRemovePlayerPrice()));
                } else {
                    player.sendMessage(C("HelpRemove"));
                }
            } else if ("deny".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdDenyUsage"));
                if (economyEnabled && pmi.getDenyPlayerPrice() != 0) {
                    player.sendMessage(C("HelpDeny") + " " + C("WordPrice",pmi.getDenyPlayerPrice()));
                } else {
                    player.sendMessage(C("HelpDeny"));
                }
                player.sendMessage(C("CmdUndenyUsage"));
                if (economyEnabled && pmi.getUndenyPlayerPrice() != 0) {
                    player.sendMessage(C("HelpUndeny") + " " + C("WordPrice",pmi.getUndenyPlayerPrice()));
                } else {
                    player.sendMessage(C("HelpUndeny"));
                }
            } else if ("setowner".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdSetOwnerUsage"));
                player.sendMessage(C("HelpSetowner"));
            } else if ("move".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdMoveUsage"));
                player.sendMessage(C("HelpMove"));
            } else if ("weanywhere".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdWEAnywhereUsage"));
                player.sendMessage(C("HelpWEAnywhere"));
            } else if ("expired".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdExpiredUsage"));
                player.sendMessage(C("HelpExpired"));
            } else if ("addtime".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdAddTimeUsage"));
                int days = pmi.getDaysToExpiration();
                if (days != 0) {
                    player.sendMessage(C("HelpAddTime"));
                }
            } else if ("dispose".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdDisposeUsage"));
                if (economyEnabled && pmi.getDisposePrice() != 0) {
                    player.sendMessage(C("HelpDispose") + " " + C("WordPrice",pmi.getDisposePrice()));
                } else {
                    player.sendMessage(C("HelpDispose"));
                }
            } else if ("buy".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdBuyUsage"));
                player.sendMessage(C("HelpBuy"));
            } else if ("sell".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(C("CmdSellUsage"));
                player.sendMessage(C("HelpSell"));
            }
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme help <" + C("WordPage") + ">";
    }
}