package com.pw.board;

import com.pw.agents.GOTr;
import com.pw.agents.GomAgent;
import com.pw.agents.TrAgent;
import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;

public class Board extends Agent {
    public ArrayList<TrAgent> TrList;
    public ArrayList<GomAgent> GomList;
    public ArrayList<GOTr> GOTrList;
    public int width, height;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;

        TrList = new ArrayList<>();
        GomList = new ArrayList<>();
        GOTrList = new ArrayList<>();
    }

    public TrAgent getTrByAID(AID trAID){
        for (TrAgent a: TrList){
            if(a.getAID().equals(trAID)){
                return a;
            }
        }
        return null;
    }
    
    public GomAgent getGomByAID(AID gomAID){
        for (GomAgent a: GomList ){
            if(a.getAID().equals(gomAID)){
                return a;
            }
        }
        return null;
    }

}
