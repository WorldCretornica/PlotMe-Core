package com.worldcretornica.schematic;

import java.util.List;

public class TileEntity extends AbstractSchematicElement {

    private static final long serialVersionUID = -2080234794735672945L;

    // START - PRE 1.8
    private final byte rot;
    private final byte skulltype;
    private final byte note;

    private final int x;
    private final int y;
    private final int z;
    private final int record;
    private final int outputsignal;
    private final int transfercooldown;
    private final int levels;
    private final int primary;
    private final int secondary;

    private final RecordItem recorditem;

    private final short brewtime;
    private final short delay;
    private final short maxnearbyentities;
    private final short maxspawndelay;
    private final short minspawndelay;
    private final short requiredplayerrange;
    private final short spawncount;
    private final short spawnrange;
    private final short burntime;
    private final short cooktime;

    private final String command;
    private final String customname;
    private final String id;
    private final String entityid;
    private final String text1;
    private final String text2;
    private final String text3;
    private final String text4;

    private final List<Item> items;
    // END - PRE 1.8

    // START - 1.8
    private final int base;
    private final List<Pattern> patterns;
    // END - 1.8

    public TileEntity(int x, int y, int z, String customname, String id, List<Item> items, byte rot,
            byte skulltype, short delay, short maxnearbyentities, short maxspawndelay, short minspawndelay,
            short requiredplayerrange, short spawncount, short spawnrange, String entityid, short burntime, short cooktime,
            String text1, String text2, String text3, String text4, byte note, int record, RecordItem recorditem,
            short brewtime, String command, int outputsignal, int transfercooldown, int levels,
            int primary, int secondary, List<Pattern> patterns, int base) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.customname = customname;
        this.id = id;
        this.items = items;
        this.rot = rot;
        this.skulltype = skulltype;
        this.delay = delay;
        this.maxnearbyentities = maxnearbyentities;
        this.maxspawndelay = maxspawndelay;
        this.minspawndelay = minspawndelay;
        this.requiredplayerrange = requiredplayerrange;
        this.spawncount = spawncount;
        this.spawnrange = spawnrange;
        this.entityid = entityid;
        this.burntime = burntime;
        this.cooktime = cooktime;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.note = note;
        this.record = record;
        this.recorditem = recorditem;
        this.brewtime = brewtime;
        this.command = command;
        this.outputsignal = outputsignal;
        this.transfercooldown = transfercooldown;
        this.levels = levels;
        this.primary = primary;
        this.secondary = secondary;
        this.patterns = patterns;
        this.base = base;
    }

    public byte getRot() {
        return rot;
    }

    public byte getSkullType() {
        return skulltype;
    }

    public byte getNote() {
        return note;
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

    public int getRecord() {
        return record;
    }

    public int getOutputSignal() {
        return outputsignal;
    }

    public int getTransferCooldown() {
        return transfercooldown;
    }

    public int getLevels() {
        return levels;
    }

    public int getPrimary() {
        return primary;
    }

    public int getSecondary() {
        return secondary;
    }

    public int getBase() {
        return base;
    }

    public RecordItem getRecordItem() {
        return recorditem;
    }

    public short getDelay() {
        return delay;
    }

    public short getMaxNearbyEntities() {
        return maxnearbyentities;
    }

    public short getMaxSpawnDelay() {
        return maxspawndelay;
    }

    public short getMinSpawnDelay() {
        return minspawndelay;
    }

    public short getRequiredPlayerRange() {
        return requiredplayerrange;
    }

    public short getSpawnCount() {
        return spawncount;
    }

    public short getSpawnRange() {
        return spawnrange;
    }

    public short getBurnTime() {
        return burntime;
    }

    public short getCookTime() {
        return cooktime;
    }

    public short getBrewTime() {
        return brewtime;
    }

    public String getEntityId() {
        return entityid;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

    public String getText3() {
        return text3;
    }

    public String getText4() {
        return text4;
    }

    public String getCustomName() {
        return customname;
    }

    public String getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }

    public String toString() {
        return "{" + this.getClass().getName() +
                ": x=" + Sanitize(x) +
                ", y=" + Sanitize(y) +
                ", z=" + Sanitize(z) +
                ", customname=" + Sanitize(customname) +
                ", id=" + Sanitize(id) +
                ", items=" + Sanitize(items) +
                ", rot=" + Sanitize(rot) +
                ", skulltype=" + Sanitize(skulltype) +
                ", delay=" + Sanitize(delay) +
                ", maxnearbyentities=" + Sanitize(maxnearbyentities) +
                ", maxspawndelay=" + Sanitize(maxspawndelay) +
                ", minspawndelay=" + Sanitize(minspawndelay) +
                ", requiredplayerrange=" + Sanitize(requiredplayerrange) +
                ", spawncount=" + Sanitize(spawncount) +
                ", spawnrange=" + Sanitize(spawnrange) +
                ", entityid=" + Sanitize(entityid) +
                ", burntime=" + Sanitize(burntime) +
                ", cooktime=" + Sanitize(cooktime) +
                ", text1=" + Sanitize(text1) +
                ", text2=" + Sanitize(text2) +
                ", text3=" + Sanitize(text3) +
                ", text4=" + Sanitize(text4) +
                ", note=" + Sanitize(note) +
                ", record=" + Sanitize(record) +
                ", recorditem=" + Sanitize(recorditem) +
                ", brewtime=" + Sanitize(brewtime) +
                ", command=" + Sanitize(command) +
                ", outputsignal=" + Sanitize(outputsignal) +
                ", transfercooldown=" + Sanitize(transfercooldown) +
                ", levels=" + Sanitize(levels) +
                ", primary=" + Sanitize(primary) +
                ", secondary=" + Sanitize(secondary) +
                ", patterns=" + Sanitize(patterns) +
                ", base=" + Sanitize(base) + "}";
    }
}
