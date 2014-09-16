package com.worldcretornica.plotme_core.worldedit;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PlotWorldEdit {

    public void setMask(Player p);

    public void setMask(Player p, Location l);

    public void setMask(Player p, String id);

    public void removeMask(Player p);
}
