package behaviours;

import agents.TR;
import biddingOntology.Destination;
import biddingOntology.PositionInfo;
import biddingOntology.Proposal;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import utils.MessageComparator;

import java.util.Collections;
import java.util.Vector;

public class HelpInitiator extends ContractNetInitiator {
    private int trNumber;

    public HelpInitiator(Agent a, ACLMessage cfp, int _trNumber) {
        super(a, cfp);

        this.trNumber = _trNumber;
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        System.out.println("HANDLE RESPONSES");
        java.util.ArrayList<ACLMessage> results = new java.util.ArrayList<>();
        // process all the utility function results
        for(Object proposal:responses){
            if(((ACLMessage)proposal).getPerformative()==ACLMessage.PROPOSE){
                results.add((ACLMessage)proposal);
            }
        }
        MessageComparator comparator = new MessageComparator();
        Collections.sort(results, comparator);
        // accept best trNumber proposals
        for(int i=0;i<this.trNumber;i++){
            ACLMessage m = results.get(i).createReply();
            m.setPerformative(ACLMessage.ACCEPT_PROPOSAL);

            try {
                Destination d = new Destination();
                d.setPosition(new PositionInfo(((TR)myAgent).getPosition().getX(), ((TR)myAgent).getPosition().getY()));
                myAgent.getContentManager().fillContent(m, d);
            } catch (Codec.CodecException e) {
                e.printStackTrace();
            } catch (OntologyException e) {
                e.printStackTrace();
            }

            acceptances.add(m);
            System.out.println("ACCEPT REPLY: "+m);
        }
    }
}
