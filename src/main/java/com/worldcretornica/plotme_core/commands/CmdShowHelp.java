package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.util.ArrayList;
import java.util.List;

public class CmdShowHelp extends PlotCommand {

    public CmdShowHelp(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "help";
    }

    public boolean execute(ICommandSender player, String[] args) {
        int page = 0;
        if (args.length >= 2) {
            page = Integer.parseInt(args[1]);
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
        }
        if (player.hasPermission(PermissionNames.USER_REMOVE) || player.hasPermission(PermissionNames.ADMIN_REMOVE)) {
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

        if (page > maxPage) {
            page = 1;
        }

        player.sendMessage("-----" + C("HelpTitle") + " " + page + "/" + maxPage + "-----");

        for (int ctr = (page - 1) * 4; ctr < (page * 4) && ctr < allowed_commands.size(); ctr++) {
            String allowedCommand = allowed_commands.get(ctr);

            if ("limit".equalsIgnoreCase(allowedCommand)) {
                int plotLimit = getPlotLimit((IPlayer) player);
                if (manager.isPlotWorld((IPlayer) player)) {

                    IWorld world = ((IPlayer) player).getWorld();

                    int ownedPlots = manager.getOwnedPlotCount(((IPlayer) player).getUniqueId(), world);

                    if (plotLimit == -1) {
                        player.sendMessage(
                                C("HelpYourPlotLimitWorld") + " : " + ownedPlots + " " + C("HelpUsedOf") + " " + C("WordInfinite"));
                    } else {
                        player.sendMessage(C("HelpYourPlotLimitWorld") + " : " + ownedPlots + " " + C("HelpUsedOf") + " " + plotLimit);
                    }
                } else if (plugin.getConfig().getBoolean("allowWorldTeleport")) {

                    IWorld world = manager.getFirstWorld();

                    int ownedPlots = manager.getOwnedPlotCount(((IPlayer) player).getUniqueId(), world);

                    if (plotLimit == -1) {
                        player.sendMessage(
                                C("HelpYourPlotLimitWorld") + " : " + ownedPlots + " " + C("HelpUsedOf") + " " + C("WordInfinite"));
                    } else {
                        player.sendMessage(
                                C("HelpYourPlotLimitWorld") + " : " + ownedPlots + " " + C("HelpUsedOf") + " " + plotLimit);
                    }
                } else {
                    player.sendMessage(C("HelpYourPlotLimitWorld") + " : " + C("MsgNotPlotWorld"));
                }
            } else if ("claim".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(" /plotme claim");
                if (economyEnabled && pmi.getClaimPrice() != 0) {
                    player.sendMessage(C("HelpClaim") + " " + C("WordPrice") + " : " + Math.round(pmi.getClaimPrice()));
                } else {
                    player.sendMessage(C("HelpClaim"));
                }
            } else if ("claim.other".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage(" /plotme claim <" + C("WordPlayer") + ">");
                if (economyEnabled && pmi.getClaimPrice() != 0) {
                    player.sendMessage(" " + C("HelpClaimOther") + " " + C("WordPrice") + " : " + Math.round(pmi.getClaimPrice()));
                } else {
                    player.sendMessage(" " + C("HelpClaimOther"));
                }
            } else if ("auto".equalsIgnoreCase(allowedCommand)) {
                if (plugin.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage("/plotme auto [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage("/plotme auto");
                }

                if (economyEnabled && pmi.getClaimPrice() != 0) {
                    player.sendMessage(C("HelpAuto") + " " + C("WordPrice") + " : " + Math.round(pmi.getClaimPrice()));
                } else {
                    player.sendMessage(C("HelpAuto"));
                }
            } else if ("home".equalsIgnoreCase(allowedCommand)) {
                if (plugin.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage(" /plotme home <number/name> <" + C("WordPlayer") + "> [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage(" /plotme home[:#] <" + C("WordPlayer") + ">");
                }

                if (economyEnabled && pmi.getPlotHomePrice() != 0) {
                    player.sendMessage(" " + C("HelpHomeOther") + " " + C("WordPrice") + " : " + Math.round(pmi.getPlotHomePrice()));
                } else {
                    player.sendMessage(" " + C("HelpHomeOther"));
                }
            } else if ("info".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme info");
                player.sendMessage(C("HelpInfo"));
            } else if ("list".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme list <" + C("WordPlayer") + ">");
                player.sendMessage(C("HelpList"));
            } else if ("biome".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme biome <" + C("WordBiome") + ">");
                if (economyEnabled && pmi.getBiomeChangePrice() != 0) {
                    player.sendMessage(C("HelpBiome") + " " + C("WordPrice") + " : " + Math.round(pmi.getBiomeChangePrice()));
                } else {
                    player.sendMessage(C("HelpBiome"));
                }
                player.sendMessage("/plotme biomes");
                player.sendMessage(C("HelpBiomeList"));
            } else if ("done".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme done");
                player.sendMessage(C("HelpDone"));
                player.sendMessage("/plotme donelist [page]");
                player.sendMessage(C("HelpDoneList"));
            } else if ("tp".equalsIgnoreCase(allowedCommand)) {
                if (plugin.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage("/plotme tp <ID> [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage("/plotme tp <ID>");
                }
                player.sendMessage(C("HelpTp"));
            } else if ("clear".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme clear");
                if (economyEnabled && pmi.getClearPrice() != 0) {
                    player.sendMessage(C("HelpId") + " " + C("WordPrice") + " : " + Math.round(pmi.getClearPrice()));
                } else {
                    player.sendMessage(C("HelpClear"));
                }
            } else if ("reset".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme reset");
                player.sendMessage(C("HelpReset"));
            } else if ("add".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme add <" + C("WordPlayer") + ">");
                if (economyEnabled && pmi.getAddPlayerPrice() != 0) {
                    player.sendMessage(C("HelpAdd") + " " + C("WordPrice") + " : " + Math.round(pmi.getAddPlayerPrice()));
                } else {
                    player.sendMessage(C("HelpAdd"));
                }
                player.sendMessage("/plotme remove <" + C("WordPlayer") + ">");
                if (economyEnabled && pmi.getRemovePlayerPrice() != 0) {
                    player.sendMessage(C("HelpRemove") + " " + C("WordPrice") + " : " + Math.round(pmi.getRemovePlayerPrice()));
                } else {
                    player.sendMessage(" " + C("HelpRemove"));
                }
            } else if ("deny".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme deny <" + C("WordPlayer") + ">");
                if (economyEnabled && pmi.getDenyPlayerPrice() != 0) {
                    player.sendMessage(C("HelpDeny") + " " + C("WordPrice") + " : " + Math.round(pmi.getDenyPlayerPrice()));
                } else {
                    player.sendMessage(C("HelpDeny"));
                }
                player.sendMessage("/plotme undeny <" + C("WordPlayer") + ">");
                if (economyEnabled && pmi.getUndenyPlayerPrice() != 0) {
                    player.sendMessage(C("HelpUndeny") + " " + C("WordPrice") + " : " + Math.round(pmi.getUndenyPlayerPrice()));
                } else {
                    player.sendMessage(C("HelpUndeny"));
                }
            } else if ("setowner".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme setowner <" + C("WordPlayer") + ">");
                player.sendMessage(C("HelpSetowner"));
            } else if ("move".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme move <" + C("WordIdFrom") + "> <" + C("WordIdTo") + ">");
                player.sendMessage(C("HelpMove"));
            } else if ("weanywhere".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme weanywhere");
                player.sendMessage(C("HelpWEAnywhere"));
            } else if ("expired".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme expired");
                player.sendMessage(C("HelpExpired"));
            } else if ("addtime".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme addtime");
                int days;
                if (pmi == null) {
                    days = 0;
                } else {
                    days = pmi.getDaysToExpiration();
                }
                if (days != 0) {
                    player.sendMessage(C("HelpAddTime1") + " " + days + " " + C("HelpAddTime2"));
                }
            } else if ("dispose".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme dispose");
                if (economyEnabled && pmi.getDisposePrice() != 0) {
                    player.sendMessage(C("HelpDispose") + " " + C("WordPrice") + " : " + Math.round(pmi.getDisposePrice()));
                } else {
                    player.sendMessage(C("HelpDispose"));
                }
            } else if ("buy".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme buy");
                player.sendMessage(C("HelpBuy"));
            } else if ("sell".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("/plotme sell [" + C("WordAmount") + "]");
                assert pmi != null;
                player.sendMessage(C("HelpSell") + " " + C("WordDefault") + " : " + Math.round(pmi.getSellToPlayerPrice()));
            }
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme help <" + C("WordPage") + ">";
    }
}
