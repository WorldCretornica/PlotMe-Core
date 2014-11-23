package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

import java.util.Collections;
import java.util.List;

public class CmdBiomeList extends PlotCommand {

    public CmdBiomeList(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
            if (player.hasPermission("PlotMe.use.biome")) {
                List<String> biomes = serverBridge.getBiomes();

                Collections.sort(biomes);

                int page = 1;
                int pages = biomes.size() / 19 + 1;

                if (args.length > 1 && !args[1].isEmpty()) {
                    page = Integer.parseInt(args[1]);
                }

                if (page <= pages) {
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
