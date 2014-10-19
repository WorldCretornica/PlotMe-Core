package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface IBukkitPlotMe_GeneratorManager {

    String getPlotId(Location loc);

    String getPlotId(Player player);

    List<Player> getPlayersInPlot(World w, String id);

    void fillroad(String id1, String id2, World w);

    void fillmiddleroad(String id1, String id2, World w);

    void setOwnerDisplay(World world, String id, String Line1, String Line2, String Line3, String Line4);

    void setSellerDisplay(World world, String id, String Line1, String Line2, String Line3, String Line4);

    void setAuctionDisplay(World world, String id, String Line1, String Line2, String Line3, String Line4);

    void removeOwnerDisplay(World world, String id);

    void removeSellerDisplay(World world, String id);

    void removeAuctionDisplay(World world, String id);

    int getIdX(String id);

    int getIdZ(String id);

    Location getPlotBottomLoc(World world, String id);

    Location getPlotTopLoc(World world, String id);

    void setBiome(World w, String id, Biome biome);

    void refreshPlotChunks(World w, String id);

    Location getTop(World w, String id);

    Location getBottom(World w, String id);

    void clear(World w, String id);

    void clear(Location bottom, Location top);

    Long[] clear(Location bottom, Location top, long maxBlocks, boolean clearEntities, Long[] start);

    Long[] clear(World w, String id, long maxBlocks, boolean clearEntities, Long[] start);

    void adjustPlotFor(World w, String id, boolean Claimed, boolean Protect, boolean Auctioned, boolean ForSale);

    boolean isBlockInPlot(String id, Location blocklocation);

    boolean movePlot(World wFrom, World wTo, String idFrom, String idTo);

    int bottomX(String id, World w);

    int bottomZ(String id, World w);

    int topX(String id, World w);

    int topZ(String id, World w);

    void regen(World w, String id, CommandSender sender);

    Location getPlotHome(World w, String id);

    boolean isValidId(String id);

    boolean createConfig(String worldname, Map<String, String> args, CommandSender cs);

    Map<String, String> getDefaultGenerationConfig();

    int getPlotSize(String worldname);

    int getRoadHeight(String worldname);
    
}
