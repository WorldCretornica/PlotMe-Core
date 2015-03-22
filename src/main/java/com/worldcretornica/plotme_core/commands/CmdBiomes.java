package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;

import java.util.List;

public class CmdBiomes extends PlotCommand implements CommandBase {

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
                List<String> biomes = serverBridge.getBiomes();

                int pages = biomes.size() / 19 + 1;
                int page = 1;

                if (args.length > 1) {
                    page = Integer.parseInt(args[1]);
                }

                player.sendMessage(C("WordBiomes") + " (" + page + "/" + pages + ") : ");

                for (int ctr = 0; ctr < 19; ctr++) {
                    if (biomes.size() <= ctr + (page - 1) * 19) {
                        return true;
                    } else {
                        player.sendMessage("  " + biomes.get(ctr + (page - 1) * 19));
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getUsage() {
        return null;
    }

}
