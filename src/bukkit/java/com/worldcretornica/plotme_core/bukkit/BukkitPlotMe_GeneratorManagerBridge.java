package com.worldcretornica.plotme_core.bukkit;

import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.IBukkitPlotMe_GeneratorManager;
import com.worldcretornica.schematic.Schematic;

import java.util.List;

public class BukkitPlotMe_GeneratorManagerBridge implements IPlotMe_GeneratorManager {

    private final IBukkitPlotMe_GeneratorManager generatorManager;

    public BukkitPlotMe_GeneratorManagerBridge(IBukkitPlotMe_GeneratorManager generatorManager) {
        this.generatorManager = generatorManager;
    }

    @Override
    public PlotId getPlotId(ILocation location) {
        return generatorManager.getPlotId(location);
    }

    @Override
    public PlotId getPlotId(IPlayer player) {
        return generatorManager.getPlotId(player);
    }

    @Override
    public List<IPlayer> getPlayersInPlot(PlotId id) {
        return generatorManager.getPlayersInPlot(id);
    }

    @Override
    public void fillRoad(PlotId id1, PlotId id2, IWorld world) {
        generatorManager.fillRoad(id1, id2, world);
    }

    @Override
    public void fillMiddleRoad(PlotId id1, PlotId id2, IWorld world) {
        generatorManager.fillMiddleRoad(id1, id2, world);
    }

    @Override
    public void setOwnerDisplay(IWorld world, PlotId id, String line1, String line2, String line3, String line4) {
        generatorManager.setOwnerDisplay(world, id, line1, line2, line3, line4);
    }

    @Override
    public void setSellerDisplay(IWorld world, PlotId id, String line1, String line2, String line3, String line4) {
        generatorManager.setSellerDisplay(world, id, line1, line2, line3, line4);
    }


    @Override
    public void removeOwnerDisplay(IWorld world, PlotId id) {
        generatorManager.removeOwnerDisplay(world, id);
    }

    @Override
    public void removeSellerDisplay(IWorld world, PlotId id) {
        generatorManager.removeSellerDisplay(world, id);
    }

    @Override
    public ILocation getPlotBottomLoc(IWorld world, PlotId id) {
        return new BukkitLocation(generatorManager.getPlotBottomLoc(world, id));
    }

    @Override
    public ILocation getPlotTopLoc(IWorld world, PlotId id) {
        return new BukkitLocation(generatorManager.getPlotTopLoc(world, id));
    }

    @Override
    public void refreshPlotChunks(IWorld world, PlotId id) {
        generatorManager.refreshPlotChunks(world, id);
    }

    @Override
    public ILocation getTop(IWorld world, PlotId id) {
        return new BukkitLocation(generatorManager.getTop(world, id));
    }

    @Override
    public ILocation getBottom(IWorld world, PlotId id) {
        return new BukkitLocation(generatorManager.getBottom(world, id));
    }

    @Override
    public Long[] clear(IWorld world, PlotId id, long maxBlocks, Long[] start) {
        return generatorManager.clear(world, id, maxBlocks, start);
    }

    @Deprecated
    @Override
    public void adjustPlotFor(IWorld world, PlotId id, boolean claimed, boolean protect, boolean auctioned, boolean forSale) {
        adjustPlotFor(world, id, claimed, protect, forSale);
    }

    @Override
    public void adjustPlotFor(IWorld world, PlotId id, boolean claimed, boolean protect, boolean forSale) {
        generatorManager.adjustPlotFor(world, id, claimed, protect, forSale);
    }

    @Override
    public boolean isBlockInPlot(PlotId id, ILocation blockLocation) {
        return generatorManager.isBlockInPlot(id, blockLocation);
    }

    @Override
    public boolean movePlot(IWorld world, PlotId idFrom, PlotId idTo) {
        return generatorManager.movePlot(world, idFrom, idTo);
    }

    @Override
    public int bottomX(PlotId id, IWorld world) {
        return generatorManager.bottomX(id, world);
    }

    @Override
    public int bottomZ(PlotId id, IWorld world) {
        return generatorManager.bottomZ(id, world);
    }

    @Override
    public int topX(PlotId id, IWorld world) {
        return generatorManager.topX(id, world);
    }

    @Override
    public int topZ(PlotId id, IWorld world) {
        return generatorManager.topZ(id, world);
    }

    @Override
    public ILocation getPlotHome(IWorld world, PlotId id) {
        return new BukkitLocation(generatorManager.getPlotHome(world, id));
    }

    @Override
    public int getPlotSize() {
        return generatorManager.getPlotSize();
    }

    @Override
    public int getGroundHeight() {
        return generatorManager.getGroundHeight();
    }

    @Override
    public ILocation getPlotMiddle(IWorld world, PlotId id) {
        return new BukkitLocation(generatorManager.getPlotMiddle(world, id));
    }

    @Override
    public Schematic getPlotSchematic(IWorld world, PlotId id) {
        return generatorManager.getPlotSchematic(world, id);
    }
}
