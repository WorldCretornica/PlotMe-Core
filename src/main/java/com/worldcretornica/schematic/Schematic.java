package com.worldcretornica.schematic;

import java.util.List;

public class Schematic extends AbstractSchematicElement {
 
    private static final long serialVersionUID = 8966082365181590943L;

    private final int[] blocks;
    private final byte[] data;
    private final byte[] biomes;
    private final String materials;
    private final Short width;
    private final Short length;
    private final Short height;
    private final List<Entity> entities;
    private final List<TileEntity> tileentities;
    private final String schemauthor;
    private final Integer originx;
    private final Integer originy;
    private final Integer originz;
 
    public Schematic(int[] blocks, byte[] data, byte[] biomes, String materials, Short width, Short length, Short height, List<Entity> entities, List<TileEntity> tileentities, String roomauthor, Integer originx, Integer originy, Integer originz) {
        this.blocks = blocks;
        this.data = data;
        this.biomes = biomes;
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

    public byte[] getBiomes() {
        return biomes;
    }

    public String getMaterials() {
        return materials;
    }

    public Short getWidth() {
        return width;
    }

    public Short getLength() {
        return length;
    }

    public Short getHeight() {
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
    
    public Integer getOriginX() {
        return originx;
    }
    
    public Integer getOriginY() {
        return originy;
    }
    
    public Integer getOriginZ() {
        return originz;
    }
        
    public String toString() {
        return "{" + this.getClass().getName() + 
                ": blocks=" + Sanitize(blocks) +
                ", data=" + Sanitize(data) +
                ", biomes=" + Sanitize(biomes) +
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
