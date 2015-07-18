package com.worldcretornica.plotme_core.bukkit;

import static com.google.common.base.Preconditions.checkNotNull;

import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.bukkit.api.BukkitEntity;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;

/**
 * A utility class that allows the conversion of any object from the Bukkit API to an
 * object that can be used in the PlotMe API.
 */
public class BukkitUtil {

    public static org.bukkit.Location adapt(Location location) {
        return new org.bukkit.Location(adapt(location.getWorld()), location.getX(), location.getY(), location.getZ());
    }

    private static World adapt(IWorld world) {
        if (world instanceof BukkitWorld) {
            return ((BukkitWorld) world).getWorld();
        }
        return null;
    }

    public static Vector locationToVector(org.bukkit.Location location) {
        return new Vector(location.getX(), location.getY(), location.getZ());
    }

    public static BukkitWorld adapt(World world) {
        return new BukkitWorld(world);
    }

    public static IEntity adapt(org.bukkit.entity.Entity entity) {
        checkNotNull(entity);
        return new BukkitEntity(entity);
    }

    public static Location adapt(org.bukkit.Location location) {
        return new Location(adapt(location.getWorld()), location.getX(), location.getY(), location.getZ());
    }
}
