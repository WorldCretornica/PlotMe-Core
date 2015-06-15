package com.worldcretornica.plotme_core.commands;

import com.google.common.collect.Lists;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;

import java.util.List;

public class CmdBiomes extends PlotCommand {

    public CmdBiomes(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "biomes";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        if (manager.isPlotWorld(player)) {
            if (player.hasPermission(PermissionNames.USER_BIOME)) {
                int page = 1;
                List<List<String>> partition = Lists.partition(serverBridge.getBiomes(), 10);
                if (args.length == 2) {
                    page = Integer.parseInt(args[1]);
                }

                player.sendMessage(C("WordBiomes") + " (" + page + "/" + partition.size() + ") : ");
                for (String s : partition.get(page)) {
                    player.sendMessage(s);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("CmdBiomesUsage");
    }

}
