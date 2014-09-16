package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmdBiomeList extends PlotCommand {

    public CmdBiomeList(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(CommandSender s, String[] args) {
        if (!(s instanceof Player) || plugin.cPerms(s, "PlotMe.use.biome")) {
            List<String> biomes = new ArrayList<>();
            
            for (Biome b : Biome.values()) {
                biomes.add(b.name());
            }

            Collections.sort(biomes);
            
            int biomesperpage = 19;
            int page = 1;
            int pages = (int) Math.ceil(((double) biomes.size()) / biomesperpage);
            
            if (args.length > 1 && !args[1].isEmpty()) {
                try{
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException notused) {}
            }
            
            if (page <= pages) {
                page = 1;
            }
            
            s.sendMessage(C("WordBiomes") + " (" + page + "/" + pages + ") : ");
            
            for (int ctr = 0; ctr < biomesperpage; ctr++) {
                if (biomes.size() <= ctr + ((page - 1) * biomesperpage)) {
                    return true;
                } else {
                    s.sendMessage("  " + AQUA + biomes.get(ctr + ((page - 1) * biomesperpage)));
                }
            }

            /*
            List<String> column1 = new ArrayList<>();
            List<String> column2 = new ArrayList<>();
            List<String> column3 = new ArrayList<>();

            for (int ctr = 0; ctr < biomes.size(); ctr++) {
                if (ctr < biomes.size() / 3) {
                    column1.add(biomes.get(ctr));
                } else if (ctr < biomes.size() * 2 / 3) {
                    column2.add(biomes.get(ctr));
                } else {
                    column3.add(biomes.get(ctr));
                }
            }

            for (int ctr = 0; ctr < column1.size(); ctr++) {
                String b;
                int nameLength;

                b = Util().FormatBiome(column1.get(ctr));
                nameLength = MinecraftFontWidthCalculator.getStringWidth(b);
                line.append(b).append(Util().whitespace(432 - nameLength));

                if (ctr < column2.size()) {
                    b = Util().FormatBiome(column2.get(ctr));
                    nameLength = MinecraftFontWidthCalculator.getStringWidth(b);
                    line.append(b).append(Util().whitespace(432 - nameLength));
                }

                if (ctr < column3.size()) {
                    b = Util().FormatBiome(column3.get(ctr));
                    line.append(b);
                }

                s.sendMessage("" + AQUA + line);
                //i = 0;
                line = new StringBuilder();

                /*int nameLength = MinecraftFontWidthCalculator.getStringWidth(b);

                 i += 1;
                 if(i == 3)
                 {
                 line.append(b);
                 s.sendMessage("" + BLUE + line);
                 i = 0;
                 line = new StringBuilder();
                 }
                 else
                 {
                 line.append(b).append(whitespace(318 - nameLength));
                 }
            }*/
        } else {
            s.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }
}
