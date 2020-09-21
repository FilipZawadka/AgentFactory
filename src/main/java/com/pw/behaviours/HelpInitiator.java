package com.pw.behaviours;

import com.pw.agents.GOTr;
import com.pw.agents.TrAgent;
import com.pw.biddingOntology.*;
import com.pw.utils.Distance;
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
    private int trNumber, trNumberTmp;
    private ArrayList<ACLMessage> proposes;

    public HelpInitiator(Agent a, ACLMessage cfp, int _trNumber) {
        super(a, cfp);
        this.trNumber = _trNumber;
        this.trNumberTmp = _trNumber;
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        if(inform.getPerformative() == ACLMessage.INFORM_IF)
            myAgent.putBack(inform);
    }

    @Override
    protected void handleOutOfSequence(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.INFORM_IF)
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

        //retrieve the initial cfp
        ContentElement ce = myAgent.getContentManager().extractContent((ACLMessage) (getDataStore().get(CFP_KEY)));

        if (ce instanceof Action && (((Action) ce).getAction()) instanceof GetHelp) {
            GetHelp help = (GetHelp) ((Action) ce).getAction();
            PositionInfo srcPosition = help.getCallForProposal().getSrcGom().getPosition();
            PositionInfo destPosition = help.getCallForProposal().getDestGom().getPosition();

            ACLMessage ownProposal = createOwnProposal(srcPosition, destPosition);
            results.add(ownProposal);

            MessageComparator comparator = new MessageComparator();
            Collections.sort(results, comparator);

            String conversation_id = ((ACLMessage) responses.get(0)).getConversationId();
            JobInitialPosition destination = null;

            // if there are less proposals than the needed number of trs, reject all and start new cfp
            if(results.size() < this.trNumberTmp){
                this.trNumberTmp = 0;
            }

            // accept best trNumber proposals
            for (int i = 0; i < results.size(); i++) {
                ACLMessage m = results.get(i).createReply();
                if (i < this.trNumberTmp) {
                    if((results.get(i)).equals(ownProposal))
                    {
                        destination = new JobInitialPosition();
                        destination.setPosition(srcPosition);
                        destination.setConversation(conversation_id);
                        destination.setSender(myAgent.getAID());
                        continue;
                    }
                    m.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    try {
                        JobInitialPosition d = new JobInitialPosition();
                        d.setPosition(srcPosition);
                        myAgent.getContentManager().fillContent(m, d);
                        System.out.println("ACCEPT REPLY " + m);
                    } catch (Codec.CodecException e) {
                        e.printStackTrace();
                    } catch (OntologyException e) {
                        e.printStackTrace();
                    }
                }
                else
                    m.setPerformative(ACLMessage.REJECT_PROPOSAL);
                m.setLanguage(CODEC.getName());
                m.setOntology(ONTO.getName());
                // send info on where the job takes place

                acceptances.add(m);

            }
            // start the job or call the cfp again
            if(this.trNumberTmp > 0)
                myAgent.addBehaviour(new StartJobBehaviour(myAgent, conversation_id, destination, (ACLMessage) (getDataStore().get(CFP_KEY))));
            else{
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! CONTRACT BROKEN");
                myAgent.addBehaviour(new HelpInitiator(myAgent, (ACLMessage) getDataStore().get(CFP_KEY), this.trNumber));
            }
        }
    }

    private ACLMessage createOwnProposal(PositionInfo src, PositionInfo dest){
        ACLMessage proposal = new ACLMessage(ACLMessage.PROPOSE);
        proposal.setSender(myAgent.getAID());

        int tokens = Distance.absolute(dest, src);
        float taskDistance = Distance.absolute(new PositionInfo(((TrAgent) myAgent).getPosition()),src);
        float utility = ((TrAgent) myAgent).utilityFunction(tokens,taskDistance,true);
        proposal.setOntology(ONTO.getName());
        proposal.setLanguage(CODEC.getName());

        SendResult sr = new SendResult();
        sr.setResult(utility);

        Action a = new Action(super.myAgent.getAID(), sr);
        try {
            super.myAgent.getContentManager().fillContent(proposal, a);
        } catch (Codec.CodecException cex) {
            cex.printStackTrace();
        } catch (OntologyException oex) {
            oex.printStackTrace();
        }
        return proposal;

    }
}