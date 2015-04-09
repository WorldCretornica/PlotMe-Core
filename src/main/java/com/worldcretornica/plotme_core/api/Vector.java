package com.worldcretornica.plotme_core.api;

import com.worldcretornica.plotme_core.utils.DoubleHelper;

public class Vector implements Comparable<Vector> {

    private double x;

    private double y;
    private double z;

    Vector() {
        this(0, 0, 0);
    }

    Vector(int x, int y, int z) {
        this((double) x, (double) y, (double) z);
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getBlockX() {
        return (int) x;
    }

    public int getBlockY() {
        return (int) y;
    }

    public int getBlockZ() {
        return (int) z;
    }

    @Override
    public int compareTo(Vector o) {
        return 0;
    }

    @Override
    public int hashCode() {
        int result = 31 + DoubleHelper.hashCode(this.getX());
        result = 31 * result + DoubleHelper.hashCode(this.getY());
        result = 31 * result + DoubleHelper.hashCode(this.getZ());
        return result;
    }

    public Vector add(double x, double y, double z) {
        return new Vector(this.x + x, this.y + y, this.z + z);
    }

    public Vector subtract(double x, double y, double z) {
        return new Vector(this.x - x, this.y - y, this.z - z);
    }
}
