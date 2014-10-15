package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.util.ArrayList;
import java.util.List;

public class CmdShowHelp extends PlotCommand {

    public CmdShowHelp(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p, int page) {
        boolean ecoon = plugin.getPlotMeCoreManager().isEconomyEnabled(p);

        List<String> allowed_commands = new ArrayList<>();

        allowed_commands.add("limit");
        if (plugin.cPerms(p, "PlotMe.use.claim")) {
            allowed_commands.add("claim");
        }
        if (plugin.cPerms(p, "PlotMe.use.claim.other")) {
            allowed_commands.add("claim.other");
        }
        if (plugin.cPerms(p, "PlotMe.use.auto")) {
            allowed_commands.add("auto");
        }
        if (plugin.cPerms(p, "PlotMe.use.home")) {
            allowed_commands.add("home");
        }
        if (plugin.cPerms(p, "PlotMe.use.home.other")) {
            allowed_commands.add("home.other");
        }
        if (plugin.cPerms(p, "PlotMe.use.info")) {
            allowed_commands.add("info");
            allowed_commands.add("biomeinfo");
        }
        if (plugin.cPerms(p, "PlotMe.use.comment")) {
            allowed_commands.add("comment");
        }
        if (plugin.cPerms(p, "PlotMe.use.comments")) {
            allowed_commands.add("comments");
        }
        if (plugin.cPerms(p, "PlotMe.use.list")) {
            allowed_commands.add("list");
        }
        if (plugin.cPerms(p, "PlotMe.use.biome")) {
            allowed_commands.add("biome");
            allowed_commands.add("biomelist");
        }
        if (plugin.cPerms(p, "PlotMe.use.done") || plugin.cPerms(p, "PlotMe.admin.done")) {
            allowed_commands.add("done");
        }
        if (plugin.cPerms(p, "PlotMe.admin.done")) {
            allowed_commands.add("donelist");
        }
        if (plugin.cPerms(p, "PlotMe.admin.tp")) {
            allowed_commands.add("tp");
        }
        if (plugin.cPerms(p, "PlotMe.admin.id")) {
            allowed_commands.add("id");
        }
        if (plugin.cPerms(p, "PlotMe.use.clear") || plugin.cPerms(p, "PlotMe.admin.clear")) {
            allowed_commands.add("clear");
        }
        if (plugin.cPerms(p, "PlotMe.admin.dispose") || plugin.cPerms(p, "PlotMe.use.dispose")) {
            allowed_commands.add("dispose");
        }
        if (plugin.cPerms(p, "PlotMe.admin.reset")) {
            allowed_commands.add("reset");
        }
        if (plugin.cPerms(p, "PlotMe.use.add") || plugin.cPerms(p, "PlotMe.admin.add")) {
            allowed_commands.add("add");
        }
        if (plugin.cPerms(p, "PlotMe.use.remove") || plugin.cPerms(p, "PlotMe.admin.remove")) {
            allowed_commands.add("remove");
        }
        if (sob.getConfig().getBoolean("allowToDeny")) {
            if (plugin.cPerms(p, "PlotMe.use.deny") || plugin.cPerms(p, "PlotMe.admin.deny")) {
                allowed_commands.add("deny");
            }
            if (plugin.cPerms(p, "PlotMe.use.undeny") || plugin.cPerms(p, "PlotMe.admin.undeny")) {
                allowed_commands.add("undeny");
            }
        }
        if (plugin.cPerms(p, "PlotMe.admin.setowner")) {
            allowed_commands.add("setowner");
        }
        if (plugin.cPerms(p, "PlotMe.admin.move")) {
            allowed_commands.add("move");
        }
        if (plugin.cPerms(p, "PlotMe.admin.weanywhere")) {
            allowed_commands.add("weanywhere");
        }
        if (plugin.cPerms(p, "PlotMe.admin.reload")) {
            allowed_commands.add("reload");
        }
        if (plugin.cPerms(p, "PlotMe.admin.list")) {
            allowed_commands.add("listother");
        }
        if (plugin.cPerms(p, "PlotMe.admin.expired")) {
            allowed_commands.add("expired");
        }
        if (plugin.cPerms(p, "PlotMe.admin.addtime")) {
            allowed_commands.add("addtime");
        }

        PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(p);

        if (plugin.getPlotMeCoreManager().isPlotWorld(p) && ecoon) {
            if (plugin.cPerms(p, "PlotMe.use.buy")) {
                allowed_commands.add("buy");
            }
            if (plugin.cPerms(p, "PlotMe.use.sell")) {
                allowed_commands.add("sell");
                if (pmi.isCanSellToBank()) {
                    allowed_commands.add("sellbank");
                }
            }
            if (plugin.cPerms(p, "PlotMe.use.auction")) {
                allowed_commands.add("auction");
            }
            if (plugin.cPerms(p, "PlotMe.use.bid")) {
                allowed_commands.add("bid");
            }
        }

        int maxpage = (int) Math.ceil((double) allowed_commands.size() / 4);

        if (page > maxpage) {
            page = 1;
        }

        p.sendMessage(RED + " ---==" + AQUA + C("HelpTitle") + " " + page + "/" + maxpage + RED + "==--- ");

        for (int ctr = (page - 1) * 4; ctr < (page * 4) && ctr < allowed_commands.size(); ctr++) {
            String allowedcmd = allowed_commands.get(ctr);

            if (allowedcmd.equalsIgnoreCase("limit")) {
                if (plugin.getPlotMeCoreManager().isPlotWorld(p) || sob.getConfig().getBoolean("allowWorldTeleport")) {
                    IWorld w = null;

                    if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
                        w = p.getWorld();
                    } else if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                        w = plugin.getPlotMeCoreManager().getFirstWorld();
                    }

                    int maxplots = plugin.getPlotLimit(p);
                    int ownedplots = plugin.getPlotMeCoreManager().getNbOwnedPlot(p, w);

                    if (maxplots == -1) {
                        p.sendMessage(GREEN + C("HelpYourPlotLimitWorld") + " : " + AQUA + ownedplots + GREEN + " " + C("HelpUsedOf") + " " + AQUA + C("WordInfinite"));
                    } else {
                        p.sendMessage(GREEN + C("HelpYourPlotLimitWorld") + " : " + AQUA + ownedplots + GREEN + " " + C("HelpUsedOf") + " " + AQUA + maxplots);
                    }
                } else {
                    p.sendMessage(GREEN + C("HelpYourPlotLimitWorld") + " : " + AQUA + C("MsgNotPlotWorld"));
                }
            } else if (allowedcmd.equalsIgnoreCase("claim")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandClaim"));
                if (ecoon && pmi != null && pmi.getClaimPrice() != 0) {
                    p.sendMessage(AQUA + " " + C("HelpClaim") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getClaimPrice()));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpClaim"));
                }
            } else if (allowedcmd.equalsIgnoreCase("claim.other")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandClaim") + " <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getClaimPrice() != 0) {
                    p.sendMessage(AQUA + " " + C("HelpClaimOther") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getClaimPrice()));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpClaimOther"));
                }
            } else if (allowedcmd.equalsIgnoreCase("auto")) {
                if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                    p.sendMessage(GREEN + " /plotme " + C("CommandAuto") + " [" + C("WordWorld") + "]");
                } else {
                    p.sendMessage(GREEN + " /plotme " + C("CommandAuto"));
                }

                if (ecoon && pmi != null && pmi.getClaimPrice() != 0) {
                    p.sendMessage(AQUA + " " + C("HelpAuto") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getClaimPrice()));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpAuto"));
                }
            } else if (allowedcmd.equalsIgnoreCase("home")) {
                if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                    p.sendMessage(GREEN + " /plotme " + C("CommandHome") + "[:#] [" + C("WordWorld") + "]");
                } else {
                    p.sendMessage(GREEN + " /plotme " + C("CommandHome") + "[:#]");
                }

                if (ecoon && pmi != null && pmi.getPlotHomePrice() != 0) {
                    p.sendMessage(AQUA + " " + C("HelpHome") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getPlotHomePrice()));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpHome"));
                }
            } else if (allowedcmd.equalsIgnoreCase("home.other")) {
                if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                    p.sendMessage(GREEN + " /plotme " + C("CommandHome") + "[:#] <" + C("WordPlayer") + "> [" + C("WordWorld") + "]");
                } else {
                    p.sendMessage(GREEN + " /plotme " + C("CommandHome") + "[:#] <" + C("WordPlayer") + ">");
                }

                if (ecoon && pmi != null && pmi.getPlotHomePrice() != 0) {
                    p.sendMessage(AQUA + " " + C("HelpHomeOther") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getPlotHomePrice()));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpHomeOther"));
                }
            } else if (allowedcmd.equalsIgnoreCase("info")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandInfo"));
                p.sendMessage(AQUA + " " + C("HelpInfo"));
            } else if (allowedcmd.equalsIgnoreCase("comment")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandComment") + " <" + C("WordComment") + ">");
                if (ecoon && pmi != null && pmi.getAddCommentPrice() != 0) {
                    p.sendMessage(AQUA + " " + C("HelpComment") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getAddCommentPrice()));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpComment"));
                }
            } else if (allowedcmd.equalsIgnoreCase("comments")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandComments"));
                p.sendMessage(AQUA + " " + C("HelpComments"));
            } else if (allowedcmd.equalsIgnoreCase("list")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandList"));
                p.sendMessage(AQUA + " " + C("HelpList"));
            } else if (allowedcmd.equalsIgnoreCase("listother")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandList") + " <" + C("WordPlayer") + ">");
                p.sendMessage(AQUA + " " + C("HelpListOther"));
            } else if (allowedcmd.equalsIgnoreCase("biomeinfo")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandBiome"));
                p.sendMessage(AQUA + " " + C("HelpBiomeInfo"));
            } else if (allowedcmd.equalsIgnoreCase("biome")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandBiome") + " <" + C("WordBiome") + ">");
                if (ecoon && pmi != null && pmi.getBiomeChangePrice() != 0) {
                    p.sendMessage(AQUA + " " + C("HelpBiome") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getBiomeChangePrice()));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpBiome"));
                }
            } else if (allowedcmd.equalsIgnoreCase("biomelist")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandBiomelist"));
                p.sendMessage(AQUA + " " + C("HelpBiomeList"));
            } else if (allowedcmd.equalsIgnoreCase("done")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandDone"));
                p.sendMessage(AQUA + " " + C("HelpDone"));
            } else if (allowedcmd.equalsIgnoreCase("tp")) {
                if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                    p.sendMessage(GREEN + " /plotme " + C("CommandTp") + " <ID> [" + C("WordWorld") + "]");
                } else {
                    p.sendMessage(GREEN + " /plotme " + C("CommandTp") + " <ID>");
                }

                p.sendMessage(AQUA + " " + C("HelpTp"));
            } else if (allowedcmd.equalsIgnoreCase("id")) {
                p.sendMessage(GREEN + " /plotme id");
                p.sendMessage(AQUA + " " + C("HelpId"));
            } else if (allowedcmd.equalsIgnoreCase("clear")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandClear"));
                if (ecoon && pmi != null && pmi.getClearPrice() != 0) {
                    p.sendMessage(AQUA + " " + C("HelpId") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getClearPrice()));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpClear"));
                }
            } else if (allowedcmd.equalsIgnoreCase("reset")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandReset"));
                p.sendMessage(AQUA + " " + C("HelpReset"));
            } else if (allowedcmd.equalsIgnoreCase("add")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandAdd") + " <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getAddPlayerPrice() != 0) {
                    p.sendMessage(AQUA + " " + C("HelpAdd") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getAddPlayerPrice()));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpAdd"));
                }
            } else if (allowedcmd.equalsIgnoreCase("deny")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandDeny") + " <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getDenyPlayerPrice() != 0) {
                    p.sendMessage(AQUA + " " + C("HelpDeny") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getDenyPlayerPrice()));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpDeny"));
                }
            } else if (allowedcmd.equalsIgnoreCase("remove")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandRemove") + " <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getRemovePlayerPrice() != 0) {
                    p.sendMessage(AQUA + " " + C("HelpRemove") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getRemovePlayerPrice()));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpRemove"));
                }
            } else if (allowedcmd.equalsIgnoreCase("undeny")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandUndeny") + " <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getUndenyPlayerPrice() != 0) {
                    p.sendMessage(AQUA + " " + C("HelpUndeny") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getUndenyPlayerPrice()));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpUndeny"));
                }
            } else if (allowedcmd.equalsIgnoreCase("setowner")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandSetowner") + " <" + C("WordPlayer") + ">");
                p.sendMessage(AQUA + " " + C("HelpSetowner"));
            } else if (allowedcmd.equalsIgnoreCase("move")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandMove") + " <" + C("WordIdFrom") + "> <" + C("WordIdTo") + ">");
                p.sendMessage(AQUA + " " + C("HelpMove"));
            } else if (allowedcmd.equalsIgnoreCase("weanywhere")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandWEAnywhere"));
                p.sendMessage(AQUA + " " + C("HelpWEAnywhere"));
            } else if (allowedcmd.equalsIgnoreCase("expired")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandExpired") + " [page]");
                p.sendMessage(AQUA + " " + C("HelpExpired"));
            } else if (allowedcmd.equalsIgnoreCase("donelist")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandDoneList") + " [page]");
                p.sendMessage(AQUA + " " + C("HelpDoneList"));
            } else if (allowedcmd.equalsIgnoreCase("addtime")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandAddtime"));
                int days = (pmi == null) ? 0 : pmi.getDaysToExpiration();
                if (days == 0) {
                    p.sendMessage(AQUA + " " + C("HelpAddTime1") + " " + RESET + C("WordNever"));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpAddTime1") + " " + RESET + days + AQUA + " " + C("HelpAddTime2"));
                }
            } else if (allowedcmd.equalsIgnoreCase("reload")) {
                p.sendMessage(GREEN + " /plotme reload");
                p.sendMessage(AQUA + " " + C("HelpReload"));
            } else if (allowedcmd.equalsIgnoreCase("dispose")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandDispose"));
                if (ecoon && pmi != null && pmi.getDisposePrice() != 0) {
                    p.sendMessage(AQUA + " " + C("HelpDispose") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getDisposePrice()));
                } else {
                    p.sendMessage(AQUA + " " + C("HelpDispose"));
                }
            } else if (allowedcmd.equalsIgnoreCase("buy")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandBuy"));
                p.sendMessage(AQUA + " " + C("HelpBuy"));
            } else if (allowedcmd.equalsIgnoreCase("sell")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandSell") + " [" + C("WordAmount") + "]");
                p.sendMessage(AQUA + " " + C("HelpSell") + " " + C("WordDefault") + " : " + RESET + Util().round(pmi.getSellToPlayerPrice()));
            } else if (allowedcmd.equalsIgnoreCase("sellbank")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandSellBank"));
                p.sendMessage(AQUA + " " + C("HelpSellBank") + " " + RESET + Util().round(pmi.getSellToBankPrice()));
            } else if (allowedcmd.equalsIgnoreCase("auction")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandAuction") + " [" + C("WordAmount") + "]");
                p.sendMessage(AQUA + " " + C("HelpAuction") + " " + C("WordDefault") + " : " + RESET + "1");
            } else if (allowedcmd.equalsIgnoreCase("bid")) {
                p.sendMessage(GREEN + " /plotme " + C("CommandBid") + " <" + C("WordAmount") + ">");
                p.sendMessage(AQUA + " " + C("HelpBid"));
            }
        }
        return true;
    }
}
