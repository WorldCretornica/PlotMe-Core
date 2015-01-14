package com.worldcretornica.plotme_core.api;

import java.util.List;
import java.util.Map;

public interface IPlotMe_GeneratorManager {

    String getPlotId(ILocation location);

    String getPlotId(IPlayer player);

    List<IPlayer> getPlayersInPlot(String id);

    void fillroad(String id1, String id2, World world);

    void fillmiddleroad(String id1, String id2, World world);

    void setOwnerDisplay(World world, String id, String line1, String line2, String line3, String line4);

    void setSellerDisplay(World world, String id, String line1, String line2, String line3, String line4);

    void setAuctionDisplay(World world, String id, String line1, String line2, String line3, String line4);

    void removeOwnerDisplay(World world, String id);

    void removeSellerDisplay(World world, String id);

    void removeAuctionDisplay(World world, String id);

    int getIdX(String id);

    int getIdZ(String id);

    ILocation getPlotBottomLoc(World world, String id);

    ILocation getPlotTopLoc(World world, String id);

    void setBiome(World world, String id, IBiome biome);

    void refreshPlotChunks(World world, String id);

    ILocation getTop(World world, String id);

    ILocation getBottom(World world, String id);

    void clear(ILocation bottom, ILocation top);

    Long[] clear(ILocation bottom, ILocation top, long maxBlocks, Long[] start);

    Long[] clear(World world, String id, long maxBlocks, Long[] start);

    void adjustPlotFor(World world, String id, boolean claimed, boolean protect, boolean auctionned, boolean forSale);

    boolean isBlockInPlot(String id, ILocation blocklocation);

    boolean movePlot(World world, String idFrom, String idTo);

    int bottomX(String id, World world);

    int bottomZ(String id, World world);

    int topX(String id, World world);

    int topZ(String id, World world);

    ILocation getPlotHome(World world, String id);

    boolean isValidId(String id);

    boolean createConfig(String worldname, Map<String, String> args);

    Map<String, String> getDefaultGenerationConfig();

    int getPlotSize(String worldname);

    int getRoadHeight(String worldname);
}
