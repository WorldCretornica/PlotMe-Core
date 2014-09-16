package com.worldcretornica.plotme_core.worldedit;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.RegionMask;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;

public class PlotWorldEdit6_0_0 implements PlotWorldEdit {

    private final PlotMe_Core plugin;
    private final WorldEditPlugin we;

    public PlotWorldEdit6_0_0(PlotMe_Core instance, WorldEditPlugin wep) {
        this.plugin = instance;
        this.we = wep;
    }

    public void setMask(Player p) {
        setMask(p, p.getLocation());
    }

    public void setMask(Player p, Location l) {
        String id = plugin.getPlotMeCoreManager().getPlotId(l);
        setMask(p, id);
    }

    public void setMask(Player p, String id) {
        World w = p.getWorld();

        Location bottom = null;
        Location top = null;

        LocalSession session = we.getSession(p);

        if (!id.equalsIgnoreCase("")) {
            Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

            if (plot != null && plot.isAllowed(p.getUniqueId())) {
                bottom = plugin.getPlotMeCoreManager().getPlotBottomLoc(w, id);
                top = plugin.getPlotMeCoreManager().getPlotTopLoc(w, id);

                LocalSession localsession = we.getSession(p);
                com.sk89q.worldedit.world.World world = localsession.getSelectionWorld();

                Vector pos1 = new Vector(bottom.getBlockX(), bottom.getBlockY(), bottom.getBlockZ());
                Vector pos2 = new Vector(top.getBlockX(), top.getBlockY(), top.getBlockZ());

                CuboidRegion cr = new CuboidRegion(world, pos1, pos2);

                RegionMask rm = new RegionMask(cr);

                session.setMask(rm);
                return;
            }
        }

        if (bottom == null || top == null) {
            bottom = new Location(w, 0, 0, 0);
            top = new Location(w, 0, 0, 0);
        }

        if (session.getMask() == null) {
            LocalSession localsession = we.getSession(p);
            com.sk89q.worldedit.world.World world = localsession.getSelectionWorld();

            Vector pos1 = new Vector(bottom.getBlockX(), bottom.getBlockY(), bottom.getBlockZ());
            Vector pos2 = new Vector(top.getBlockX(), top.getBlockY(), top.getBlockZ());

            CuboidRegion cr = new CuboidRegion(world, pos1, pos2);

            RegionMask rm = new RegionMask(cr);

            session.setMask(rm);
        }
    }

    public void removeMask(Player p) {
        LocalSession session = we.getSession(p);
        Mask mask = null;
        session.setMask(mask);
    }
}
