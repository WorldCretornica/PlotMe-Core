package com.worldcretornica.plotme_core.bukkit;

import static com.google.common.base.Preconditions.checkNotNull;

import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.bukkit.api.BukkitEntity;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * A utility class that allows the conversion of any object from the Bukkit API to an
 * object that can be used in the PlotMe API.
 */
public class BukkitUtil {

    public static Location adapt(ILocation location) {
        return new Location(adapt(location.getWorld()), 0, 0, 0);
    }

    private static World adapt(IWorld world) {
        if (world instanceof BukkitWorld) {
            return ((BukkitWorld) world).getWorld();
        }
        return null;
    }

    public static Vector locationToVector(Location location) {
        return new Vector(location.getX(), location.getY(), location.getZ());
    }

    public static BukkitWorld adapt(World world) {
        return new BukkitWorld(world);
    }

    public static IEntity adapt(org.bukkit.entity.Entity entity) {
        checkNotNull(entity);
        return new BukkitEntity(entity);
    }

}
