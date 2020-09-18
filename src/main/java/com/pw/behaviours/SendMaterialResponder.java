package com.pw.behaviours;

import com.pw.agents.TrAgent;
import com.pw.biddingOntology.GomJobRequest;
import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import lombok.SneakyThrows;

public class SendMaterialResponder extends AchieveREResponder {

    public SendMaterialResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    @SneakyThrows
    protected ACLMessage handleRequest(ACLMessage request) {
        ContentElement ce;
        GomJobRequest jobRequest = null;
        ce = myAgent.getContentManager().extractContent(request);
        if (ce instanceof Action && ((Action) ce).getAction() instanceof GomJobRequest) {
            jobRequest = (GomJobRequest) ((Action) ce).getAction();
        }
        System.out.println("$$$$$$$$$$$$$ TR received message: " + jobRequest);

        ACLMessage bid = new ACLMessage(ACLMessage.CFP);

        // info from the gom's request
        ((TrAgent) myAgent).prepareHelpCfp(jobRequest, bid, myAgent);
        int trNumber = jobRequest.getTrNumber();
        myAgent.addBehaviour(new HelpInitiator(myAgent, bid, trNumber));

        ACLMessage response = request.createReply();
        response.setPerformative(ACLMessage.AGREE);

        return response;
    }
}
