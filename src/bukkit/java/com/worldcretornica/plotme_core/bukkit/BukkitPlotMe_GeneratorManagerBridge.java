package com.worldcretornica.plotme_core.bukkit;

import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.bukkit.api.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BukkitPlotMe_GeneratorManagerBridge implements IPlotMe_GeneratorManager {

    private final IBukkitPlotMe_GeneratorManager generatorManager;

    public BukkitPlotMe_GeneratorManagerBridge(IBukkitPlotMe_GeneratorManager generatorManager) {
        this.generatorManager = generatorManager;
    }

    @Override
    public String getPlotId(ILocation location) {
        return generatorManager.getPlotId(((BukkitLocation) location).getLocation());
    }

    @Override
    public String getPlotId(IPlayer player) {
        return generatorManager.getPlotId(((BukkitPlayer) player).getPlayer());
    }

    @Override
    public List<IPlayer> getPlayersInPlot(String id) {
        List<IPlayer> players = new ArrayList<>();

        for (Player player : generatorManager.getPlayersInPlot(id)) {
            players.add(new BukkitPlayer(player));
        }

        return players;
    }

    @Override
    public void fillroad(String id1, String id2, IWorld world) {
        generatorManager.fillroad(id1, id2, ((BukkitWorld) world).getWorld());
    }

    @Override
    public void fillmiddleroad(String id1, String id2, IWorld world) {
        generatorManager.fillmiddleroad(id1, id2, ((BukkitWorld) world).getWorld());
    }

    @Override
    public void setOwnerDisplay(IWorld world, String id, String line1, String line2, String line3, String line4) {
        generatorManager.setOwnerDisplay(((BukkitWorld) world).getWorld(), id, line1, line2, line3, line4);
    }

    @Override
    public void setSellerDisplay(IWorld world, String id, String line1, String line2, String line3, String line4) {
        generatorManager.setSellerDisplay(((BukkitWorld) world).getWorld(), id, line1, line2, line3, line4);
    }

    @Override
    public void setAuctionDisplay(IWorld world, String id, String line1, String line2, String line3, String line4) {
        generatorManager.setAuctionDisplay(((BukkitWorld) world).getWorld(), id, line1, line2, line3, line4);
    }

    @Override
    public void removeOwnerDisplay(IWorld world, String id) {
        generatorManager.removeOwnerDisplay(((BukkitWorld) world).getWorld(), id);
    }

    @Override
    public void removeSellerDisplay(IWorld world, String id) {
        generatorManager.removeSellerDisplay(((BukkitWorld) world).getWorld(), id);
    }

    @Override
    public void removeAuctionDisplay(IWorld world, String id) {
        generatorManager.removeAuctionDisplay(((BukkitWorld) world).getWorld(), id);
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
        return new BukkitLocation(generatorManager.getPlotBottomLoc(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public ILocation getPlotTopLoc(IWorld world, String id) {
        return new BukkitLocation(generatorManager.getPlotTopLoc(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public void setBiome(IWorld world, String id, IBiome biome) {
        generatorManager.setBiome(((BukkitWorld) world).getWorld(), id, ((BukkitBiome) biome).getBiome());
    }

    @Override
    public void refreshPlotChunks(IWorld world, String id) {
        generatorManager.refreshPlotChunks(((BukkitWorld) world).getWorld(), id);
    }

    @Override
    public ILocation getTop(IWorld world, String id) {
        return new BukkitLocation(generatorManager.getTop(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public ILocation getBottom(IWorld world, String id) {
        return new BukkitLocation(generatorManager.getBottom(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public void clear(ILocation bottom, ILocation top) {
        generatorManager.clear(((BukkitLocation) bottom).getLocation(), ((BukkitLocation) top).getLocation());
    }

    @Override
    public Long[] clear(ILocation bottom, ILocation top, long maxBlocks, Long[] start) {
        return generatorManager.clear(((BukkitLocation) bottom).getLocation(), ((BukkitLocation) top).getLocation(), maxBlocks, start);
    }

    @Override
    public Long[] clear(IWorld world, String id, long maxBlocks, Long[] start) {
        return generatorManager.clear(((BukkitWorld) world).getWorld(), id, maxBlocks, start);
    }

    @Override
    public void adjustPlotFor(IWorld world, String id, boolean claimed, boolean protect, boolean auctionned, boolean forSale) {
        generatorManager.adjustPlotFor(((BukkitWorld) world).getWorld(), id, claimed, protect, auctionned, forSale);
    }

    @Override
    public boolean isBlockInPlot(String id, ILocation location) {
        return generatorManager.isBlockInPlot(id, ((BukkitLocation) location).getLocation());
    }

    @Override
    public boolean movePlot(IWorld world, String idFrom, String idTo) {
        return generatorManager.movePlot(((BukkitWorld) world).getWorld(), idFrom, idTo);
    }

    @Override
    public int bottomX(String id, IWorld world) {
        return generatorManager.bottomX(id, ((BukkitWorld) world).getWorld());
    }

    @Override
    public int bottomZ(String id, IWorld world) {
        return generatorManager.bottomZ(id, ((BukkitWorld) world).getWorld());
    }

    @Override
    public int topX(String id, IWorld world) {
        return generatorManager.topX(id, ((BukkitWorld) world).getWorld());
    }

    @Override
    public int topZ(String id, IWorld world) {
        return generatorManager.topZ(id, ((BukkitWorld) world).getWorld());
    }

    @Override
    public ILocation getPlotHome(IWorld world, String id) {
        return new BukkitLocation(generatorManager.getPlotHome(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public boolean isValidId(String id) {
        return generatorManager.isValidId(id);
    }

    @Override
    public boolean createConfig(String worldname, Map<String, String> args) {
        return generatorManager.createConfig(worldname, args);
    }

    @Override
    public Map<String, String> getDefaultGenerationConfig() {
        return generatorManager.getDefaultGenerationConfig();
    }

    @Override
    public int getPlotSize(String worldname) {
        return generatorManager.getPlotSize(worldname);
    }

    @Override
    public int getRoadHeight(String worldname) {
        return generatorManager.getRoadHeight(worldname);
    }

}
