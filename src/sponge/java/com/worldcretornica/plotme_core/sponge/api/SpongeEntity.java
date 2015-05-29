package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.TeleportRunnable;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Location;

import java.util.UUID;

public class SpongeEntity implements IEntity {

    private final Entity entity;

    public SpongeEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public ILocation getLocation() {
        return new ILocation(getWorld(), getPosition());
    }

    @Override
    public void setLocation(ILocation location) {
        entity.setLocation(new Location(entity.getWorld(), location.getX(), location.getY(), location.getZ()));
    }

    /**
     * Uses the code that allows a delay while
     * "Teleporting" or moving the entity
     *

     * @param location new location
     */
    @Override public void teleport(ILocation location) {
        PlotMe_CorePlugin.getInstance().getServerObjectBuilder().runTaskLater(new TeleportRunnable(this, location), PlotMe_CorePlugin.getInstance()
                .getAPI().getConfig().getInt("tp-delay"));
    }

    @Override
    public IWorld getWorld() {
        return new SpongeWorld(entity.getWorld());
    }

    @Override
    public void remove() {
        entity.remove();
    }

    @Override
    public UUID getUniqueId() {
        return entity.getUniqueId();
    }

    @Override
    public String getName() {
        return null; //Todo: Sponge doesn't have a get name for entities (yet)....kinda surprised they STILL DONT HAVE ONE
    }

    @Override
    public Vector getPosition() {
        return new Vector(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ());
    }
}
