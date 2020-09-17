package com.pw.behaviours;

import com.pw.agents.GomAgent;
import com.pw.biddingOntology.GomInfo;
import com.pw.biddingOntology.GomJobRequest;
import com.pw.biddingOntology.MaterialInfo;
import com.pw.biddingOntology.PositionInfo;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import lombok.SneakyThrows;

import java.util.Vector;

import static com.pw.utils.Naming.GOM;
import static com.pw.utils.Naming.TR;

public class SendMaterialInitiator extends TickerBehaviour {

    public SendMaterialInitiator(Agent a, long period) {
        super(a, period);
    }

    @SneakyThrows
    @Override
    protected void onTick() {
        ACLMessage request = createRequest();

        myAgent.addBehaviour(new AchieveREInitiator(myAgent, request) {
            @Override
            protected void handleAllResponses(Vector responses) {
                for (Object message : responses) {
                    if (((ACLMessage) message).getPerformative() != ACLMessage.AGREE) {
                        // TODO handle the case when TR failed to react
                    }
                }
            }
        });
    }

    private ACLMessage createRequest() throws Codec.CodecException, OntologyException {
        GomAgent agent = (GomAgent) myAgent;

        //TODO fill with data:
        GomInfo from = new GomInfo(new AID(GOM(agent.getDefinition().getNumber()), AID.ISLOCALNAME), new PositionInfo(agent.getPosition()));
        GomInfo to = new GomInfo();
        MaterialInfo materialInfo = new MaterialInfo();
        GomJobRequest jobRequest = new GomJobRequest(from, to, materialInfo);

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(new AID(TR(agent.getDefinition().getNumber()), AID.ISLOCALNAME));
        request.setLanguage(agent.getCodec().getName());
        request.setOntology(agent.getOnto().getName());

        Action action = new Action(new AID(TR(agent.getDefinition().getNumber()), AID.ISLOCALNAME), jobRequest);
        agent.getContentManager().fillContent(request, action);

        return request;
    }
}
