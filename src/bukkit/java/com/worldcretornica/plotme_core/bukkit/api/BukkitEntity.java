package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.TeleportRunnable;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.bukkit.BukkitUtil;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.UUID;

public class BukkitEntity implements IEntity {

    public final Entity entity;
    private final Vector coords;

    public BukkitEntity(Entity entity) {
        this.entity = entity;
        coords = BukkitUtil.locationToVector(entity.getLocation());
    }

    @Override
    public ILocation getLocation() {
        return new ILocation(getWorld(), getPosition());
    }

    @Override
    public void setLocation(ILocation location) {
        entity.teleport(new Location(((BukkitWorld) location.getWorld()).getWorld(), location.getX(), location.getY(), location.getZ()));
    }

    @Override public void teleport(ILocation location) {
        PlotMe_CorePlugin.getInstance().getServerObjectBuilder().runTaskLater(new TeleportRunnable(this, location), PlotMe_CorePlugin.getInstance()
                .getAPI().getConfig().getInt("tp-delay"));
    }

    /**
     * Get the world the entity is currently in.
     *
     * @return the world the entity is in
     */
    @Override
    public IWorld getWorld() {
        return new BukkitWorld(entity.getWorld());
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

    public void setMetadata(String string, MetadataValue value) {
        entity.setMetadata(string, value);
    }

    public List<MetadataValue> getMetadata(String string) {
        return entity.getMetadata(string);
    }
}
