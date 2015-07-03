package com.worldcretornica.plotme_core.bukkit;

import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.util.eventbus.EventHandler;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;

public class PlotWorldEditListener {

    private final PlotMe_Core api;

    public PlotWorldEditListener(PlotMe_Core api) {
        this.api = api;
    }

    @Subscribe(priority = EventHandler.Priority.VERY_EARLY)
    public void worldEditListener(EditSessionEvent event) {
        Actor actor = event.getActor();
        if (event.getWorld() == null) {
            return;
        }
        World world1 = PlotMe_CorePlugin.getInstance().getServer().getWorld(event.getWorld().getName());
        BukkitWorld adapt = BukkitUtil.adapt(world1);
        if (PlotMeCoreManager.getInstance().isPlotWorld(adapt)) {
            if (actor != null && actor.isPlayer()) {
                event.setExtent(new PlotMeWorldEdit(api, event.getExtent(), event.getActor()));
            }
        }
    }
}