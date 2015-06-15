package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IWorldBorder;
import org.bukkit.WorldBorder;

public class BukkitWorldBorder implements IWorldBorder {

    private final WorldBorder border;

    public BukkitWorldBorder(WorldBorder border) {
        this.border = border;
    }

    public double minX() {
        return this.border.getCenter().getX() - (this.border.getSize() / 2.0D);
    }

    public double minZ() {
        return this.border.getCenter().getZ() - this.border.getSize() / 2.0D;
    }

    public double maxX() {
        return this.border.getCenter().getX() + this.border.getSize() / 2.0D;
    }

    public double maxZ() {
        return this.border.getCenter().getZ() + this.border.getSize() / 2.0D;
    }

}
