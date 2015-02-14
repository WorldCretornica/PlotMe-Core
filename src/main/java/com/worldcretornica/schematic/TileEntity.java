package com.worldcretornica.schematic;

import java.util.List;

public class TileEntity extends AbstractSchematicElement {

    private static final long serialVersionUID = -2080234794735672945L;

    // START - PRE 1.8
    private final Byte rot;
    private final Byte skulltype;
    private final Byte note;

    private final Integer x;
    private final Integer y;
    private final Integer z;
    private final Integer record;
    private final Integer outputsignal;
    private final Integer transfercooldown;
    private final Integer levels;
    private final Integer primary;
    private final Integer secondary;

    private final RecordItem recorditem;

    private final Short brewtime;
    private final Short delay;
    private final Short maxnearbyentities;
    private final Short maxspawndelay;
    private final Short minspawndelay;
    private final Short requiredplayerrange;
    private final Short spawncount;
    private final Short spawnrange;
    private final Short burntime;
    private final Short cooktime;

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
    private final Integer base;
    private final List<Pattern> patterns;
    // END - 1.8

    public TileEntity(Integer x, Integer y, Integer z, String customname, String id, List<Item> items, Byte rot,
            Byte skulltype, Short delay, Short maxnearbyentities, Short maxspawndelay, Short minspawndelay,
            Short requiredplayerrange, Short spawncount, Short spawnrange, String entityid, Short burntime, Short cooktime,
            String text1, String text2, String text3, String text4, Byte note, Integer record, RecordItem recorditem,
            Short brewtime, String command, Integer outputsignal, Integer transfercooldown, Integer levels,
            Integer primary, Integer secondary, List<Pattern> patterns, Integer base) {
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

    public Byte getRot() {
        return rot;
    }

    public Byte getSkullType() {
        return skulltype;
    }

    public Byte getNote() {
        return note;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public Integer getRecord() {
        return record;
    }

    public Integer getOutputSignal() {
        return outputsignal;
    }

    public Integer getTransferCooldown() {
        return transfercooldown;
    }

    public Integer getLevels() {
        return levels;
    }

    public Integer getPrimary() {
        return primary;
    }

    public Integer getSecondary() {
        return secondary;
    }

    public Integer getBase() {
        return base;
    }

    public RecordItem getRecordItem() {
        return recorditem;
    }

    public Short getDelay() {
        return delay;
    }

    public Short getMaxNearbyEntities() {
        return maxnearbyentities;
    }

    public Short getMaxSpawnDelay() {
        return maxspawndelay;
    }

    public Short getMinSpawnDelay() {
        return minspawndelay;
    }

    public Short getRequiredPlayerRange() {
        return requiredplayerrange;
    }

    public Short getSpawnCount() {
        return spawncount;
    }

    public Short getSpawnRange() {
        return spawnrange;
    }

    public Short getBurnTime() {
        return burntime;
    }

    public Short getCookTime() {
        return cooktime;
    }

    public Short getBrewTime() {
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
