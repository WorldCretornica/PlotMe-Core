package com.worldcretornica.plotme_core;


import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.RegionMask;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.bukkit.api.*;
import org.bukkit.*;

public class PlotWorldEdit {

    private final WorldEditPlugin worldEdit;

    public PlotWorldEdit(WorldEditPlugin worldEditPlugin) {
        this.worldEdit = worldEditPlugin;
    }

    public void setMask(IPlayer player) {
        String id = PlotMeCoreManager.getInstance().getPlotId(player);
        setMask(player, id);
    }

    public void setMask(IPlayer player, String id) {
        BukkitWorld bukkitWorld = (BukkitWorld) player.getWorld();
        BukkitPlayer bukkitPlayer = (BukkitPlayer) player;

        BukkitLocation bottom;
        BukkitLocation top;

        LocalSession session = worldEdit.getSession(bukkitPlayer.getPlayer());

        if (!id.isEmpty()) {
            PlotMeCoreManager manager = PlotMeCoreManager.getInstance();
            Plot plot = manager.getPlotById(id, player);

            if (plot != null && plot.isAllowed(player.getName(), player.getUniqueId())) {
                bottom = (BukkitLocation) manager.getPlotBottomLoc(bukkitWorld, id);
                top = (BukkitLocation) manager.getPlotTopLoc(bukkitWorld, id);

                LocalSession localsession = worldEdit.getSession(bukkitPlayer.getPlayer());
                World world = localsession.getSelectionWorld();

                Vector pos1 = new Vector(bottom.getBlockX(), bottom.getBlockY(), bottom.getBlockZ());
                Vector pos2 = new Vector(top.getBlockX(), top.getBlockY(), top.getBlockZ());

                CuboidRegion cr = new CuboidRegion(world, pos1, pos2);

                Mask regionMask = new RegionMask(cr);

                session.setMask(regionMask);
                return;
            }
        }

        bottom = new BukkitLocation(new Location(bukkitWorld.getWorld(), 0, 0, 0));
        top = new BukkitLocation(new Location(bukkitWorld.getWorld(), 0, 0, 0));

        if (session.getMask() == null) {
            LocalSession localsession = worldEdit.getSession(bukkitPlayer.getPlayer());
            World world = localsession.getSelectionWorld();

            Vector pos1 = new Vector(bottom.getBlockX(), bottom.getBlockY(), bottom.getBlockZ());
            Vector pos2 = new Vector(top.getBlockX(), top.getBlockY(), top.getBlockZ());

            CuboidRegion cr = new CuboidRegion(world, pos1, pos2);

            Mask rm = new RegionMask(cr);

            session.setMask(rm);
        }
    }

    public void removeMask(IPlayer player) {
        BukkitPlayer bp = (BukkitPlayer) player;
        LocalSession session = worldEdit.getSession(bp.getPlayer());
        session.setMask((Mask) null);
    }

}
