package com.pw.behaviours;

import com.pw.agents.GomAgent;
import com.pw.agents.GuiAgent;
import com.pw.agents.TrAgent;
import com.pw.board.Board;
import jade.core.behaviours.CyclicBehaviour;

public class UpdateBoardBehaviour extends CyclicBehaviour {
    @Override
    public void action() {
        Board board = ((GuiAgent)myAgent).getCurrentFactory().getBoard();
        int [] guiArr = new int[board.height*board.width];
        for (GomAgent a: board.GomList){
            guiArr[a.getPosition().getX() + a.getPosition().getY()*board.width] += 100;
        }
        for (TrAgent a: board.TrList){
            guiArr[a.getPosition().getX() + a.getPosition().getY()*board.width] += 1;
        }
        ((GuiAgent)myAgent).getGui().updateGUI(guiArr);
    }
}
