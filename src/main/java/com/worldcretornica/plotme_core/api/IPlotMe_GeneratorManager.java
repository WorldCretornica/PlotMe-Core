package com.worldcretornica.plotme_core.api;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.utils.ClearEntry;
import com.worldcretornica.schematic.Schematic;

import java.util.List;

public interface IPlotMe_GeneratorManager {

    PlotId getPlotId(Vector loc);

    PlotId getPlotId(IPlayer player);

    List<IPlayer> getPlayersInPlot(PlotId id);

    void clearEntities(Vector bottom, Vector top);

    void fillRoad(PlotId id1, PlotId id2);

    void fillMiddleRoad(PlotId id1, PlotId id2);

    void setOwnerDisplay(PlotId id, String line1, String line2, String line3, String line4);

    void setSellerDisplay(PlotId id, String line1, String line2, String line3, String line4);

    void removeOwnerDisplay(PlotId id);

    void removeSellerDisplay(PlotId id);

    Vector getPlotBottomLoc(PlotId id);

    Vector getPlotTopLoc(PlotId id);

    void refreshPlotChunks(PlotId id);

    Vector getTop(PlotId id);

    Vector getBottom(PlotId id);

    void clear(Vector bottom, Vector top, PlotId clearMap, ClearEntry entry);

    void adjustPlotFor(Plot id, boolean claimed, boolean protect, boolean forSale);

    boolean isBlockInPlot(PlotId id, Vector location);

    boolean movePlot(PlotId idFrom, PlotId idTo);

    int bottomX(PlotId id);

    int bottomZ(PlotId id);

    int topX(PlotId id);

    int topZ(PlotId id);

    Location getPlotHome(PlotId id);

    int getPlotSize();

    int getGroundHeight();

    Vector getPlotMiddle(PlotId id);

    Schematic getPlotSchematic(PlotId id);

    void setBiome(PlotId id, String biome);
}
