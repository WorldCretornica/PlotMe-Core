package com.worldcretornica.schematic;

import java.io.Serializable;

abstract class AbstractSchematicElement implements Serializable {

    private static final long serialVersionUID = -504382438942523971L;

    public abstract String toString();

    String Sanitize(Object object) {
        if (object == null) {
            return "";
        } else {
            return object.toString();
        }
    }
}
