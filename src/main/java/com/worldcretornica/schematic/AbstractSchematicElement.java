package com.worldcretornica.schematic;

import java.io.Serializable;

public abstract class AbstractSchematicElement implements Serializable {

    private static final long serialVersionUID = -504382438942523971L;

    public abstract String toString();

    protected String Sanitize(Object object) {
        return (object == null ? "" : object.toString());
    }
}
