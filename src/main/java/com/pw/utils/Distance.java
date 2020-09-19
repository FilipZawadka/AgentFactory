package com.pw.utils;

import com.pw.biddingOntology.PositionInfo;

import static java.lang.Math.abs;

public class Distance {
    public static double euclidean(Position p1, Position p2){
        return Math.pow(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2), 0.5);
    }

    public static double euclidean(PositionInfo p1, PositionInfo p2){
        return Math.pow(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2), 0.5);
    }
    public static int absolute(PositionInfo p1, PositionInfo p2){
        return abs(p1.getX() - p2.getX())+ abs(p1.getY() - p2.getY());
    }
    public static boolean isEqual(PositionInfo p1, PositionInfo p2) {
    return (p1.getX() == p2.getX() && p1.getY() == p2.getY());
    }
    public static int absolute(Position p1, Position p2){
        return abs(p1.getX() - p2.getX())+ abs(p1.getY() - p2.getY());
    }
    public static boolean isEqual(Position p1, Position p2) {
        return (p1.getX().equals(p2.getX()) && p1.getY().equals(p2.getY()));
    }

}
