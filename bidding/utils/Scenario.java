package utils;

public class Scenario {
    // class holding the info on:
    // board size
    // number of goms
    // position of goms

    public Integer boardWidth;
    public Integer boardHeight;
    public Position[] positions;

    public Scenario(Integer width, Integer height,Position[] positions){
        this.boardWidth = width;
        this.boardHeight = height;
        this.positions = positions;
    }
}
