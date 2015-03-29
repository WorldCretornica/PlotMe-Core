package com.worldcretornica.schematic;

import java.util.List;

public class Schematic extends AbstractSchematicElement {

    private static final long serialVersionUID = 8966082365181590943L;

    private final int[] blocks;
    private final byte[] data;
    private final String materials;
    private final short width;
    private final short length;
    private final short height;
    private final List<Entity> entities;
    private final List<TileEntity> tileentities;
    private final String schemauthor;
    private final int originx;
    private final int originy;
    private final int originz;

    public Schematic(int[] blocks, byte[] data, String materials, short width, short length, short height, List<Entity> entities,
            List<TileEntity> tileentities, String roomauthor, int originx, int originy, int originz) {
        this.blocks = blocks;
        this.data = data;
        this.materials = materials;
        this.width = width;
        this.length = length;
        this.height = height;
        this.entities = entities;
        this.tileentities = tileentities;
        this.schemauthor = roomauthor;
        this.originx = originx;
        this.originy = originy;
        this.originz = originz;
    }

    public int[] getBlocks() {
        return blocks;
    }

    public byte[] getData() {
        return data;
    }

    public String getMaterials() {
        return materials;
    }

    public short getWidth() {
        return width;
    }

    public short getLength() {
        return length;
    }

    public short getHeight() {
        return height;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<TileEntity> getTileEntities() {
        return tileentities;
    }

    public String getAuthor() {
        return schemauthor;
    }

    public int getOriginX() {
        return originx;
    }

    public int getOriginY() {
        return originy;
    }

    public int getOriginZ() {
        return originz;
    }

    public String toString() {
        return "{" + this.getClass().getName() +
                ": blocks=" + Sanitize(blocks) +
                ", data=" + Sanitize(data) +
                ", materials=" + Sanitize(materials) +
                ", width=" + Sanitize(width) +
                ", length=" + Sanitize(length) +
                ", height=" + Sanitize(height) +
                ", entities=" + Sanitize(entities) +
                ", tileentities=" + Sanitize(tileentities) +
                ", roomauthor=" + Sanitize(schemauthor) +
                ", originx=" + Sanitize(originx) +
                ", originy=" + Sanitize(originy) +
                ", originz=" + Sanitize(originz) + "}";
    }
}
