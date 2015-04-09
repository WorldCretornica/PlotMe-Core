package com.worldcretornica.plotme_core.bukkit.listener;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import com.worldcretornica.plotme_core.bukkit.PlotMeWorldEdit;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;

public class PlotWorldEditListener {

    private final WorldEdit worldEdit = WorldEdit.getInstance();
    private final PlotMe_CorePlugin plugin;

    public PlotWorldEditListener(PlotMe_CorePlugin plugin) {

        this.plugin = plugin;
    }

    @Subscribe
    public void worldEditListener(EditSessionEvent event) {
        Actor actor = event.getActor();
        if (actor != null && actor.isPlayer()) {
            event.setExtent(new PlotMeWorldEdit(plugin, event.getExtent(), event.getWorld(), event.getActor()));
        }
    }
}