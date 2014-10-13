package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;

import java.util.Collections;
import java.util.List;

public class CmdBiomeList extends PlotCommand {

    public CmdBiomeList(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p, String[] args) {
        if (plugin.cPerms(p, "PlotMe.use.biome")) {
            List<String> biomes = sob.getBiomes();
            
            Collections.sort(biomes);
            
            int biomesperpage = 19;
            int page = 1;
            int pages = (int) Math.ceil((double) biomes.size() / biomesperpage);
            
            if (args.length > 1 && !args[1].isEmpty()) {
                try{
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException notused) {}
            }
            
            if (page <= pages) {
                page = 1;
            }

            p.sendMessage(C("WordBiomes") + " (" + page + "/" + pages + ") : ");
            
            for (int ctr = 0; ctr < biomesperpage; ctr++) {
                if (biomes.size() <= ctr + (page - 1) * biomesperpage) {
                    return true;
                } else {
                    p.sendMessage("  " + AQUA + biomes.get(ctr + (page - 1) * biomesperpage));
                }
            }
        } else {
            p.sendMessage(RED + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
