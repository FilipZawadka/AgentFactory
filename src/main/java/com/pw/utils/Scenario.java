package com.pw.utils;

public class Scenario {
    // class holding the info on:
    // com.pw.board size
    // number of goms
    // position of goms

    public Integer boardWidth;
    public Integer boardHeight;

    public Scenario(Integer width, Integer height){
        this.boardWidth = width;
        this.boardHeight = height;
    }
}
