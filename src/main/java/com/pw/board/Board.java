package com.pw.board;

import com.pw.agents.GOTr;
import com.pw.agents.GomAgent;
import com.pw.agents.TrAgent;
import com.pw.utils.Position;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.Map;

public class Board extends Agent {
    public ArrayList<TrAgent> TrList;
    public ArrayList<GomAgent> GomList;
    public ArrayList<GOTr> GOTrList;
    public int width, height;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
