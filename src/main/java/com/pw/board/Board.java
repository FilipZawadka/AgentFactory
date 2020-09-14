package com.pw.board;

import java.util.Map;

import jade.core.Agent;
import com.pw.utils.Position;
public class
Board extends Agent {
    public Map<Position, Cell> cells;
    public int width, height;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
