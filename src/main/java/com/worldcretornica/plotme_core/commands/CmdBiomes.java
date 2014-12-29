package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

import java.util.List;

public class CmdBiomes extends PlotCommand {

    public CmdBiomes(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String arg) {
        if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
            if (player.hasPermission(PermissionNames.USER_BIOME)) {
                List<String> biomes = serverBridge.getBiomes();

                int pages = biomes.size() / 19 + 1;
                int page;

                try {
                    page = Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    page = 1;
                }
                player.sendMessage(C("WordBiomes") + " (" + page + "/" + pages + ") : ");

                for (int ctr = 0; ctr < 19; ctr++) {
                    if (biomes.size() <= ctr + (page - 1) * 19) {
                        return true;
                    } else {
                        player.sendMessage("  §b" + biomes.get(ctr + (page - 1) * 19));
                    }
                }
            } else {
                player.sendMessage("§c" + C("MsgPermissionDenied"));
                return false;
            }
        }
        return true;
    }
}
