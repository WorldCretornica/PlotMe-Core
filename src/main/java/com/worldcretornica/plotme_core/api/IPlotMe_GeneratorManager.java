package com.worldcretornica.plotme_core.api;

import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.schematic.Schematic;

import java.util.List;
import java.util.Map;

public interface IPlotMe_GeneratorManager {

    PlotId getPlotId(ILocation location);

    PlotId getPlotId(IPlayer player);

    List<IPlayer> getPlayersInPlot(PlotId id);

    void fillRoad(PlotId id1, PlotId id2, IWorld world);

    void fillMiddleRoad(PlotId id1, PlotId id2, IWorld world);

    void setOwnerDisplay(IWorld world, PlotId id, String line1, String line2, String line3, String line4);

    void setSellerDisplay(IWorld world, PlotId id, String line1, String line2, String line3, String line4);

    void setAuctionDisplay(IWorld world, PlotId id, String line1, String line2, String line3, String line4);

    void removeOwnerDisplay(IWorld world, PlotId id);

    void removeSellerDisplay(IWorld world, PlotId id);

    void removeAuctionDisplay(IWorld world, PlotId id);

    ILocation getPlotBottomLoc(IWorld world, PlotId id);

    ILocation getPlotTopLoc(IWorld world, PlotId id);

    void setBiome(IWorld world, PlotId id, IBiome biome);

    void refreshPlotChunks(IWorld world, PlotId id);

    ILocation getTop(IWorld world, PlotId id);

    ILocation getBottom(IWorld world, PlotId id);

    void clear(ILocation bottom, ILocation top);

    Long[] clear(ILocation bottom, ILocation top, long maxBlocks, Long[] start);

    Long[] clear(IWorld world, PlotId id, long maxBlocks, Long[] start);

    void adjustPlotFor(IWorld world, PlotId id, boolean claimed, boolean protect, boolean auctioned, boolean forSale);

    boolean isBlockInPlot(PlotId id, ILocation blockLocation);

    boolean movePlot(IWorld world, PlotId idFrom, PlotId idTo);

    int bottomX(PlotId id, IWorld world);

    int bottomZ(PlotId id, IWorld world);

    int topX(PlotId id, IWorld world);

    int topZ(PlotId id, IWorld world);

    ILocation getPlotHome(IWorld world, PlotId id);

    boolean isValidId(String id);

    boolean createConfig(String worldName, Map<String, String> args);

    Map<String, String> getDefaultGenerationConfig();

    int getPlotSize(String worldName);

    int getRoadHeight(String worldName);

    ILocation getPlotMiddle(IWorld world, PlotId id);

    Schematic getPlotSchematic(IWorld world, PlotId id);
}
