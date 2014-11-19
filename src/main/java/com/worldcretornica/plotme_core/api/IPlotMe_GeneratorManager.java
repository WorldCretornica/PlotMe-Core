package com.worldcretornica.plotme_core.api;

import java.util.List;
import java.util.Map;

public interface IPlotMe_GeneratorManager {

    String getPlotId(ILocation location);

    String getPlotId(IPlayer player);

    List<IPlayer> getPlayersInPlot(IWorld world, String id);

    void fillroad(String id1, String id2, IWorld world);

    void fillmiddleroad(String id1, String id2, IWorld world);

    void setOwnerDisplay(IWorld world, String id, String Line1, String Line2, String Line3, String Line4);

    void setSellerDisplay(IWorld world, String id, String Line1, String Line2, String Line3, String Line4);

    void setAuctionDisplay(IWorld world, String id, String Line1, String Line2, String Line3, String Line4);

    void removeOwnerDisplay(IWorld world, String id);

    void removeSellerDisplay(IWorld world, String id);

    void removeAuctionDisplay(IWorld world, String id);

    int getIdX(String id);

    int getIdZ(String id);

    ILocation getPlotBottomLoc(IWorld world, String id);

    ILocation getPlotTopLoc(IWorld world, String id);

    void setBiome(IWorld world, String id, IBiome biome);

    void refreshPlotChunks(IWorld world, String id);

    ILocation getTop(IWorld world, String id);

    ILocation getBottom(IWorld world, String id);

    void clear(IWorld world, String id);

    void clear(ILocation bottom, ILocation top);

    Long[] clear(ILocation bottom, ILocation top, long maxBlocks, Long[] start);

    Long[] clear(IWorld world, String id, long maxBlocks, Long[] start);

    void adjustPlotFor(IWorld world, String id, boolean claimed, boolean protect, boolean auctionned, boolean forSale);

    boolean isBlockInPlot(String id, ILocation blocklocation);

    boolean movePlot(IWorld world, String idFrom, String idTo);

    int bottomX(String id, IWorld world);

    int bottomZ(String id, IWorld world);

    int topX(String id, IWorld world);

    int topZ(String id, IWorld world);

    ILocation getPlotHome(IWorld world, String id);

    boolean isValidId(String id);

    boolean createConfig(String worldname, Map<String, String> args);

    Map<String, String> getDefaultGenerationConfig();

    int getPlotSize(String worldname);

    int getRoadHeight(String worldname);
}
