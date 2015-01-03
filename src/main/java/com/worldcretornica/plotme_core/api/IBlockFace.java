package com.worldcretornica.plotme_core.api;


public enum IBlockFace {
    NORTH(0, 0, -1),
    EAST(1, 0, 0),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NORTH_EAST(NORTH, EAST),
    NORTH_WEST(NORTH, WEST),
    SOUTH_EAST(SOUTH, EAST),
    SOUTH_WEST(SOUTH, WEST),
    WEST_NORTH_WEST(WEST, NORTH_WEST),
    NORTH_NORTH_WEST(NORTH, NORTH_WEST),
    NORTH_NORTH_EAST(NORTH, NORTH_EAST),
    EAST_NORTH_EAST(EAST, NORTH_EAST),
    EAST_SOUTH_EAST(EAST, SOUTH_EAST),
    SOUTH_SOUTH_EAST(SOUTH, SOUTH_EAST),
    SOUTH_SOUTH_WEST(SOUTH, SOUTH_WEST),
    WEST_SOUTH_WEST(WEST, SOUTH_WEST),
    SELF(0, 0, 0);
    private final int modY;
    private final int modX;
    private final int modZ;

    IBlockFace(int modX, int modY, int modZ) {
        this.modX = modX;
        this.modY = modY;
        this.modZ = modZ;
    }

    IBlockFace(IBlockFace face1, IBlockFace face2) {
        this.modX = face1.getModX() + face2.getModX();
        this.modY = face1.getModY() + face2.getModY();
        this.modZ = face1.getModZ() + face2.getModZ();

    }

    public int getModX() {
        return modX;
    }

    public int getModY() {
        return modY;
    }

    public int getModZ() {
        return modZ;
    }

    public IBlockFace getOppositeFace() {
        switch (this) {
            case NORTH:
                return IBlockFace.SOUTH;

            case SOUTH:
                return IBlockFace.NORTH;

            case EAST:
                return IBlockFace.WEST;

            case WEST:
                return IBlockFace.EAST;

            case UP:
                return IBlockFace.DOWN;

            case DOWN:
                return IBlockFace.UP;

            case NORTH_EAST:
                return IBlockFace.SOUTH_WEST;

            case NORTH_WEST:
                return IBlockFace.SOUTH_EAST;

            case SOUTH_EAST:
                return IBlockFace.NORTH_WEST;

            case SOUTH_WEST:
                return IBlockFace.NORTH_EAST;

            case WEST_NORTH_WEST:
                return IBlockFace.EAST_SOUTH_EAST;

            case NORTH_NORTH_WEST:
                return IBlockFace.SOUTH_SOUTH_EAST;

            case NORTH_NORTH_EAST:
                return IBlockFace.SOUTH_SOUTH_WEST;

            case EAST_NORTH_EAST:
                return IBlockFace.WEST_SOUTH_WEST;

            case EAST_SOUTH_EAST:
                return IBlockFace.WEST_NORTH_WEST;

            case SOUTH_SOUTH_EAST:
                return IBlockFace.NORTH_NORTH_WEST;

            case SOUTH_SOUTH_WEST:
                return IBlockFace.NORTH_NORTH_EAST;

            case WEST_SOUTH_WEST:
                return IBlockFace.EAST_NORTH_EAST;

            case SELF:
                return IBlockFace.SELF;
        }

        return IBlockFace.SELF;
    }
}
