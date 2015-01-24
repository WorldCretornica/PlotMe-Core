package com.worldcretornica.plotme_core.sponge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.spongepowered.api.entity.player.Player;

import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.sponge.api.*;

public class SpongePlotMe_GeneratorManagerBridge implements IPlotMe_GeneratorManager {

    private final ISpongePlotMe_GeneratorManager generatorManager;
    
    public SpongePlotMe_GeneratorManagerBridge(ISpongePlotMe_GeneratorManager generatorManager) {
        this.generatorManager = generatorManager;
    }

    @Override
    public String getPlotId(ILocation location) {
        return generatorManager.getPlotId(((SpongeLocation) location).getLocation());
    }

    @Override
    public String getPlotId(IPlayer player) {
        return generatorManager.getPlotId(((SpongePlayer) player).getPlayer());
    }

    @Override
    public List<IPlayer> getPlayersInPlot(String id) {
        List<IPlayer> players = new ArrayList<>();
        
        for (Player player : generatorManager.getPlayersInPlot(id)) {
            players.add(new SpongePlayer(player));
        }
        
        return players;
    }

    @Override
    public void fillroad(String id1, String id2, IWorld world) {
        generatorManager.fillroad(id1, id2, ((SpongeWorld) world).getWorld());
    }

    @Override
    public void fillmiddleroad(String id1, String id2, IWorld world) {
        generatorManager.fillmiddleroad(id1, id2, ((SpongeWorld) world).getWorld());
    }

    @Override
    public void setOwnerDisplay(IWorld world, String id, String line1, String line2, String line3, String line4) {
        generatorManager.setOwnerDisplay(((SpongeWorld) world).getWorld(), id, line1, line2, line3, line4);
    }

    @Override
    public void setSellerDisplay(IWorld world, String id, String line1, String line2, String line3, String line4) {
        generatorManager.setSellerDisplay(((SpongeWorld) world).getWorld(), id, line1, line2, line3, line4);
    }

    @Override
    public void setAuctionDisplay(IWorld world, String id, String line1, String line2, String line3, String line4) {
        generatorManager.setAuctionDisplay(((SpongeWorld) world).getWorld(), id, line1, line2, line3, line4);
    }

    @Override
    public void removeOwnerDisplay(IWorld world, String id) {
        generatorManager.removeOwnerDisplay(((SpongeWorld) world).getWorld(), id);
    }

    @Override
    public void removeSellerDisplay(IWorld world, String id) {
        generatorManager.removeSellerDisplay(((SpongeWorld) world).getWorld(), id);
    }

    @Override
    public void removeAuctionDisplay(IWorld world, String id) {
        generatorManager.removeAuctionDisplay(((SpongeWorld) world).getWorld(), id);
    }

    @Override
    public int getIdX(String id) {
        return generatorManager.getIdX(id);
    }

    @Override
    public int getIdZ(String id) {
        return generatorManager.getIdZ(id);
    }

    @Override
    public ILocation getPlotBottomLoc(IWorld world, String id) {
        return new SpongeLocation(generatorManager.getPlotBottomLoc(((SpongeWorld) world).getWorld(), id));
    }

    @Override
    public ILocation getPlotTopLoc(IWorld world, String id) {
        return new SpongeLocation(generatorManager.getPlotTopLoc(((SpongeWorld) world).getWorld(), id));
    }

    @Override
    public void setBiome(IWorld world, String id, IBiome biome) {
        generatorManager.setBiome(((SpongeWorld) world).getWorld(), id, ((SpongeBiomeType) biome).getBiomeType());
    }

    @Override
    public void refreshPlotChunks(IWorld world, String id) {
        generatorManager.refreshPlotChunks(((SpongeWorld) world).getWorld(), id);
    }

    @Override
    public ILocation getTop(IWorld world, String id) {
        return new SpongeLocation(generatorManager.getTop(((SpongeWorld) world).getWorld(), id));
    }

    @Override
    public ILocation getBottom(IWorld world, String id) {
        return new SpongeLocation(generatorManager.getBottom(((SpongeWorld) world).getWorld(), id));
    }

    @Override
    public void clear(ILocation bottom, ILocation top) {
        generatorManager.clear(((SpongeLocation) bottom).getLocation(), ((SpongeLocation) top).getLocation());
    }

    @Override
    public Long[] clear(ILocation bottom, ILocation top, long maxBlocks, Long[] start) {
        return generatorManager.clear(((SpongeLocation) bottom).getLocation(), ((SpongeLocation) top).getLocation(), maxBlocks, start);
    }

    @Override
    public Long[] clear(IWorld world, String id, long maxBlocks, Long[] start) {
        return generatorManager.clear(((SpongeWorld) world).getWorld(), id, maxBlocks, start);
    }

    @Override
    public void adjustPlotFor(IWorld world, String id, boolean claimed, boolean protect, boolean auctioned, boolean forSale) {
        generatorManager.adjustPlotFor(((SpongeWorld) world).getWorld(), id, claimed, protect, auctioned, forSale);
    }

    @Override
    public boolean isBlockInPlot(String id, ILocation blockLocation) {
        return generatorManager.isBlockInPlot(id, ((SpongeLocation) blockLocation).getLocation());
    }

    @Override
    public boolean movePlot(IWorld world, String idFrom, String idTo) {
        return generatorManager.movePlot(((SpongeWorld) world).getWorld(), idFrom, idTo);
    }

    @Override
    public int bottomX(String id, IWorld world) {
        return generatorManager.bottomX(id, ((SpongeWorld) world).getWorld());
    }

    @Override
    public int bottomZ(String id, IWorld world) {
        return generatorManager.bottomZ(id, ((SpongeWorld) world).getWorld());
    }

    @Override
    public int topX(String id, IWorld world) {
        return generatorManager.topX(id, ((SpongeWorld) world).getWorld());
    }

    @Override
    public int topZ(String id, IWorld world) {
        return generatorManager.topZ(id, ((SpongeWorld) world).getWorld());
    }

    @Override
    public ILocation getPlotHome(IWorld world, String id) {
        return new SpongeLocation(generatorManager.getPlotHome(((SpongeWorld) world).getWorld(), id));
    }

    @Override
    public boolean isValidId(String id) {
        return generatorManager.isValidId(id);
    }

    @Override
    public boolean createConfig(String worldName, Map<String, String> args) {
        return generatorManager.createConfig(worldName, args);
    }

    @Override
    public Map<String, String> getDefaultGenerationConfig() {
        return generatorManager.getDefaultGenerationConfig();
    }

    @Override
    public int getPlotSize(String worldName) {
        return generatorManager.getPlotSize(worldName);
    }

    @Override
    public int getRoadHeight(String worldName) {
        return generatorManager.getRoadHeight(worldName);
    }
    
    
}
