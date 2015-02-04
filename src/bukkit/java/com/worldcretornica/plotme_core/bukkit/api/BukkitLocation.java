package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import org.bukkit.*;

public class BukkitLocation implements ILocation, Cloneable {

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
    public void setX(double x) {
        this.location.setX(x);
    }

    @Override
    public double getY() {
        return location.getY();
    }

    @Override
    public void setY(double y) {
        this.location.setY(y);
    }

    @Override
    public double getZ() {
        return location.getZ();
    }

    @Override
    public void setZ(double z) {
        this.location.setZ(z);
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
    
    @Override
    public ILocation subtract(double x, double y, double z) {
        return new BukkitLocation(location.subtract(x, y, z));
    }
    
    @Override
    public ILocation clone() {
        return new BukkitLocation(location.clone());
    }
}
