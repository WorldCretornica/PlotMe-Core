package com.worldcretornica.plotme_core.api;

public class ILocation {

    private final Vector pos;

    private final IWorld world;
    private int topZ;

    public ILocation(IWorld world) {
        this(world, new Vector());
    }

    public ILocation(IWorld world, Vector vector) {
        pos = vector;
        this.world = world;
    }

    public ILocation(IWorld world, double x, double y, double z) {
        this(world, new Vector(x, y, z));
    }

    public ILocation(IWorld world, int x, int y, int z) {
        this(world, (double) x, (double) y, (double) z);
    }

    public IWorld getWorld() {
        return world;
    }

    public int getBlockX() {
        return pos.getBlockX();
    }

    public int getBlockY() {
        return pos.getBlockY();
    }

    public int getBlockZ() {
        return pos.getBlockZ();
    }

    public double getX() {
        return pos.getX();
    }

    public void setX(double x) {
        pos.setX(x);
    }

    public Vector getPos() {
        return pos;
    }

    public double getY() {
        return pos.getY();
    }

    public void setY(double y) {
        pos.setY(y);
    }

    public double getZ() {
        return pos.getZ();
    }

    public void setZ(double z) {
        pos.setZ(z);
    }

    public ILocation add(double x, double y, double z) {
        return new ILocation(getWorld(), pos.add(x, y, z));
    }

    public ILocation subtract(double x, double y, double z) {
        return new ILocation(getWorld(), pos.subtract(x, y, z));
    }

    @Override
    public int hashCode() {
        int result;
        result = 31 * this.getWorld().hashCode() + pos.hashCode();
        return result;
    }

    public IBlock getBlock() {
        return world.getBlockAt(getBlockX(), getBlockY(), getBlockZ());
    }

}
