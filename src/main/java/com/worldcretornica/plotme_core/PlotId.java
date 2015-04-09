package com.worldcretornica.plotme_core;


import com.worldcretornica.plotme_core.api.IWorld;

public class PlotId {

    private final int x;
    private final int z;
    private IWorld world;

    public PlotId(int x, int z, IWorld world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    public PlotId(String id) throws NumberFormatException {
        this.x = Integer.parseInt(id.substring(0, id.indexOf(';')));
        this.z = Integer.parseInt(id.substring(id.indexOf(';') + 1));
    }

    /**
     * Check if the string is in the plot id format
     *
     * @param id id value to be checked
     * @return true if the id is valid, false otherwise
     */
    public static boolean isValidID(String id) {
        String[] coords = id.split(";");
        if (coords.length == 2) {
            try {
                Integer.parseInt(coords[0]);
                Integer.parseInt(coords[1]);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public IWorld getWorld() {
        return world;
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
            result = this.getX() == me.getX() && this.getZ() == me.getZ() && this.getWorld() == me.getWorld();
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = world.hashCode();
        hash += getX();
        hash += getZ();
        return hash;
    }
}

