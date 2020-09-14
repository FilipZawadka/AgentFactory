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
    private static final String GOM_CLASS_NAME = "com.pw.agents.GoM";
    private static final String TR_CLASS_NAME = "com.pw.agents.TR";

    private Board board;

    public Factory(Scenario scenario, AgentContainer container) throws StaleProxyException {
        // initialization based on the scenario
        board = new Board(scenario.getBoardWidth(), scenario.getBoardHeight());
        int gomCount = scenario.getGomDefinitions().size();

        for (int i = 0; i < gomCount; i++) {
            GomDefinition gomDefinition = scenario.getGomDefinitions().get(i);

            Object[] gomArguments = new Object[]{i, gomDefinition.getPosition().getX(), gomDefinition.getPosition().getY()};
            AgentController agentController = container.createNewAgent(GOM(i), GOM_CLASS_NAME, gomArguments);
            agentController.start();

            Object[] trArguments = new Object[]{i, board};
            agentController = container.createNewAgent(TR(i), TR_CLASS_NAME, trArguments);
            agentController.start();
        }
    }
}
