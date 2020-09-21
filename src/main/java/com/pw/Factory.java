package com.pw;

import com.pw.biddingOntology.BiddingOntology;
import com.pw.board.Board;
import com.pw.scenerios.Scenario;
import com.pw.utils.GomDefinition;
import guis.BoardGui;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.pw.utils.Naming.GOM;
import static com.pw.utils.Naming.TR;

@Getter
public class Factory {
    private static final String GOM_CLASS_NAME = "com.pw.agents.GomAgent";
    private static final String TR_CLASS_NAME = "com.pw.agents.TrAgent";
    public static final AtomicInteger CFP_ID_COUNTER = new AtomicInteger(0);
    public static final Codec CODEC = new SLCodec();
    public static final Ontology ONTO = BiddingOntology.getInstance();

    private Board board;
    private ArrayList<AgentController> controllers;

    public Factory(Scenario scenario, AgentContainer container) throws StaleProxyException {
        this.board = new Board(scenario.getBoardWidth(), scenario.getBoardHeight());
        this.controllers = new ArrayList<>();

        for (GomDefinition gom : scenario.getGomDefinitions()) {
            Object[] gomArguments = {gom, board};
            AgentController agentControllerGom = container.createNewAgent(GOM(gom.getNumber()), GOM_CLASS_NAME, gomArguments);
            agentControllerGom.start();
            this.controllers.add(agentControllerGom);

            Object[] trArguments = {gom.getNumber(), board, gom.getPosition(), scenario.getTrBreakContractValue()};
            AgentController agentControllerTr = container.createNewAgent(TR(gom.getNumber()), TR_CLASS_NAME, trArguments);
            agentControllerTr.start();
            this.controllers.add(agentControllerTr);
        }
    }

    public void delete() throws StaleProxyException {
        for(AgentController controller: this.controllers){
            controller.kill();
        }
//        for(TrAgent a : board.TrList)
//            a.doDelete();
//        for (GomAgent a : board.GomList)
//            a.doDelete();
    }
}
