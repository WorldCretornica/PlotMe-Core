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

    private PlotMe_Core api;
    private PlotMe_CorePlugin plugin;
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
    private final CmdComment comment;
    private final CmdComments comments;
    private final CmdDeny deny;
    private final CmdDispose dispose;
    private final CmdDone done;
    private final CmdDoneList donelist;
    private final CmdExpired expired;
    private final CmdHome home;
    private final CmdID id;
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
        plugin = instance;
        api = plugin.getAPI();
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
        comment = new CmdComment(api);
        comments = new CmdComments(api);
        deny = new CmdDeny(api);
        dispose = new CmdDispose(api);
        done = new CmdDone(api);
        donelist = new CmdDoneList(api);
        expired = new CmdExpired(api);
        home = new CmdHome(api);
        id = new CmdID(api);
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
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (l.equalsIgnoreCase("plotme") || l.equalsIgnoreCase("plot") || l.equalsIgnoreCase("p")) {
            if (!(s instanceof Player)) {
                if (args.length == 0) {
                    s.sendMessage(C("ConsoleHelpMain"));
                    s.sendMessage(" - /plotme reload");
                    s.sendMessage(C("ConsoleHelpReload"));
                    return true;
                } else {
                    String a0 = args[0];
                    if (a0.equalsIgnoreCase("reload")) {
                        return reload.exec(new BukkitCommandSender(s));
                    }
                    if (a0.equalsIgnoreCase(C("CommandResetExpired"))) {
                        return resetexpired.exec(new BukkitCommandSender(s), args);
                    }
                    if (a0.equalsIgnoreCase(C("CommandCreateWorld"))) {
                        return createworld.exec(new BukkitCommandSender(s), args);
                    }
                }
            } else {
                IPlayer p = new BukkitPlayer((Player) s);

                if (args.length == 0) {
                    return showhelp.exec(p, 1);
                } else {
                    String a0 = args[0];
                    int ipage = -1;

                    try {
                        ipage = Integer.parseInt(a0);
                    } catch (NumberFormatException ignored) {
                    }

                    if (ipage == -1) {
                        if (a0.equalsIgnoreCase(C("CommandHelp"))) {
                            if (args.length > 1) {
                                String a1 = args[1];
                                ipage = -1;

                                try {
                                    ipage = Integer.parseInt(a1);
                                } catch (NumberFormatException ignored) {
                                }
                            }

                            if (ipage == -1) {
                                return showhelp.exec(p, 1);
                            } else {
                                return showhelp.exec(p, ipage);
                            }
                        }
                        if (a0.equalsIgnoreCase(C("CommandClaim"))) {
                            return claim.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase(C("CommandAuto"))) {
                            return auto.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase(C("CommandInfo")) || a0.equalsIgnoreCase("i")) {
                            return info.exec(p);
                        }
                        if (a0.equalsIgnoreCase(C("CommandComment"))) {
                            return comment.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase(C("CommandComments")) || a0.equalsIgnoreCase("c")) {
                            return comments.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase(C("CommandBiome")) || a0.equalsIgnoreCase("b")) {
                            return biome.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase(C("CommandBiomelist"))) {
                            return biomelist.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase("id")) {
                            return id.exec(p);
                        }
                        if (a0.equalsIgnoreCase(C("CommandTp"))) {
                            return tp.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase(C("CommandClear"))) {
                            return clear.exec(p);
                        }
                        if (a0.equalsIgnoreCase(C("CommandReset"))) {
                            return reset.exec(p);
                        }
                        if (a0.equalsIgnoreCase(C("CommandAdd")) || a0.equalsIgnoreCase("+")) {
                            return add.exec(p, args);
                        }
                        if (plugin.getConfig().getBoolean("allowToDeny")) {
                            if (a0.equalsIgnoreCase(C("CommandDeny"))) {
                                return deny.exec(p, args);
                            }
                            if (a0.equalsIgnoreCase(C("CommandUndeny"))) {
                                return undeny.exec(p, args);
                            }
                        }
                        if (a0.equalsIgnoreCase(C("CommandRemove")) || a0.equalsIgnoreCase("-")) {
                            return remove.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase(C("CommandSetowner")) || a0.equalsIgnoreCase("o")) {
                            return setowner.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase(C("CommandMove")) || a0.equalsIgnoreCase("m")) {
                            return move.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase("reload")) {
                            return reload.exec(new BukkitCommandSender(s));
                        }
                        if (a0.equalsIgnoreCase(C("CommandWEAnywhere"))) {
                            return weanywhere.exec(p);
                        }
                        if (a0.equalsIgnoreCase(C("CommandList"))) {
                            return plotlist.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase(C("CommandExpired"))) {
                            return expired.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase(C("CommandAddtime"))) {
                            return addtime.exec(p);
                        }
                        if (a0.equalsIgnoreCase(C("CommandDone"))) {
                            return done.exec(p);
                        }
                        if (a0.equalsIgnoreCase(C("CommandDoneList"))) {
                            return donelist.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase(C("CommandProtect"))) {
                            return protect.exec(p);
                        }
                        if (a0.equalsIgnoreCase(C("CommandSell"))) {
                            return sell.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase(C("CommandDispose"))) {
                            return dispose.exec(p);
                        }
                        if (a0.equalsIgnoreCase(C("CommandAuction"))) {
                            return auction.exec(p, args);
                        }
                        if (a0.equalsIgnoreCase(C("CommandBuy"))) {
                            return buy.exec(p);
                        }
                        if (a0.equalsIgnoreCase(C("CommandBid"))) {
                            return bid.exec(p, args);
                        }
                        if (a0.startsWith(C("CommandHome")) || a0.startsWith("h")) {
                            return home.exec(p, args);
                        }
                    } else {
                        return showhelp.exec(p, ipage);
                    }
                }
            }
        }
        return false;
    }
}
