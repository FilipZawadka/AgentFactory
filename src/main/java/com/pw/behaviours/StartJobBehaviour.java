package com.pw.behaviours;

import com.pw.agents.GOTr;
import com.pw.agents.TrAgent;
import com.pw.biddingOntology.BiddingOntology;
import com.pw.biddingOntology.CallForProposal;
import com.pw.biddingOntology.GetHelp;
import com.pw.biddingOntology.PositionInfo;
import com.pw.utils.Position;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartJobBehaviour extends SimpleBehaviour {
    private Codec codec = new SLCodec();
    private Ontology onto = BiddingOntology.getInstance();
    private MessageTemplate mt;
    private List<TrAgent> trAgents;
    private Integer trNumber;
    private PositionInfo start, end;
    private State state;

    private enum State{
        RECEIVE_INFORM, CREATE_GOTR, DONE
    }

    @SneakyThrows
    public StartJobBehaviour(Agent a, String conversation_id, ACLMessage cfp) {
        super(a);
        System.out.println("@@@@@@@@@@@@@@@@@@@@ START CFP: " + cfp);

        this.mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.and(
                        MessageTemplate.MatchOntology(onto.getName()),
                        MessageTemplate.and(
                                MessageTemplate.MatchLanguage(codec.getName()),
                                MessageTemplate.MatchConversationId(conversation_id))));

        ContentElement ce = myAgent.getContentManager().extractContent(cfp);
        if(ce instanceof Action && ((Action)ce).getAction() instanceof GetHelp){
            GetHelp help = (GetHelp)((Action)ce).getAction();
            CallForProposal cfpContent = help.getCallForProposal();
            this.trNumber = cfpContent.getTrNumber();
            this.start = cfpContent.getSrcGom().getPosition();
            this.end = cfpContent.getDestGom().getPosition();
        }

        this.state = State.RECEIVE_INFORM;
        this.trAgents = new ArrayList<>();
    }

    @Override
    public void action() {
        ACLMessage inform;

        switch(this.state){
            // TODO the tr doesnt receive all of the informs!
            case RECEIVE_INFORM:
                inform = myAgent.receive(this.mt);
                if(inform != null){
                    trAgents.add((TrAgent)(((TrAgent)myAgent).getBoard().getTrByAID(inform.getSender())));
                    if(trAgents.size() == trNumber) {
                        System.out.println("################# RECEIVED ALL INFORMS");
                        this.state = State.CREATE_GOTR;
                    }
                }
                break;
            case CREATE_GOTR:
                // TODO gotr id
                System.out.println("################# GOTR GO GO GO");
                GOTr gotr = new GOTr(new Position(start), ((TrAgent)myAgent).getId(), ((TrAgent)myAgent).getBoard(), trAgents);
                gotr.goTo(new Position(end));
                this.state = State.DONE;
                break;
            default:
                break;
        }
    }

    @Override
    public boolean done() {
        if(this.state == State.DONE)
            return true;
        return false;
    }
}
