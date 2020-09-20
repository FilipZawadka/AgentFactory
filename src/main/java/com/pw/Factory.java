package com.pw;

import com.pw.biddingOntology.BiddingOntology;
import com.pw.board.Board;
import com.pw.scenerios.GomDefinition;
import com.pw.scenerios.Scenario;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.concurrent.atomic.AtomicInteger;

import static com.pw.utils.Naming.GOM;
import static com.pw.utils.Naming.TR;

public class Factory {
    private static final String GOM_CLASS_NAME = "com.pw.agents.GomAgent";
    private static final String TR_CLASS_NAME = "com.pw.agents.TrAgent";
    public static final AtomicInteger CFP_ID_COUNTER = new AtomicInteger(0);
    public static final Codec CODEC = new SLCodec();
    public static final Ontology ONTO = BiddingOntology.getInstance();

    public Factory(Scenario scenario, AgentContainer container) throws StaleProxyException {
        Board board = new Board(scenario.getBoardWidth(), scenario.getBoardHeight());

        for (GomDefinition gom : scenario.getGomDefinitions()) {
            Object[] gomArguments = {gom,board};
            AgentController agentController = container.createNewAgent(GOM(gom.getNumber()), GOM_CLASS_NAME, gomArguments);
            agentController.start();

            Object[] trArguments = {gom.getNumber(), board, gom.getPosition(), scenario.getTrBreakContractValue()};
            agentController = container.createNewAgent(TR(gom.getNumber()), TR_CLASS_NAME, trArguments);
            agentController.start();
        }
        GUI gui = new GUI(board);
        gui.start();
    }
}
