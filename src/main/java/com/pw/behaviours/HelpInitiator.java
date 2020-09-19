package com.pw.behaviours;

import com.pw.agents.GOTr;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import static com.pw.Factory.CODEC;
import static com.pw.Factory.ONTO;

public class HelpInitiator extends ContractNetInitiator {
    private int trNumber, readyTrs;
    private ArrayList<ACLMessage> proposes;

    public HelpInitiator(Agent a, ACLMessage cfp, int _trNumber) {
        super(a, cfp);
        this.trNumber = _trNumber;
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        super.handleInform(inform);
    }

    @Override
    protected void handleOutOfSequence(ACLMessage msg) {
        if(msg.getPerformative() == ACLMessage.INFORM_IF)
            myAgent.putBack(msg);
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
            for (int i = 0; i < results.size(); i++) {
                ACLMessage m = results.get(i).createReply();
                if(i<this.trNumber)
                    m.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                else
                    m.setPerformative(ACLMessage.REJECT_PROPOSAL);
                m.setLanguage(CODEC.getName());
                m.setOntology(ONTO.getName());
                // send info on where the job takes place
                try {
                    JobInitialPosition d = new JobInitialPosition();
                    d.setPosition(sourcePosition);
                    myAgent.getContentManager().fillContent(m, d);
                    System.out.println("ACCEPT REPLY "+m.getInReplyTo());
                } catch (Codec.CodecException e) {
                    e.printStackTrace();
                } catch (OntologyException e) {
                    e.printStackTrace();
                }

                acceptances.add(m);

            }
        }
    }
}