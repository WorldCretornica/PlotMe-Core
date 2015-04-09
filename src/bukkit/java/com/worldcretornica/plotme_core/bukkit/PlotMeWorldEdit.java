package com.worldcretornica.plotme_core.bukkit;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.world.World;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;

public class PlotMeWorldEdit extends AbstractDelegateExtent {

    private final PlotMe_CorePlugin plugin;
    private final Extent extent;
    private final World world;
    private final Actor actor;
    private final WorldEdit worldEdit = WorldEdit.getInstance();
    private final PlotMeCoreManager manager = PlotMeCoreManager.getInstance();
    private final BukkitWorld plotMeWorld;

    public PlotMeWorldEdit(PlotMe_CorePlugin plugin, Extent extent, World world, Actor actor) {
        super(extent);
        this.plugin = plugin;
        this.extent = extent;
        this.world = world;
        this.actor = actor;
        plotMeWorld = new BukkitWorld(((com.sk89q.worldedit.bukkit.BukkitWorld) this.world).getWorld());

    }

    @Override
    public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
        if (world instanceof com.sk89q.worldedit.bukkit.BukkitWorld) {
            if (manager.isPlotWorld(plotMeWorld)) {
                if (manager.isPlayerIgnoringWELimit(plugin.wrapPlayer(((BukkitPlayer) actor).getPlayer()))) {
                    return extent.setBlock(location, block);
                } else {
                    ILocation loc = new ILocation(plotMeWorld, location.getX(), location.getY(), location.getZ());
                    PlotId id = manager.getPlotId(loc);
                    if (id != null) {
                        Plot plot = manager.getPlotById(id);
                        return plot != null && plot.isAllowed(actor.getUniqueId()) && extent.setBlock(location, block);
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
