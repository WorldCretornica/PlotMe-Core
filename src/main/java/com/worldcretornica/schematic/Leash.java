package com.worldcretornica.schematic;


public class Leash extends AbstractSchematicElement {

    private static final long serialVersionUID = 5212830225976155819L;
    private final int x;
    private final int y;
    private final int z;

    public Leash(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "{" + this.getClass().getName() +
                ": x=" + Sanitize(x) +
                ", y=" + Sanitize(y) +
                ", z=" + Sanitize(z) + "}";
    }
}
