package com.worldcretornica.schematic;


public class RecordItem extends AbstractSchematicElement {

    private static final long serialVersionUID = -7017507616421449624L;
    private final Byte count;
    private final Short damage;
    private final Short id;

    public RecordItem(Byte count, Short damage, Short id) {
        this.count = count;
        this.damage = damage;
        this.id = id;
    }

    public Byte getCount() {
        return count;
    }

    public Short getDamage() {
        return damage;
    }

    public Short getId() {
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
