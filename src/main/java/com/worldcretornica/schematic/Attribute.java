package com.worldcretornica.schematic;

import java.util.List;

public class Attribute extends AbstractSchematicElement {

    private static final long serialVersionUID = 519498698831096866L;
    private final double base;
    private final String name;
    private final List<Modifier> modifiers;

    public Attribute(double base, String name, List<Modifier> modifiers) {
        this.base = base;
        this.name = name;
        this.modifiers = modifiers;
    }

    public double getBase() {
        return base;
    }

    public String getName() {
        return name;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    @Override
    public String toString() {
        return "{" + this.getClass().getName() +
                ": base=" + Sanitize(base) +
                ", name=" + Sanitize(name) +
                ", modifiers=" + Sanitize(modifiers) + "}";
    }

}
