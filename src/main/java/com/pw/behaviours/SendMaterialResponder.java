package com.pw.behaviours;

import com.pw.agents.TR;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class SendMaterialResponder extends AchieveREResponder {
    public SendMaterialResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    protected ACLMessage handleRequest(ACLMessage request) {
        ACLMessage bid = new ACLMessage(ACLMessage.CFP);
        // info from the gom's request
        int trNumber = ((TR) myAgent).prepareCfp(request, bid, myAgent);
        myAgent.addBehaviour(new HelpInitiator(myAgent, bid, trNumber));

        ACLMessage response = request.createReply();
        response.setPerformative(ACLMessage.AGREE);

        return response;
    }
}
