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

    void fillroad(PlotId id1, PlotId id2, World world);

    void fillmiddleroad(PlotId id1, PlotId id2, World world);

    void setOwnerDisplay(World world, PlotId id, String line1, String line2, String line3, String line4);

    void setSellerDisplay(World world, PlotId id, String line1, String line2, String line3, String line4);

    void removeOwnerDisplay(World world, PlotId id);

    void removeSellerDisplay(World world, PlotId id);

    void removeAuctionDisplay(World world, PlotId id);

    Location getPlotBottomLoc(World world, PlotId id);

    Location getPlotTopLoc(World world, PlotId id);

    void setBiome(World world, PlotId id, BiomeType biome);

    void refreshPlotChunks(World world, PlotId id);

    Location getTop(World world, PlotId id);

    Location getBottom(World world, PlotId id);

    void clear(Location bottom, Location top);

    Long[] clear(Location bottom, Location top, long maxBlocks, Long[] start);

    Long[] clear(World world, PlotId id, long maxBlocks, Long[] start);

    void adjustPlotFor(World world, PlotId id, boolean claimed, boolean protect, boolean forSale);

    boolean isBlockInPlot(PlotId id, Location location);

    boolean movePlot(World world, PlotId idFrom, PlotId idTo);

    int bottomX(PlotId id, World world);

    int bottomZ(PlotId id, World world);

    int topX(PlotId id, World world);

    int topZ(PlotId id, World world);

    Location getPlotHome(World world, PlotId id);

    boolean isValidId(String id);

    int getPlotSize();

    int getRoadHeight();

    Location getPlotMiddle(World world, PlotId id);

    Schematic getPlotSchematic(World world, PlotId id);
}
