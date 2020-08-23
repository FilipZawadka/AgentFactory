package biddingOntology;

import jade.content.Concept;

public class PositionInfo implements Concept {
    private Integer x,y;

    public PositionInfo(){

    }

    public PositionInfo(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
