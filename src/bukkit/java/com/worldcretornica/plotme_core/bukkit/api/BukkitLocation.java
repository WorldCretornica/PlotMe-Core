package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.Location;

import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;

public class BukkitLocation implements ILocation {

    Location location;
    
    public BukkitLocation(Location loc) {
        this.location = loc;
    }

    @Override
    public IWorld getWorld() {
        return new BukkitWorld(location.getWorld());
    }

    @Override
    public int getBlockX() {
        return location.getBlockX();
    }

    @Override
    public int getBlockY() {
        return location.getBlockY();
    }

    @Override
    public int getBlockZ() {
        return location.getBlockZ();
    }
    
    public Location getLocation() {
        return location;
    }
}
