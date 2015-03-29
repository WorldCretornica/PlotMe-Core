package com.worldcretornica.schematic;


public class Modifier extends AbstractSchematicElement {

    private static final long serialVersionUID = 5083122183941649572L;
    private final int operation;
    private final double amount;
    private final String name;

    public Modifier(int operation, double amount, String name) {
        this.operation = operation;
        this.amount = amount;
        this.name = name;
    }

    public int getOperation() {
        return operation;
    }

    public double getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{" + this.getClass().getName() +
                ": operation=" + Sanitize(operation) +
                ", amount=" + Sanitize(amount) +
                ", name=" + Sanitize(name) + "}";
    }

}
