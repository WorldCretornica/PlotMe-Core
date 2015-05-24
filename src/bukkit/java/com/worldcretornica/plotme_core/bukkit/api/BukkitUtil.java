package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.Vector;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * A utility class that allows the conversion of any object from the Bukkit API to an
 * object that can be used in the PlotMe API.
 */
public class BukkitUtil {

    public static Location toBukkitLocation(ILocation location) {
        return new Location(null, 0, 0, 0);
    }

    public static Vector locationToVector(Location location) {
        return new Vector(location.getX(), location.getY(), location.getZ());
    }

    public static BukkitWorld adapt(World world) {
        return new BukkitWorld(world);
    }


}
