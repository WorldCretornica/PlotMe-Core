package com.worldcretornica.plotme_core.api;

import com.worldcretornica.plotme_core.utils.DoubleHelper;

public class Vector implements Comparable<Vector> {

    private double x;
    private double y;
    private double z;

    public Vector() {
        this(0, 0, 0);
    }

    public Vector(int x, int y, int z) {
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
        return (int) Math.round(x);
    }

    public int getBlockY() {
        return (int) Math.round(y);
    }

    public int getBlockZ() {
        return (int) Math.round(z);
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Override public String toString() {
        return "X: " + x + " Y: " + y + " Z: " + z;
    }

    @Override
    public int compareTo(Vector o) {
        return (int) (lengthSquared() - o.lengthSquared());
    }

    @Override public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Vector)) {
            return false;
        }
        Vector vector = ((Vector) obj);
        //noinspection RedundantIfStatement
        if (Double.compare(vector.x, this.x) != 0 || Double.compare(vector.y, this.y) != 0 || Double.compare(vector.z, this.z) != 0) {
            return false;
        }
        return true;
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

    public Vector add(Vector vector) {
        return add(vector.getX(), vector.getY(), vector.getZ());
    }

    public Vector subtract(Vector vector) {
        return subtract(vector.getX(), vector.getY(), vector.getZ());
    }
}
