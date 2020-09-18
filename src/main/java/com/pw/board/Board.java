package com.pw.board;

import com.pw.utils.Position;
import jade.core.Agent;

import java.util.Map;

public class Board extends Agent {
    public Map<Position, Cell> cells;
    public int width, height;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
