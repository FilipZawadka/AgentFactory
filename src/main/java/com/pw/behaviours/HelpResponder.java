
package com.pw.behaviours;

import com.pw.agents.TrAgent;
import com.pw.biddingOntology.BiddingOntology;
import com.pw.biddingOntology.JobInitialPosition;
import com.pw.biddingOntology.SendResult;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SSContractNetResponder;

import java.util.Random;

public class HelpResponder extends SSContractNetResponder {
    private Codec codec = new SLCodec();
    private Ontology onto = BiddingOntology.getInstance();

    public HelpResponder(Agent a, ACLMessage cfp) {
        super(a, cfp);
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        ACLMessage reply = cfp.createReply();
        //process the content
        reply.setPerformative(ACLMessage.PROPOSE);
        //calculate the UTILITY
        float utility = new Random().nextFloat();

        reply.setOntology(onto.getName());
        reply.setLanguage(codec.getName());

        SendResult sr = new SendResult();
        sr.setResult(utility);

        Action a = new Action(super.myAgent.getAID(), sr);
        try {
            super.myAgent.getContentManager().fillContent(reply, a);
        } catch (Codec.CodecException ce) {
            ce.printStackTrace();
        } catch (OntologyException oe) {
            oe.printStackTrace();
        }

        System.out.println("REPLY: " + reply);

        return reply;
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        // save the job destination in the destinations array
        ContentElement ce = null;
        try {
            ce = myAgent.getContentManager().extractContent(accept);
        } catch (Codec.CodecException e) {
            e.printStackTrace();
        } catch (OntologyException e) {
            e.printStackTrace();
        }

        if(ce instanceof JobInitialPosition){
            JobInitialPosition destination = (JobInitialPosition)ce;
            destination.setMessage(accept);
            ((TrAgent)myAgent).addJobPosition(destination);
        }

        return null;
    }
}