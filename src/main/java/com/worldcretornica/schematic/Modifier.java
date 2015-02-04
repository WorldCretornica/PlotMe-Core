package com.worldcretornica.schematic;


public class Modifier extends AbstractSchematicElement {

    private static final long serialVersionUID = 5083122183941649572L;
    private final Integer operation;
    private final Double amount;
    private final String name;
    
    public Modifier(Integer operation, Double amount, String name) {
        this.operation = operation;
        this.amount = amount;
        this.name = name;
    }
    
    public Integer getOperation() {
        return operation;
    }
    
    public Double getAmount() {
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
