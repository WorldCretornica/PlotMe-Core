package com.worldcretornica.plotme_core.bukkit.worldedit;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.RegionMask;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
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
        this.plugin = instance;
        this.we = wep;
    }

    public void setMask(IPlayer p) {
        setMask(p, p.getLocation());
    }

    public void setMask(IPlayer p, ILocation l) {
        String id = plugin.getPlotMeCoreManager().getPlotId(l);
        setMask(p, id);
    }

    public void setMask(IPlayer p, String id) {
        BukkitWorld w = (BukkitWorld) p.getWorld();
        BukkitPlayer bp = (BukkitPlayer) p;

        BukkitLocation bottom = null;
        BukkitLocation top = null;

        LocalSession session = we.getSession(bp.getPlayer());

        if (!id.equalsIgnoreCase("")) {
            Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

            if (plot != null && plot.isAllowed(p.getUniqueId())) {
                bottom = (BukkitLocation) plugin.getPlotMeCoreManager().getPlotBottomLoc(w, id);
                top = (BukkitLocation) plugin.getPlotMeCoreManager().getPlotTopLoc(w, id);

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

        if (bottom == null || top == null) {
            bottom = new BukkitLocation(new Location(w.getWorld(), 0, 0, 0));
            top = new BukkitLocation(new Location(w.getWorld(), 0, 0, 0));
        }

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

    public void removeMask(IPlayer p) {
        BukkitPlayer bp = (BukkitPlayer) p;
        LocalSession session = we.getSession(bp.getPlayer());
        session.setMask((Mask) null);
    }
}
