import board.Board;
import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import utils.Position;
import utils.Scenario;

import java.util.HashMap;

public class Factory {
    Board board;
    HashMap<Integer, Agent[]> agentPairs;

    public Factory(Scenario scenario, AgentContainer container) throws StaleProxyException {
        // initialization based on the scenario
        board = new Board(scenario.boardWidth,scenario.boardHeight);

        int gomNumber = 3;
        Position[] gomPositions = new Position[gomNumber];

        for(Integer i=0; i<gomNumber; i++){
            gomPositions[i] = new Position(0, 0);
            AgentController ac = container.createNewAgent("GoM"+i.toString(), "agents.GoM",
                    new Object[]{i, gomPositions[i].getX(),gomPositions[i].getY()});
            ac.start();

            ac = container.createNewAgent("TR"+i.toString(), "agents.TR", new Object[]{i, board});

            ac.start();
        }
    }
}
