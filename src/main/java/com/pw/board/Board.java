package com.pw.board;

import com.pw.GUI;
import com.pw.agents.GOTr;
import com.pw.agents.GomAgent;
import com.pw.agents.TrAgent;
import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;
<<<<<<< HEAD
import java.util.Arrays;
import java.util.Map;
=======
>>>>>>> 24b71bcc221d43e00afb059efa8967b1f4e3a2c8

public class Board extends Agent {
    public ArrayList<TrAgent> TrList;
    public ArrayList<GomAgent> GomList;
    public ArrayList<GOTr> GOTrList;
    public int width, height;
    public GUI gui;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;

        TrList = new ArrayList<>();
        GomList = new ArrayList<>();
        GOTrList = new ArrayList<>();

        gui = new GUI(height,width);
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

    public void updateGUI(){
        int [] guiArr = new int[height*width];
        for (GomAgent a: GomList){
            guiArr[a.getPosition().getX() + a.getPosition().getY()*width] = 100;
        }
        for (TrAgent a: TrList){
            guiArr[a.getPosition().getX() + a.getPosition().getY()*width] += 1;
        }
        gui.updateGUI(guiArr);
    }

}
