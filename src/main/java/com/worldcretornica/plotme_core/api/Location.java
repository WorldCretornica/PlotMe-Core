package com.worldcretornica.plotme_core.api;

public class Location {

    private final Vector vector;

    private final IWorld world;

    /**
     * Creates a location in the given world with the vector set at 0,0,0
     * @param world world to create location
     */
    public Location(IWorld world) {
        this(world, new Vector());
    }

    public Location(IWorld world, Vector vector) {
        this.vector = vector;
        this.world = world;
    }

    public Location(IWorld world, double x, double y, double z) {
        this(world, new Vector(x, y, z));
    }

    public Location(IWorld world, int x, int y, int z) {
        this(world, (double) x, (double) y, (double) z);
    }

    public IWorld getWorld() {
        return world;
    }

    public int getBlockX() {
        return vector.getBlockX();
    }

    public int getBlockY() {
        return vector.getBlockY();
    }

    public int getBlockZ() {
        return vector.getBlockZ();
    }

    public double getX() {
        return vector.getX();
    }

    public void setX(double x) {
        vector.setX(x);
    }

    public Vector getVector() {
        return vector;
    }

    public double getY() {
        return vector.getY();
    }

    public void setY(double y) {
        vector.setY(y);
    }

    public double getZ() {
        return vector.getZ();
    }

    public void setZ(double z) {
        vector.setZ(z);
    }

    public Location add(double x, double y, double z) {
        return new Location(getWorld(), vector.add(x, y, z));
    }

    public Location subtract(double x, double y, double z) {
        return new Location(getWorld(), vector.subtract(x, y, z));
    }

    @Override
    public int hashCode() {
        int result;
        result = 31 * this.getWorld().hashCode() + vector.hashCode();
        return result;
    }

    @Override public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location location = (Location) obj;
            if (location.getWorld().equals(this.world) && location.getVector().equals(this.vector)) {
                return true;
            }

        }
        return false;
    }

    public IBlock getBlock() {
        return world.getBlockAt(getBlockX(), getBlockY(), getBlockZ());
    }

}
