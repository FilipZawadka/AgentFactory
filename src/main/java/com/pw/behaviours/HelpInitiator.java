package com.pw.behaviours;

import com.pw.agents.TrAgent;
import com.pw.biddingOntology.GetHelp;
import com.pw.biddingOntology.JobInitialPosition;
import com.pw.biddingOntology.PositionInfo;
import com.pw.utils.MessageComparator;
import com.pw.utils.Position;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
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
        java.util.ArrayList<ACLMessage> results = new java.util.ArrayList<>();
        // process all the utility function results
        for (Object proposal : responses) {
            if (((ACLMessage) proposal).getPerformative() == ACLMessage.PROPOSE) {
                results.add((ACLMessage) proposal);
            }
        }

        MessageComparator comparator = new MessageComparator();
        Collections.sort(results, comparator);

        //retrieve the initial cfp
        ContentElement ce = myAgent.getContentManager().extractContent((ACLMessage)(getDataStore().get(CFP_KEY)));

        if(ce instanceof Action && (((Action) ce).getAction()) instanceof GetHelp){
            GetHelp help = (GetHelp)((Action) ce).getAction();
            PositionInfo sourcePosition = help.getCallForProposal().getSrcGom().getPosition();

            String conversation_id = ((ACLMessage)responses.get(0)).getConversationId();
            myAgent.addBehaviour(new StartJobBehaviour(myAgent, conversation_id, (ACLMessage)(getDataStore().get(CFP_KEY))));

            // accept best trNumber proposals
            for (int i = 0; i < this.trNumber; i++) {
                ACLMessage m = results.get(i).createReply();
                m.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                // send info on where the job takes place
                try {
                    JobInitialPosition d = new JobInitialPosition();
                    d.setPosition(sourcePosition);
                    myAgent.getContentManager().fillContent(m, d);
                } catch (Codec.CodecException e) {
                    e.printStackTrace();
                } catch (OntologyException e) {
                    e.printStackTrace();
                }

                acceptances.add(m);
                System.out.println("ACCEPT REPLY FROM "+myAgent.getLocalName());
            }
        }
    }
}