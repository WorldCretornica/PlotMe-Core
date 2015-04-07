package com.worldcretornica.plotme_core;


import com.worldcretornica.plotme_core.api.IWorld;

public class PlotId {

    private final int x;
    private final int z;
    private Object world;

    public PlotId(int x, int z, IWorld world) {
        this.x = x;
        this.z = z;
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
            if (this.getX() == me.getX()) {
                if (this.getZ() == me.getZ()) {
                    result = this.getWorld() == me.getWorld();
                } else {
                    result = false;
                }
            } else {
                result = false;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + getX();
        hash = 31 * hash + getZ();
        return 31 * (31 + getX()) + getZ();
    }

    public Object getWorld() {
        return world;
    }
}

