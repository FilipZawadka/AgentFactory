package com.pw.utils;

public class NeighborPosition {
    public static Position getRightPosition(Position position){
        return new Position(position.getX()+1,position.getY());
    }
    public static Position getLeftPosition(Position position){
        return new Position(position.getX()-1,position.getY());
    }
    public static Position getUpPosition(Position position){
        return new Position(position.getX(),position.getY()+1);
    }
    public static Position getDownPosition(Position position){
        return new Position(position.getX(),position.getY()-1);
    }
}
