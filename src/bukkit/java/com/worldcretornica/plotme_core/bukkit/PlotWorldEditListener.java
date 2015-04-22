package com.worldcretornica.plotme_core.bukkit;

import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;

public class PlotWorldEditListener {

    private final PlotMe_CorePlugin plugin;

    public PlotWorldEditListener(PlotMe_CorePlugin plugin) {

        this.plugin = plugin;
    }

    @Subscribe
    public void worldEditListener(EditSessionEvent event) {
        Actor actor = event.getActor();
        if (event.getWorld() == null) {
            return;
        }
        @SuppressWarnings("null")
        BukkitWorld plotmeWorld = new BukkitWorld(((com.sk89q.worldedit.bukkit.BukkitWorld) event.getWorld()).getWorld());
        if (PlotMeCoreManager.getInstance().isPlotWorld(plotmeWorld)) {
            if (actor != null && actor.isPlayer()) {
                event.setExtent(new PlotMeWorldEdit(plugin, event.getExtent(), event.getActor()));
            }
        }
    }
}