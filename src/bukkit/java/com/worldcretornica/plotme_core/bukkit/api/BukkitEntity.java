package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class BukkitEntity implements IEntity {

    private final Entity entity;
    private final Vector coords;
    private final ILocation loc;
    private final BukkitWorld world;

    public BukkitEntity(Entity entity) {
        this.entity = entity;
        coords = BukkitConverter.locationToVector(entity.getLocation());
        world = BukkitConverter.adapt(entity.getWorld());
        loc = new ILocation(world, getPosition());
    }

    @Override
    public ILocation getLocation() {
        return loc;
    }

    @Override
    public void setLocation(ILocation location) {
        entity.teleport(new Location(((BukkitWorld) location.getWorld()).getWorld(), location.getX(), location.getY(), location.getZ()));
    }

    /**
     * Get the world the entity is currently in.
     *
     * @return the world the entity is in
     */
    @Override
    public IWorld getWorld() {
        return world;
    }

    @Override
    public void remove() {
        entity.remove();
    }

    /**
     * Gets the UUID of the actor
     *
     * @return UUID of the actor
     */
    @Override
    public UUID getUniqueId() {
        return entity.getUniqueId();
    }

    @Override
    public String getName() {
        return entity.getName();
    }

    public Vector getPosition() {
        return coords;
    }
}
