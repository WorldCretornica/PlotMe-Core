package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.schematic.Schematic;
import org.bukkit.Location;

import java.util.List;

public interface IBukkitPlotMe_GeneratorManager {

    PlotId getPlotId(ILocation loc);

    PlotId getPlotId(IPlayer player);

    List<IPlayer> getPlayersInPlot(PlotId id);

    void fillRoad(PlotId id1, PlotId id2, IWorld world);

    void fillMiddleRoad(PlotId id1, PlotId id2, IWorld world);

    void setOwnerDisplay(IWorld world, PlotId id, String line1, String line2, String line3, String line4);

    void setSellerDisplay(IWorld world, PlotId id, String line1, String line2, String Line3, String line4);

    void removeOwnerDisplay(IWorld world, PlotId id);

    void removeSellerDisplay(IWorld world, PlotId id);

    Location getPlotBottomLoc(IWorld world, PlotId id);

    Location getPlotTopLoc(IWorld world, PlotId id);

    void refreshPlotChunks(IWorld world, PlotId id);

    Location getTop(IWorld world, PlotId id);

    Location getBottom(IWorld world, PlotId id);

    Long[] clear(ILocation bottom, ILocation top, long maxBlocks, Long[] start);

    Long[] clear(IWorld world, PlotId id, long maxBlocks, Long[] start);

    void adjustPlotFor(IWorld world, PlotId id, boolean claimed, boolean protect, boolean forSale);

    boolean isBlockInPlot(PlotId id, ILocation location);

    boolean movePlot(IWorld world, PlotId idFrom, PlotId idTo);

    int bottomX(PlotId id, IWorld world);

    int bottomZ(PlotId id, IWorld world);

    int topX(PlotId id, IWorld world);

    int topZ(PlotId id, IWorld world);

    Location getPlotHome(IWorld world, PlotId id);

    int getPlotSize();

    int getGroundHeight();

    Location getPlotMiddle(IWorld world, PlotId id);

    Schematic getPlotSchematic(IWorld world, PlotId id);
}
