package com.worldcretornica.plotme_core.sponge;

import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.sponge.api.ISpongePlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.sponge.api.SpongeBiomeType;
import com.worldcretornica.plotme_core.sponge.api.SpongeLocation;
import com.worldcretornica.plotme_core.sponge.api.SpongePlayer;
import com.worldcretornica.plotme_core.sponge.api.SpongeWorld;
import com.worldcretornica.schematic.Schematic;
import org.spongepowered.api.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpongePlotMe_GeneratorManagerBridge implements IPlotMe_GeneratorManager {

    private final ISpongePlotMe_GeneratorManager generatorManager;

    public SpongePlotMe_GeneratorManagerBridge(ISpongePlotMe_GeneratorManager generatorManager) {
        this.generatorManager = generatorManager;
    }

    @Override
    public PlotId getPlotId(ILocation location) {
        return generatorManager.getPlotId(((SpongeLocation) location).getLocation());
    }

    @Override
    public PlotId getPlotId(IPlayer player) {
        return generatorManager.getPlotId(((SpongePlayer) player).getPlayer());
    }

    @Override
    public List<IPlayer> getPlayersInPlot(PlotId id) {
        List<IPlayer> players = new ArrayList<>();

        for (Player player : generatorManager.getPlayersInPlot(id)) {
            players.add(new SpongePlayer(player));
        }

        return players;
    }

    @Override
    public void fillRoad(PlotId id1, PlotId id2, IWorld world) {
        generatorManager.fillroad(id1, id2, ((SpongeWorld) world).getWorld());
    }

    @Override
    public void fillMiddleRoad(PlotId id1, PlotId id2, IWorld world) {
        generatorManager.fillmiddleroad(id1, id2, ((SpongeWorld) world).getWorld());
    }

    @Override
    public void setOwnerDisplay(IWorld world, PlotId id, String line1, String line2, String line3, String line4) {
        generatorManager.setOwnerDisplay(((SpongeWorld) world).getWorld(), id, line1, line2, line3, line4);
    }

    @Override
    public void setSellerDisplay(IWorld world, PlotId id, String line1, String line2, String line3, String line4) {
        generatorManager.setSellerDisplay(((SpongeWorld) world).getWorld(), id, line1, line2, line3, line4);
    }

    @Override
    public void removeOwnerDisplay(IWorld world, PlotId id) {
        generatorManager.removeOwnerDisplay(((SpongeWorld) world).getWorld(), id);
    }

    @Override
    public void removeSellerDisplay(IWorld world, PlotId id) {
        generatorManager.removeSellerDisplay(((SpongeWorld) world).getWorld(), id);
    }

    @Override
    public void removeAuctionDisplay(IWorld world, PlotId id) {
        generatorManager.removeAuctionDisplay(((SpongeWorld) world).getWorld(), id);
    }

    @Override
    public ILocation getPlotBottomLoc(IWorld world, PlotId id) {
        return new SpongeLocation(generatorManager.getPlotBottomLoc(((SpongeWorld) world).getWorld(), id));
    }

    @Override
    public ILocation getPlotTopLoc(IWorld world, PlotId id) {
        return new SpongeLocation(generatorManager.getPlotTopLoc(((SpongeWorld) world).getWorld(), id));
    }

    @Override
    public void setBiome(IWorld world, PlotId id, IBiome biome) {
        generatorManager.setBiome(((SpongeWorld) world).getWorld(), id, ((SpongeBiomeType) biome).getBiomeType());
    }

    @Override
    public void refreshPlotChunks(IWorld world, PlotId id) {
        generatorManager.refreshPlotChunks(((SpongeWorld) world).getWorld(), id);
    }

    @Override
    public ILocation getTop(IWorld world, PlotId id) {
        return new SpongeLocation(generatorManager.getTop(((SpongeWorld) world).getWorld(), id));
    }

    @Override
    public ILocation getBottom(IWorld world, PlotId id) {
        return new SpongeLocation(generatorManager.getBottom(((SpongeWorld) world).getWorld(), id));
    }

    @Override
    public void clear(ILocation bottom, ILocation top) {
        generatorManager.clear(((SpongeLocation) bottom).getLocation(), ((SpongeLocation) top).getLocation());
    }

    @Override
    public Long[] clear(IWorld world, PlotId id, long maxBlocks, Long[] start) {
        return generatorManager.clear(((SpongeWorld) world).getWorld(), id, maxBlocks, start);
    }

    @Override
    public void adjustPlotFor(IWorld world, PlotId id, boolean claimed, boolean protect, boolean auctioned, boolean forSale) {
        generatorManager.adjustPlotFor(((SpongeWorld) world).getWorld(), id, claimed, protect, forSale);
    }

    @Override
    public void adjustPlotFor(IWorld world, PlotId id, boolean claimed, boolean protect, boolean forSale) {
        generatorManager.adjustPlotFor(((SpongeWorld) world).getWorld(), id, claimed, protect, forSale);
    }

    @Override
    public boolean isBlockInPlot(PlotId id, ILocation blockLocation) {
        return generatorManager.isBlockInPlot(id, ((SpongeLocation) blockLocation).getLocation());
    }

    @Override
    public boolean movePlot(IWorld world, PlotId idFrom, PlotId idTo) {
        return generatorManager.movePlot(((SpongeWorld) world).getWorld(), idFrom, idTo);
    }

    @Override
    public int bottomX(PlotId id, IWorld world) {
        return generatorManager.bottomX(id, ((SpongeWorld) world).getWorld());
    }

    @Override
    public int bottomZ(PlotId id, IWorld world) {
        return generatorManager.bottomZ(id, ((SpongeWorld) world).getWorld());
    }

    @Override
    public int topX(PlotId id, IWorld world) {
        return generatorManager.topX(id, ((SpongeWorld) world).getWorld());
    }

    @Override
    public int topZ(PlotId id, IWorld world) {
        return generatorManager.topZ(id, ((SpongeWorld) world).getWorld());
    }

    @Override
    public ILocation getPlotHome(IWorld world, PlotId id) {
        return new SpongeLocation(generatorManager.getPlotHome(((SpongeWorld) world).getWorld(), id));
    }

    @Override
    public boolean createConfig(String worldName, Map<String, String> args) {
        return generatorManager.createConfig(worldName, args);
    }

    @Override
    public int getPlotSize(String worldName) {
        return generatorManager.getPlotSize(worldName);
    }

    @Override
    public int getRoadHeight(String worldName) {
        return generatorManager.getRoadHeight(worldName);
    }

    @Override
    public ILocation getPlotMiddle(IWorld world, PlotId id) {
        return new SpongeLocation(generatorManager.getPlotMiddle(((SpongeWorld) world).getWorld(), id));
    }

    @Override
    public Schematic getPlotSchematic(IWorld world, PlotId id) {
        return generatorManager.getPlotSchematic(((SpongeWorld) world).getWorld(), id);
    }
}
