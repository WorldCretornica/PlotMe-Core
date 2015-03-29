package com.worldcretornica.plotme_core.bukkit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.util.eventbus.EventHandler;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.IPlayer;

/**
 * Created by Matthew on 3/25/2015.
 */
public class PlotMeWorldEdit {

    private final PlotMe_CorePlugin plugin;
    private final PlotMeCoreManager manager = PlotMeCoreManager.getInstance();

    public PlotMeWorldEdit(PlotMe_CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe(priority = EventHandler.Priority.EARLY)
    public void wrapToDestroyEverything(EditSessionEvent event) {
        if (event.getStage().equals(EditSession.Stage.BEFORE_CHANGE) || event.getStage().equals(EditSession.Stage.BEFORE_HISTORY)) {
            if (!(event.getActor() instanceof BukkitPlayer)) {
                return;
            }
            final BukkitPlayer player = (BukkitPlayer) event.getActor();

            event.setExtent(new AbstractDelegateExtent(event.getExtent()) {
                @Override
                public boolean setBlock(Vector location, BaseBlock block) {
                    IPlayer iPlayer = plugin.wrapPlayer(player.getPlayer());
                    PlotId id = manager.getPlotId(iPlayer);
                    if (id == null && iPlayer.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {

                    }
                    throw new RuntimeException("No block for you");
                }
            });
        }
    }
}
