package com.pw.behaviours;

import java.util.Vector;

import com.pw.agents.GoM;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class SendMaterialInitiator extends TickerBehaviour {

    public SendMaterialInitiator(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(new AID("TR"+((GoM)myAgent).getId(), AID.ISLOCALNAME));
        request.setLanguage((((GoM)myAgent).codec).getName());
        request.setOntology((((GoM)myAgent).onto).getName());

        myAgent.addBehaviour(new AchieveREInitiator(myAgent, request){
            @Override
            protected void handleAllResponses(Vector responses){
                for(Object message : responses){
                    if(((ACLMessage)message).getPerformative() != ACLMessage.AGREE){
                        // TODO handle the case when TR failed to react
                    }
                }
            }
        });
    }
}
