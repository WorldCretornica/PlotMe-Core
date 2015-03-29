package com.worldcretornica.schematic;


public class RecordItem extends AbstractSchematicElement {

    private static final long serialVersionUID = -7017507616421449624L;
    private final byte count;
    private final short damage;
    private final short id;

    public RecordItem(byte count, short damage, short id) {
        this.count = count;
        this.damage = damage;
        this.id = id;
    }

    public byte getCount() {
        return count;
    }

    public short getDamage() {
        return damage;
    }

    public short getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{" + this.getClass().getName() +
                ": count=" + Sanitize(count) +
                ", damage=" + Sanitize(damage) +
                ", id=" + Sanitize(id) + "}";
    }

}
