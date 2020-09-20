package com.pw.behaviours;

import com.pw.agents.TrAgent;
import com.pw.biddingOntology.BiddingOntology;
import com.pw.biddingOntology.GomJobRequest;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import lombok.SneakyThrows;

public class SendMaterialResponder extends AchieveREResponder {
    private Codec codec = new SLCodec();
    private Ontology onto = BiddingOntology.getInstance();

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

        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);

        // info from the gom's request
        ((TrAgent) myAgent).prepareHelpCfp(jobRequest, cfp, myAgent);
        int trNumber = jobRequest.getTrNumber();
        myAgent.addBehaviour(new HelpInitiator(myAgent, cfp, trNumber));

        // response to gom
        ACLMessage response = request.createReply();
        response.setPerformative(ACLMessage.AGREE);

//        // own proposal
//        ACLMessage ownProposal = ACLMessage(ACLMessage.PR)
//        ownProposal.setPerformative(ACLMessage.PROPOSE);
//
//        float taskDistance = Distance.absolute(jobRequest.getTo().getPosition(),jobRequest.getFrom().getPosition());
//        float utility = ((TrAgent) myAgent).utilityFunction(taskDistance,false);
//        ownProposal.setOntology(onto.getName());
//        ownProposal.setLanguage(codec.getName());
//        SendResult sr = new SendResult();
//        sr.setResult(utility);
//
//        Action a = new Action(super.myAgent.getAID(), sr);
//        try {
//            super.myAgent.getContentManager().fillContent(ownProposal, a);
//        } catch (Codec.CodecException cex) {
//            cex.printStackTrace();
//        } catch (OntologyException oex) {
//            oex.printStackTrace();
//        }
//        myAgent.send(ownProposal);

        return response;
    }
}
