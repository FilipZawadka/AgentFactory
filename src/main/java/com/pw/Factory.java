package com.pw;

import com.pw.board.Board;
import com.pw.utils.Position;
import com.pw.utils.Scenario;
import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.HashMap;

public class Factory {
    Board board;
    HashMap<Integer, Agent[]> agentPairs;

    public Factory(Scenario scenario, AgentContainer container) throws StaleProxyException {
        // initialization based on the scenario
        board = new Board(scenario.boardWidth, scenario.boardHeight);

        int gomNumber = 3;
        Position[] gomPositions = new Position[gomNumber];

        for (Integer i = 0; i < gomNumber; i++) {
            gomPositions[i] = new Position(0, 0);
            AgentController ac = container.createNewAgent("GoM" + i.toString(), "com.pw.agents.GoM",
                new Object[]{i, gomPositions[i].getX(), gomPositions[i].getY()});
            ac.start();

            ac = container.createNewAgent("TR" + i.toString(), "com.pw.agents.TR", new Object[]{i, board});

            ac.start();
        }
    }
}
