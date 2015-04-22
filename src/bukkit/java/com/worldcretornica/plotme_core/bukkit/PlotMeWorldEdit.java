package com.worldcretornica.plotme_core.bukkit;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;

public class PlotMeWorldEdit extends AbstractDelegateExtent {

    private final Extent extent;
    private final Actor actor;
    private final PlotMeCoreManager manager = PlotMeCoreManager.getInstance();
    private final IPlayer player;

    public PlotMeWorldEdit(PlotMe_CorePlugin plugin, Extent extent, Actor actor) {
        super(extent);
        this.extent = extent;
        this.actor = actor;
        player = plugin.wrapPlayer(((BukkitPlayer) actor).getPlayer());


    }

    @Override
    public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
        if (manager.isPlayerIgnoringWELimit(player)) {
            return extent.setBlock(location, block);
        } else {
            ILocation loc = new ILocation(player.getWorld(), location.getX(), location.getY(), location.getZ());
            Plot plot = manager.getPlot(loc);
            return plot != null && plot.isAllowed(actor.getUniqueId()) && extent.setBlock(location, block);
        }
    }

    @Override
    public boolean setBiome(Vector2D position, BaseBiome biome) {
        if (manager.isPlayerIgnoringWELimit(player)) {
            return extent.setBiome(position, biome);
        } else {
            ILocation loc = new ILocation(player.getWorld(), position.getX(), 0, position.getZ());
            Plot plot = manager.getPlot(loc);
            return plot != null && plot.isAllowed(actor.getUniqueId()) && extent.setBiome(position, biome);
        }
    }
}
