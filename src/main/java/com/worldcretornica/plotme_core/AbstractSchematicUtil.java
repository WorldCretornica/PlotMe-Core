package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.schematic.Schematic;
import com.worldcretornica.schematic.jnbt.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public abstract class AbstractSchematicUtil {

    // List of blocks that should be placed last in world generation
    protected final Collection<Integer> blockPlacedLast = new HashSet<>();

    public abstract void pasteSchematic(IWorld world, Vector loc, Schematic schem);

    public abstract Schematic loadSchematic(File file) throws Exception;

    public abstract Schematic createCompiledSchematic(IWorld world, Vector loc1, Vector loc2);

    public abstract void saveCompiledSchematic(Schematic schem, String name);

    public abstract Schematic loadCompiledSchematic(String name);

    protected <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws Exception {
        if (!items.containsKey(key)) {
            throw new Exception(String.format("Schematic file is missing the %s tag.", key));
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(
                    tag.getName() + " tag is not of tag type " + expected.getName() + System.lineSeparator() + "tag is: " + tag);
        }
        return expected.cast(tag);
    }

    private <T> T convert(Tag obj, Class<T> expected) {
        if (obj == null) {
            return null;
        } else {
            if (!expected.isInstance(obj.getValue())) {
                throw new IllegalArgumentException(
                        obj.getName() + " tag is not of tag type " + expected.getName() + System.lineSeparator() + "tag is: " + obj);
            }

            return expected.cast(obj.getValue());
        }
    }

    protected <T> List<T> convert(List<Tag> tagList, Class<T> expected) {
        if (tagList != null) {
            List<T> newlist = new ArrayList<>();
            for (Tag tag : tagList) {
                newlist.add(convert(tag, expected));
            }
            return newlist;
        } else {
            return null;
        }
    }
    
    /*protected void setBlock(byte[][] result, int x, int y, int z, byte blockkid) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new byte[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blockkid;
    }*/
}