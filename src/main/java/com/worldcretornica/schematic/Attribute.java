package com.worldcretornica.schematic;

import java.util.List;

public class Attribute extends AbstractSchematicElement {

    private static final long serialVersionUID = 519498698831096866L;
    private final Double base;
    private final String name;
    private final List<Modifier> modifiers;

    public Attribute(Double base, String name, List<Modifier> modifiers) {
        this.base = base;
        this.name = name;
        this.modifiers = modifiers;
    }

    public Double getBase() {
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
