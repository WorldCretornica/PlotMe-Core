package com.worldcretornica.plotme_core.bukkit;

import com.google.common.base.Optional;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.Location;

class PlotMeWorldEdit extends AbstractDelegateExtent {

    private final PlotMe_Core api;
    private final Extent extent;
    private final Actor actor;
    private final PlotMeCoreManager manager = PlotMeCoreManager.getInstance();
    private final IPlayer player;

    public PlotMeWorldEdit(PlotMe_Core api, Extent extent, Actor actor) {
        super(extent);
        this.api = api;
        this.extent = extent;
        this.actor = actor;
        player = api.getServerBridge().getPlayer(actor.getUniqueId());


    }

    @Override
    public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
        if (manager.isPlayerIgnoringWELimit(player)) {
            return extent.setBlock(location, block);
        } else {
            Location loc = new Location(player.getWorld(), location.getX(), location.getY(), location.getZ());
            Plot plot = manager.getPlot(loc);
            if (plot != null) {
                Optional<Plot.AccessLevel> member = plot.isMember(actor.getUniqueId());
                if (member.isPresent()) {
                    if (member.get().equals(Plot.AccessLevel.TRUSTED) && !api.getServerBridge().getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                        return false;
                    }
                    if (extent.setBlock(location, block)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public boolean setBiome(Vector2D position, BaseBiome biome) {
        if (manager.isPlayerIgnoringWELimit(player)) {
            return extent.setBiome(position, biome);
        } else {
            Location loc = new Location(player.getWorld(), position.getX(), 0, position.getZ());
            Plot plot = manager.getPlot(loc);
            if (plot != null) {
                Optional<Plot.AccessLevel> member = plot.isMember(actor.getUniqueId());
                if (member.isPresent()) {
                    if (member.get().equals(Plot.AccessLevel.TRUSTED) && !api.getServerBridge().getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                        return false;
                    }
                    if (extent.setBiome(position, biome)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
