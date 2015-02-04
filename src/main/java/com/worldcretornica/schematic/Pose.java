package com.worldcretornica.schematic;

import java.util.List;

public class Pose extends AbstractSchematicElement {

    private static final long serialVersionUID = 3379214820104525045L;

    private final List<Float> body;
    private final List<Float> head;
    private final List<Float> leftarm;
    private final List<Float> rightarm;
    private final List<Float> leftleg;
    private final List<Float> rightleg;
    
    public Pose(List<Float> body, List<Float> head, List<Float> leftarm, List<Float> rightarm, List<Float> leftleg, List<Float> rightleg) {
        this.body = body;
        this.head = head;
        this.leftarm = leftarm;
        this.rightarm = rightarm;
        this.leftleg = leftleg;
        this.rightleg = rightleg;
    }
    
    public List<Float> getBody() { return body; }
    public List<Float> getHead() { return head; }
    public List<Float> getLeftArm() { return leftarm; }
    public List<Float> getRightArm() { return rightarm; }
    public List<Float> getLeftLeg() { return leftleg; }
    public List<Float> getRightLeg() { return rightleg; }
    
    @Override
    public String toString() {
        return "{" + this.getClass().getName() + 
                ": body=" + Sanitize(body) + 
                ", head=" + Sanitize(head) + 
                ", leftarm=" + Sanitize(leftarm) + 
                ", rightarm=" + Sanitize(rightarm) + 
                ", leftleg=" + Sanitize(leftleg) + 
                ", rightleg=" + Sanitize(rightleg) + "}";
    }

}
