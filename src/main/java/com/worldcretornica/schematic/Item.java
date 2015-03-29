package com.worldcretornica.schematic;


public class Item extends AbstractSchematicElement {

    private static final long serialVersionUID = 8615036231087494336L;
    private final byte count;
    private final byte slot;
    private final short damage;
    private final short id;
    private final ItemTag tag;

    public Item(byte count, byte slot, short damage, short id, ItemTag tag) {
        this.count = count;
        this.slot = slot;
        this.damage = damage;
        this.id = id;
        this.tag = tag;
    }

    public byte getCount() {
        return count;
    }

    public byte getSlot() {
        return slot;
    }

    public short getDamage() {
        return damage;
    }

    public short getId() {
        return id;
    }

    public ItemTag getTag() {
        return tag;
    }

    public String toString() {
        return "{" + this.getClass().getName() +
                ": count=" + Sanitize(count) +
                ", slot=" + Sanitize(slot) +
                ", damage=" + Sanitize(damage) +
                ", id=" + Sanitize(id) +
                ", tag=" + Sanitize(tag) + "}";

    }
}
