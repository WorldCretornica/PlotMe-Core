package com.worldcretornica.plotme_core.bukkit.worldedit;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.RegionMask;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.PlotWorldEdit;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.Location;

public class PlotWorldEdit6_0_0 implements PlotWorldEdit {

    private final PlotMe_Core plugin;
    private final WorldEditPlugin we;

    public PlotWorldEdit6_0_0(PlotMe_Core instance, WorldEditPlugin wep) {
        plugin = instance;
        we = wep;
    }

    @Override
    public void setMask(IPlayer player) {
        String id = PlotMeCoreManager.getPlotId(player);
        setMask(player, id);
    }

    @Override
    public void setMask(IPlayer player, String id) {
        BukkitWorld w = (BukkitWorld) player.getWorld();
        BukkitPlayer bp = (BukkitPlayer) player;

        BukkitLocation bottom;
        BukkitLocation top;

        LocalSession session = we.getSession(bp.getPlayer());

        if (!"".equalsIgnoreCase(id)) {
            Plot plot = plugin.getPlotMeCoreManager().getPlotById(player, id);

            if (plot != null && plot.isAllowed(player.getUniqueId())) {
                bottom = (BukkitLocation) PlotMeCoreManager.getPlotBottomLoc(w, id);
                top = (BukkitLocation) PlotMeCoreManager.getPlotTopLoc(w, id);

                LocalSession localsession = we.getSession(bp.getPlayer());
                World world = localsession.getSelectionWorld();

                Vector pos1 = new Vector(bottom.getBlockX(), bottom.getBlockY(), bottom.getBlockZ());
                Vector pos2 = new Vector(top.getBlockX(), top.getBlockY(), top.getBlockZ());

                CuboidRegion cr = new CuboidRegion(world, pos1, pos2);

                RegionMask rm = new RegionMask(cr);

                session.setMask(rm);
                return;
            }
        }

        bottom = new BukkitLocation(new Location(w.getWorld(), 0, 0, 0));
        top = new BukkitLocation(new Location(w.getWorld(), 0, 0, 0));

        if (session.getMask() == null) {
            LocalSession localsession = we.getSession(bp.getPlayer());
            World world = localsession.getSelectionWorld();

            Vector pos1 = new Vector(bottom.getBlockX(), bottom.getBlockY(), bottom.getBlockZ());
            Vector pos2 = new Vector(top.getBlockX(), top.getBlockY(), top.getBlockZ());

            CuboidRegion cr = new CuboidRegion(world, pos1, pos2);

            RegionMask rm = new RegionMask(cr);

            session.setMask(rm);
        }
    }

    @Override
    public void removeMask(IPlayer player) {
        BukkitPlayer bp = (BukkitPlayer) player;
        LocalSession session = we.getSession(bp.getPlayer());
        session.setMask((Mask) null);
    }
}
