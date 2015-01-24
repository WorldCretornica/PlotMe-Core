package com.worldcretornica.plotme_core.sponge.api;

import java.util.List;
import java.util.Map;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;

public interface ISpongePlotMe_GeneratorManager {

    String getPlotId(Location location);
    
    String getPlotId(Player player);
    
    List<Player> getPlayersInPlot(String id);
    
    void fillroad(String id1, String id2, World world);
    
    void fillmiddleroad(String id1, String id2, World world);

    void setOwnerDisplay(World world, String id, String line1, String line2, String line3, String line4);

    void setSellerDisplay(World world, String id, String line1, String line2, String Line3, String line4);

    void setAuctionDisplay(World world, String id, String line1, String line2, String Line3, String line4);

    void removeOwnerDisplay(World world, String id);

    void removeSellerDisplay(World world, String id);

    void removeAuctionDisplay(World world, String id);

    int getIdX(String id);

    int getIdZ(String id);

    Location getPlotBottomLoc(World world, String id);

    Location getPlotTopLoc(World world, String id);

    void setBiome(World world, String id, BiomeType biome);

    void refreshPlotChunks(World world, String id);

    Location getTop(World world, String id);

    Location getBottom(World world, String id);

    void clear(Location bottom, Location top);

    Long[] clear(Location bottom, Location top, long maxBlocks, Long[] start);

    Long[] clear(World world, String id, long maxBlocks, Long[] start);

    void adjustPlotFor(World world, String id, boolean claimed, boolean protect, boolean auctioned, boolean forSale);

    boolean isBlockInPlot(String id, Location location);

    boolean movePlot(World world, String idFrom, String idTo);

    int bottomX(String id, World world);

    int bottomZ(String id, World world);

    int topX(String id, World world);

    int topZ(String id, World world);

    Location getPlotHome(World world, String id);

    boolean isValidId(String id);

    boolean createConfig(String worldName, Map<String, String> args);

    Map<String, String> getDefaultGenerationConfig();

    int getPlotSize(String worldName);

    int getRoadHeight(String worldName);
}
