package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.schematic.Schematic;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;

import java.util.List;

public interface ISpongePlotMe_GeneratorManager {

    PlotId getPlotId(Location location);

    PlotId getPlotId(Player player);

    List<Player> getPlayersInPlot(PlotId id);

    void fillroad(PlotId id1, PlotId id2);

    void fillmiddleroad(PlotId id1, PlotId id2);

    void setOwnerDisplay(PlotId id, String line1, String line2, String line3, String line4);

    void setSellerDisplay(PlotId id, String line1, String line2, String line3, String line4);

    void removeOwnerDisplay(PlotId id);

    void removeSellerDisplay(PlotId id);

    void removeAuctionDisplay(World world, PlotId id);

    Location getPlotBottomLoc(PlotId id);

    Location getPlotTopLoc(PlotId id);

    void setBiome(World world, PlotId id, BiomeType biome);

    void refreshPlotChunks(PlotId id);

    Location getTop(PlotId id);

    Location getBottom(PlotId id);

    void clear(Location bottom, Location top);

    Long[] clear(Location bottom, Location top, long maxBlocks, Long[] start);

    Long[] clear(PlotId id, long maxBlocks, Long[] start);

    void adjustPlotFor(PlotId id, boolean claimed, boolean protect, boolean forSale);

    boolean isBlockInPlot(PlotId id, Location location);

    boolean movePlot(PlotId idFrom, PlotId idTo);

    int bottomX(PlotId id);

    int bottomZ(PlotId id);

    int topX(PlotId id);

    int topZ(PlotId id);

    Location getPlotHome(PlotId id);

    boolean isValidId(String id);

    int getPlotSize();

    int getGroundHeight();

    Location getPlotMiddle(PlotId id);

    Schematic getPlotSchematic(PlotId id);
}
