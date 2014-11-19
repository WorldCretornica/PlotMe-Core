package com.worldcretornica.plotme_core.bukkit;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitCommandSender;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitCommand implements CommandExecutor {

    private final PlotMe_Core api;
    private final CmdAdd add;
    private final CmdAddTime addtime;
    private final CmdAuction auction;
    private final CmdAuto auto;
    private final CmdBid bid;
    private final CmdBiome biome;
    private final CmdBiomeList biomelist;
    private final CmdBuy buy;
    private final CmdClaim claim;
    private final CmdClear clear;
    private final CmdDeny deny;
    private final CmdDispose dispose;
    private final CmdDone done;
    private final CmdDoneList donelist;
    private final CmdExpired expired;
    private final CmdHome home;
    private final CmdInfo info;
    private final CmdMove move;
    private final CmdPlotList plotlist;
    private final CmdProtect protect;
    private final CmdReload reload;
    private final CmdRemove remove;
    private final CmdReset reset;
    private final CmdResetExpired resetexpired;
    private final CmdSell sell;
    private final CmdSetOwner setowner;
    private final CmdShowHelp showhelp;
    private final CmdTP tp;
    private final CmdUndeny undeny;
    private final CmdWEAnywhere weanywhere;
    private final CmdCreateWorld createworld;

    public BukkitCommand(PlotMe_CorePlugin instance) {
        api = instance.getAPI();
        add = new CmdAdd(api);
        addtime = new CmdAddTime(api);
        auction = new CmdAuction(api);
        auto = new CmdAuto(api);
        bid = new CmdBid(api);
        biome = new CmdBiome(api);
        biomelist = new CmdBiomeList(api);
        buy = new CmdBuy(api);
        claim = new CmdClaim(api);
        clear = new CmdClear(api);
        deny = new CmdDeny(api);
        dispose = new CmdDispose(api);
        done = new CmdDone(api);
        donelist = new CmdDoneList(api);
        expired = new CmdExpired(api);
        home = new CmdHome(api);
        info = new CmdInfo(api);
        move = new CmdMove(api);
        plotlist = new CmdPlotList(api);
        protect = new CmdProtect(api);
        reload = new CmdReload(api);
        remove = new CmdRemove(api);
        reset = new CmdReset(api);
        resetexpired = new CmdResetExpired(api);
        sell = new CmdSell(api);
        setowner = new CmdSetOwner(api);
        showhelp = new CmdShowHelp(api);
        tp = new CmdTP(api);
        undeny = new CmdUndeny(api);
        weanywhere = new CmdWEAnywhere(api);
        createworld = new CmdCreateWorld(api);
    }

    private String C(String caption) {
        return api.getUtil().C(caption);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ("plotme".equalsIgnoreCase(label)) {
            if (!(sender instanceof Player)) {
                if (args.length == 0) {
                    sender.sendMessage(C("ConsoleHelpMain"));
                    sender.sendMessage("- /plotme reload");
                    sender.sendMessage(C("ConsoleHelpReload"));
                    return true;
                } else {
                    String a0 = args[0];
                    if ("reload".equalsIgnoreCase(a0)) {
                        return reload.exec();
                    }
                    if ("resetexpired".equalsIgnoreCase(a0)) {
                        return resetexpired.exec(new BukkitCommandSender(sender), args);
                    }
                    if ("createworld".equalsIgnoreCase(a0)) {
                        return createworld.exec(new BukkitCommandSender(sender), args);
                    }
                }
            } else {
                IPlayer player = new BukkitPlayer((Player) sender);

                if (args.length == 0) {
                    return showhelp.exec(player, 1);
                } else {
                    String a0 = args[0];
                    int ipage = -1;

                    try {
                        ipage = Integer.parseInt(a0);
                    } catch (NumberFormatException ignored) {
                    }

                    if (ipage == -1) {
                        if ("help".equalsIgnoreCase(a0)) {
                            if (args.length > 1) {
                                String a1 = args[1];
                                ipage = -1;

                                try {
                                    ipage = Integer.parseInt(a1);
                                } catch (NumberFormatException ignored) {
                                }
                            }

                            if (ipage == -1) {
                                return showhelp.exec(player, 1);
                            } else {
                                return showhelp.exec(player, ipage);
                            }
                        }
                        if ("claim".equalsIgnoreCase(a0)) {
                            return claim.exec(player, args);
                        }
                        if ("auto".equalsIgnoreCase(a0)) {
                            return auto.exec(player, args);
                        }
                        if ("info".equalsIgnoreCase(a0) || "i".equalsIgnoreCase(a0)) {
                            return info.exec(player);
                        }
                        if ("biome".equalsIgnoreCase(a0)) {
                            return biome.exec(player, args);
                        }
                        if ("biomelist".equalsIgnoreCase(a0)) {
                            return biomelist.exec(player, args);
                        }
                        if ("tp".equalsIgnoreCase(a0)) {
                            return tp.exec(player, args);
                        }
                        if ("clear".equalsIgnoreCase(a0)) {
                            return clear.exec(player);
                        }
                        if ("reset".equalsIgnoreCase(a0)) {
                            return reset.exec(player);
                        }
                        if ("add".equalsIgnoreCase(a0) || "+".equalsIgnoreCase(a0)) {
                            return add.exec(player, args);
                        }
                        if ("deny".equalsIgnoreCase(a0)) {
                            return deny.exec(player, args);
                        }
                        if ("undeny".equalsIgnoreCase(a0)) {
                            return undeny.exec(player, args);
                        }
                        if ("remove".equalsIgnoreCase(a0) || "-".equalsIgnoreCase(a0)) {
                            return remove.exec(player, args);
                        }
                        if ("setowner".equalsIgnoreCase(a0)) {
                            return setowner.exec(player, args);
                        }
                        if ("move".equalsIgnoreCase(a0)) {
                            return move.exec(player, args);
                        }
                        if ("reload".equalsIgnoreCase(a0)) {
                            return reload.exec();
                        }
                        if ("weanywhere".equalsIgnoreCase(a0)) {
                            return weanywhere.exec(player);
                        }
                        if ("list".equalsIgnoreCase(a0)) {
                            return plotlist.exec(player, args);
                        }
                        if ("expired".equalsIgnoreCase(a0)) {
                            return expired.exec(player, args);
                        }
                        if ("addtime".equalsIgnoreCase(a0)) {
                            return addtime.exec(player);
                        }
                        if ("done".equalsIgnoreCase(a0)) {
                            return done.exec(player);
                        }
                        if ("donelist".equalsIgnoreCase(a0)) {
                            return donelist.exec(player, args);
                        }
                        if ("protect".equalsIgnoreCase(a0)) {
                            return protect.exec(player);
                        }
                        if ("sell".equalsIgnoreCase(a0)) {
                            return sell.exec(player, args);
                        }
                        if ("dispose".equalsIgnoreCase(a0)) {
                            return dispose.exec(player);
                        }
                        if ("auction".equalsIgnoreCase(a0)) {
                            return auction.exec(player, args);
                        }
                        if ("buy".equalsIgnoreCase(a0)) {
                            return buy.exec(player);
                        }
                        if ("bid".equalsIgnoreCase(a0)) {
                            return bid.exec(player, args);
                        }
                        if (a0.startsWith("home") || a0.startsWith("h")) {
                            return home.exec(player, args);
                        }
                    } else {
                        return showhelp.exec(player, ipage);
                    }
                }
            }
        }
        return false;
    }
}
