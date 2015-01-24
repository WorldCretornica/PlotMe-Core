package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import org.spongepowered.api.world.Location;

public class SpongeLocation implements ILocation {

    private final Location location;

    public SpongeLocation(Location location) {
        this.location = location;
    }

    @Override
    public IWorld getWorld() {
        return null;
    }

    @Override
    public int getBlockX() {
        return location.getBlock().getX();
    }

    @Override
    public int getBlockY() {
        return location.getBlock().getY();
    }

    @Override
    public int getBlockZ() {
        return location.getBlock().getZ();
    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public double getZ() {
        return 0;
    }

    @Override
    public IBlock getBlock() {
        return new SpongeBlockLoc(location.getBlock());
    }

    @Override
    public ILocation add(double x, double y, double z) {
        return new SpongeLocation(location.add(x, y, z));
    }
    
    public Location getLocation() {
        return location;
    }
}
