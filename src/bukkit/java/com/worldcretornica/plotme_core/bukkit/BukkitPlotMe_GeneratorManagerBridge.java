package com.worldcretornica.plotme_core.bukkit;

import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import com.worldcretornica.plotme_core.bukkit.api.IBukkitPlotMe_GeneratorManager;
import com.worldcretornica.schematic.Schematic;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BukkitPlotMe_GeneratorManagerBridge implements IPlotMe_GeneratorManager {

    private final IBukkitPlotMe_GeneratorManager generatorManager;

    public BukkitPlotMe_GeneratorManagerBridge(IBukkitPlotMe_GeneratorManager generatorManager) {
        this.generatorManager = generatorManager;
    }

    @Override
    public PlotId getPlotId(ILocation location) {
        return generatorManager.getPlotId(((BukkitLocation) location).getLocation());
    }

    @Override
    public PlotId getPlotId(IPlayer player) {
        return generatorManager.getPlotId(((BukkitPlayer) player).getPlayer());
    }

    @Override
    public List<IPlayer> getPlayersInPlot(PlotId id) {
        List<IPlayer> players = new ArrayList<>();

        for (Player player : generatorManager.getPlayersInPlot(id)) {
            players.add(new BukkitPlayer(player));
        }

        return players;
    }

    @Override
    public void fillRoad(PlotId id1, PlotId id2, IWorld world) {
        generatorManager.fillRoad(id1, id2, ((BukkitWorld) world).getWorld());
    }

    @Override
    public void fillMiddleRoad(PlotId id1, PlotId id2, IWorld world) {
        generatorManager.fillMiddleRoad(id1, id2, ((BukkitWorld) world).getWorld());
    }

    @Override
    public void setOwnerDisplay(IWorld world, PlotId id, String line1, String line2, String line3, String line4) {
        generatorManager.setOwnerDisplay(((BukkitWorld) world).getWorld(), id, line1, line2, line3, line4);
    }

    @Override
    public void setSellerDisplay(IWorld world, PlotId id, String line1, String line2, String line3, String line4) {
        generatorManager.setSellerDisplay(((BukkitWorld) world).getWorld(), id, line1, line2, line3, line4);
    }


    @Override
    public void removeOwnerDisplay(IWorld world, PlotId id) {
        generatorManager.removeOwnerDisplay(((BukkitWorld) world).getWorld(), id);
    }

    @Override
    public void removeSellerDisplay(IWorld world, PlotId id) {
        generatorManager.removeSellerDisplay(((BukkitWorld) world).getWorld(), id);
    }

    @Override
    public ILocation getPlotBottomLoc(IWorld world, PlotId id) {
        return new BukkitLocation(generatorManager.getPlotBottomLoc(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public ILocation getPlotTopLoc(IWorld world, PlotId id) {
        return new BukkitLocation(generatorManager.getPlotTopLoc(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public void refreshPlotChunks(IWorld world, PlotId id) {
        generatorManager.refreshPlotChunks(((BukkitWorld) world).getWorld(), id);
    }

    @Override
    public ILocation getTop(IWorld world, PlotId id) {
        return new BukkitLocation(generatorManager.getTop(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public ILocation getBottom(IWorld world, PlotId id) {
        return new BukkitLocation(generatorManager.getBottom(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public void clear(ILocation bottom, ILocation top) {
        generatorManager.clear(((BukkitLocation) bottom).getLocation(), ((BukkitLocation) top).getLocation());
    }

    @Override
    public Long[] clear(IWorld world, PlotId id, long maxBlocks, Long[] start) {
        return generatorManager.clear(((BukkitWorld) world).getWorld(), id, maxBlocks, start);
    }

    @Deprecated
    @Override
    public void adjustPlotFor(IWorld world, PlotId id, boolean claimed, boolean protect, boolean auctioned, boolean forSale) {
        adjustPlotFor(world, id, claimed, protect, forSale);
    }

    @Override
    public void adjustPlotFor(IWorld world, PlotId id, boolean claimed, boolean protect, boolean forSale) {
        generatorManager.adjustPlotFor(((BukkitWorld) world).getWorld(), id, claimed, protect, forSale);
    }

    @Override
    public boolean isBlockInPlot(PlotId id, ILocation blockLocation) {
        return generatorManager.isBlockInPlot(id, ((BukkitLocation) blockLocation).getLocation());
    }

    @Override
    public boolean movePlot(IWorld world, PlotId idFrom, PlotId idTo) {
        return generatorManager.movePlot(((BukkitWorld) world).getWorld(), idFrom, idTo);
    }

    @Override
    public int bottomX(PlotId id, IWorld world) {
        return generatorManager.bottomX(id, ((BukkitWorld) world).getWorld());
    }

    @Override
    public int bottomZ(PlotId id, IWorld world) {
        return generatorManager.bottomZ(id, ((BukkitWorld) world).getWorld());
    }

    @Override
    public int topX(PlotId id, IWorld world) {
        return generatorManager.topX(id, ((BukkitWorld) world).getWorld());
    }

    @Override
    public int topZ(PlotId id, IWorld world) {
        return generatorManager.topZ(id, ((BukkitWorld) world).getWorld());
    }

    @Override
    public ILocation getPlotHome(IWorld world, PlotId id) {
        return new BukkitLocation(generatorManager.getPlotHome(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public int getPlotSize() {
        return generatorManager.getPlotSize();
    }

    @Override
    public int getRoadHeight() {
        return generatorManager.getRoadHeight();
    }

    @Override
    public ILocation getPlotMiddle(IWorld world, PlotId id) {
        return new BukkitLocation(generatorManager.getPlotMiddle(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public Schematic getPlotSchematic(IWorld world, PlotId id) {
        return generatorManager.getPlotSchematic(((BukkitWorld) world).getWorld(), id);
    }
}
