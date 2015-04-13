package com.worldcretornica.plotme_core.bukkit;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;

public class PlotMeWorldEdit extends AbstractDelegateExtent {

    private final PlotMe_CorePlugin plugin;
    private final Extent extent;
    private final Actor actor;
    private final PlotMeCoreManager manager = PlotMeCoreManager.getInstance();

    public PlotMeWorldEdit(PlotMe_CorePlugin plugin, Extent extent, Actor actor) {
        super(extent);
        this.plugin = plugin;
        this.extent = extent;
        this.actor = actor;

    }

    @Override
    public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
        IPlayer player = plugin.wrapPlayer(((BukkitPlayer) actor).getPlayer());
        if (manager.isPlayerIgnoringWELimit(player)) {
            return extent.setBlock(location, block);
        } else {
            ILocation loc = new ILocation(player.getWorld(), location.getX(), location.getY(), location.getZ());
            PlotId id = manager.getPlotId(loc);
            if (id != null) {
                Plot plot = manager.getPlotById(id);
                if (plot != null && plot.isAllowed(actor.getUniqueId())) {
                    return extent.setBlock(location, block);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
}
