package com.worldcretornica.plotme_core;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.masks.RegionMask;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlotWorldEdit {

    private final PlotMe_Core plugin;
    private final WorldEditPlugin we;

    public PlotWorldEdit(PlotMe_Core instance, WorldEditPlugin wep) {
        plugin = instance;
        we = wep;
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

        if (!id.equals("")) {
            PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(w.getName(), id);

            if (ptc == null) {
                Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                if (plot != null && plot.isAllowed(p.getName())) {
                    bottom = plugin.getPlotMeCoreManager().getPlotBottomLoc(w, id);
                    top = plugin.getPlotMeCoreManager().getPlotTopLoc(w, id);

                    BukkitPlayer player = we.wrapPlayer(p);
                    LocalWorld world = player.getWorld();

                    Vector pos1 = new Vector(bottom.getBlockX(), bottom.getBlockY(), bottom.getBlockZ());
                    Vector pos2 = new Vector(top.getBlockX(), top.getBlockY(), top.getBlockZ());

                    CuboidRegion cr = new CuboidRegion(world, pos1, pos2);

                    RegionMask rm = new RegionMask(cr);

                    session.setMask(rm);
                    return;
                }
            }
        }

        if (bottom == null || top == null) {
            bottom = new Location(w, 0, 0, 0);
            top = new Location(w, 0, 0, 0);
        }

        if (session.getMask() == null) {
            BukkitPlayer player = we.wrapPlayer(p);
            LocalWorld world = player.getWorld();

            Vector pos1 = new Vector(bottom.getBlockX(), bottom.getBlockY(), bottom.getBlockZ());
            Vector pos2 = new Vector(top.getBlockX(), top.getBlockY(), top.getBlockZ());

            CuboidRegion cr = new CuboidRegion(world, pos1, pos2);

            RegionMask rm = new RegionMask(cr);

            session.setMask(rm);
        }
    }

    public void removeMask(Player p) {
        LocalSession session = we.getSession(p);
        session.setMask((Mask) null);
    }	}
