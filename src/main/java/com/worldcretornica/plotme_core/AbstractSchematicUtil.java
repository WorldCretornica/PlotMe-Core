package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.schematic.Schematic;
import com.worldcretornica.schematic.jnbt.Tag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public abstract class AbstractSchematicUtil {

    // List of blocks that should be placed last in world generation
    protected final Collection<Integer> blockPlacedLast = new HashSet<>();

    public abstract void pasteSchematic(ILocation loc, Schematic schem);

    public abstract Schematic loadSchematic(File file) throws IOException, IllegalArgumentException;

    public abstract Schematic createCompiledSchematic(ILocation loc1, ILocation loc2);

    public abstract void saveCompiledSchematic(Schematic schem, String name);

    public abstract Schematic loadCompiledSchematic(String name);

    protected <T extends Tag, K> K getChildTag(Map<String, Tag> items, String key, Class<T> expected, Class<K> result) {
        if (!items.containsKey(key)) {
            return null;
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(
                    tag.getName() + " tag is not of tag type " + expected.getName() + System.lineSeparator() + "tag is: " + tag);
        }
        Object obj = expected.cast(tag).getValue();
        if (!result.isInstance(obj)) {
            return null;
        }
        return result.cast(obj);
    }

    protected <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) {
        if (!items.containsKey(key)) {
            return null;
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(
                    tag.getName() + " tag is not of tag type " + expected.getName() + System.lineSeparator() + "tag is: " + tag);
        }
        return expected.cast(tag);
    }

    private <T> T convert(Object obj, Class<T> expected) {
        if (!(obj instanceof Tag)) {
            return null;
        } else {

            Tag tag = (Tag) obj;

            if (!expected.isInstance(tag.getValue())) {
                throw new IllegalArgumentException(
                        tag.getName() + " tag is not of tag type " + expected.getName() + System.lineSeparator() + "tag is: " + tag);
            }

            return expected.cast(tag.getValue());
        }
    }

    protected <T> List<T> convert(List<?> tagList, Class<T> expected) {
        if (tagList != null) {
            List<T> newlist = new ArrayList<>();
            for (Object tag : tagList) {
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