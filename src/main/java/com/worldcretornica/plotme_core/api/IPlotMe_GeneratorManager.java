package com.worldcretornica.plotme_core.api;

import java.util.List;
import java.util.Map;

public interface IPlotMe_GeneratorManager {

    String getPlotId(ILocation loc);

    String getPlotId(IPlayer player);

    List<IPlayer> getPlayersInPlot(IWorld w, String id);

    void fillroad(String id1, String id2, IWorld w);

    void fillmiddleroad(String id1, String id2, IWorld w);

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

    void setBiome(IWorld w, String id, IBiome biome);

    void refreshPlotChunks(IWorld w, String id);

    ILocation getTop(IWorld w, String id);

    ILocation getBottom(IWorld w, String id);

    void clear(IWorld w, String id);

    void clear(ILocation bottom, ILocation top);

    Long[] clear(ILocation bottom, ILocation top, long maxBlocks, boolean clearEntities, Long[] start);

    Long[] clear(IWorld w, String id, long maxBlocks, boolean clearEntities, Long[] start);

    void adjustPlotFor(IWorld w, String id, boolean Claimed, boolean Protect, boolean Auctionned, boolean ForSale);

    boolean isBlockInPlot(String id, ILocation blocklocation);

    boolean movePlot(IWorld wFrom, IWorld wTo, String idFrom, String idTo);

    int bottomX(String id, IWorld w);

    int bottomZ(String id, IWorld w);

    int topX(String id, IWorld w);

    int topZ(String id, IWorld w);

    void regen(IWorld w, String id, ICommandSender sender);

    ILocation getPlotHome(IWorld w, String id);

    boolean isValidId(String id);

    boolean createConfig(String worldname, Map<String, String> args, ICommandSender cs);

    Map<String, String> getDefaultGenerationConfig();

    int getPlotSize(String worldname);

    int getRoadHeight(String worldname);
}
