package com.worldcretornica.plotme_core;


public class PlotId {

    private final int x;
    private final int z;

    public PlotId(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public PlotId(String id) {
        this.x = Integer.parseInt(id.substring(0, id.indexOf(';')));
        this.z = Integer.parseInt(id.substring(id.indexOf(';') + 1));
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getID() {
        return x + ";" + z;
    }

    @Override
    public String toString() {
        return getID();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof PlotId) {
            PlotId me = (PlotId) obj;
            result = (this.getX() == me.getX() && this.getZ() == me.getZ());
        }
        return result;
    }

    @Override
    public int hashCode() {
        return (31 * (31 + getX()) + getZ());
    }
}

