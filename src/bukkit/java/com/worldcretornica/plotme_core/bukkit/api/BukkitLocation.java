package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.World;

public class BukkitLocation implements Location {

    private final org.bukkit.Location location;

    public BukkitLocation(org.bukkit.Location location) {
        this.location = location;
    }

    @Override
    public World getWorld() {
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

    public org.bukkit.Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "World: " + location.getWorld().getName().toLowerCase() + " X/Y/Z: " + getX() + "," + getY() + "," + getZ() + " Block: " + getBlock();
    }

    @Override
    public Location add(double x, double y, double z) {
        return new BukkitLocation(location.add(x, y, z));
    }
}
