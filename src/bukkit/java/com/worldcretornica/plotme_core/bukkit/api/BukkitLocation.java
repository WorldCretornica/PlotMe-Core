package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import org.bukkit.Location;

public class BukkitLocation implements ILocation {

    private final Location location;

    public BukkitLocation(Location location) {
        this.location = location;
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

    @Override
    public double getX() {
        return location.getX();
    }

    @Override
    public double getY() {
        return location.getY();
    }

    @Override
    public double getZ() {
        return location.getZ();
    }

    @Override
    public IBlock getBlock() {
        return new BukkitBlock(location.getBlock());
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "World: " + location.getWorld().getName().toLowerCase() + " X/Y/Z: " + getX() + "," + getY() + "," + getZ() + " Block: " + getBlock();
    }

    @Override
    public ILocation add(double x, double y, double z) {
        return new BukkitLocation(location.add(x, y, z));
    }
}
