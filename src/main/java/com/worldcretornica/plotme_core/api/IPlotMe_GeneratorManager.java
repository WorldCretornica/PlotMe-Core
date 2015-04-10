package com.worldcretornica.plotme_core.api;

import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.schematic.Schematic;

import java.util.List;

public interface IPlotMe_GeneratorManager {

    PlotId getPlotId(ILocation location);

    PlotId getPlotId(IPlayer player);

    List<IPlayer> getPlayersInPlot(PlotId id);

    void fillRoad(PlotId id1, PlotId id2);

    void fillMiddleRoad(PlotId id1, PlotId id2);

    void setOwnerDisplay(PlotId id, String line1, String line2, String line3, String line4);

    void setSellerDisplay(PlotId id, String line1, String line2, String line3, String line4);

    void removeOwnerDisplay(PlotId id);

    void removeSellerDisplay(PlotId id);

    ILocation getPlotBottomLoc(PlotId id);

    ILocation getPlotTopLoc(PlotId id);

    void refreshPlotChunks(PlotId id);

    ILocation getTop(PlotId id);

    ILocation getBottom(PlotId id);

    Long[] clear(PlotId id, long maxBlocks, Long[] start);

    Long[] clear(ILocation bottom, ILocation top, long maxBlocks, Long[] start);

    void adjustPlotFor(PlotId id, boolean claimed, boolean protect, boolean forSale);

    boolean isBlockInPlot(PlotId id, ILocation blockLocation);

    boolean movePlot(PlotId idFrom, PlotId idTo);

    int bottomX(PlotId id);

    int bottomZ(PlotId id);

    int topX(PlotId id);

    int topZ(PlotId id);

    ILocation getPlotHome(PlotId id);

    int getPlotSize();

    int getGroundHeight();

    ILocation getPlotMiddle(PlotId id);

    Schematic getPlotSchematic(PlotId id);
}
