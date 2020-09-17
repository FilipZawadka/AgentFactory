package com.pw;

import com.pw.board.Board;
import com.pw.scenerios.GomDefinition;
import com.pw.scenerios.Scenario;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import static com.pw.utils.Naming.GOM;
import static com.pw.utils.Naming.TR;

public class Factory {
    private static final String GOM_CLASS_NAME = "com.pw.agents.GomAgent";
    private static final String TR_CLASS_NAME = "com.pw.agents.TrAgent";

    private Board board;

    public Factory(Scenario scenario, AgentContainer container) throws StaleProxyException {
        this.board = new Board(scenario.getBoardWidth(), scenario.getBoardHeight());

        for (GomDefinition gom : scenario.getGomDefinitions()) {
            Object[] gomArguments = {gom};
            AgentController agentController = container.createNewAgent(GOM(gom.getNumber()), GOM_CLASS_NAME, gomArguments);
            agentController.start();

            if (!gom.isInitial() && !gom.isFinal()) {
                Object[] trArguments = {gom.getNumber(), board};
                agentController = container.createNewAgent(TR(gom.getNumber()), TR_CLASS_NAME, trArguments);
                agentController.start();
            }
        }
    }
}
