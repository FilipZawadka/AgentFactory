package com.pw.behaviours;

import com.pw.agents.TrAgent;
import com.pw.biddingOntology.JobInitialPosition;
import com.pw.biddingOntology.PositionInfo;
import com.pw.utils.MessageComparator;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.Vector;

public class HelpInitiator extends ContractNetInitiator {
    private int trNumber;

    public HelpInitiator(Agent a, ACLMessage cfp, int _trNumber) {
        super(a, cfp);
        this.trNumber = _trNumber;
    }

    @SneakyThrows
    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        System.out.println("HANDLE RESPONSES");
        java.util.ArrayList<ACLMessage> results = new java.util.ArrayList<>();
        String conversation_id;
        // process all the utility function results
        for (Object proposal : responses) {
            if (((ACLMessage) proposal).getPerformative() == ACLMessage.PROPOSE) {
                results.add((ACLMessage) proposal);
            }
        }
        MessageComparator comparator = new MessageComparator();
        Collections.sort(results, comparator);
        // accept best trNumber proposals
        for (int i = 0; i < this.trNumber; i++) {
            ACLMessage m = results.get(i).createReply();
            m.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            // send info on where the job takes place
            try {
                JobInitialPosition d = new JobInitialPosition();
                d.setPosition(new PositionInfo(((TrAgent)myAgent).getPosition().getX(), ((TrAgent)myAgent).getPosition().getY()));
                myAgent.getContentManager().fillContent(m, d);
            } catch (Codec.CodecException e) {
                e.printStackTrace();
            } catch (OntologyException e) {
                e.printStackTrace();
            }

            acceptances.add(m);
            System.out.println("ACCEPT REPLY: " + m);
        }
        conversation_id = ((ACLMessage)acceptances.get(0)).getConversationId();
        myAgent.addBehaviour(new StartJobBehaviour(myAgent, conversation_id, (ACLMessage)(getDataStore().get(CFP_KEY))));
    }
}