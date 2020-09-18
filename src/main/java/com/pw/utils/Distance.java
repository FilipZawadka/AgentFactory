package com.pw.utils;

import com.pw.biddingOntology.PositionInfo;

public class Distance {
    public static double euclidean(Position p1, Position p2){
        return Math.pow(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2), 0.5);
    }

    public static double euclidean(PositionInfo p1, PositionInfo p2){
        return Math.pow(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2), 0.5);
    }
}
