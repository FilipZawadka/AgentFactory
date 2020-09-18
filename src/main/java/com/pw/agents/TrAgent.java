package com.pw.agents;

import com.pw.behaviours.HelpResponder;
import com.pw.behaviours.SendMaterialResponder;
import com.pw.biddingOntology.*;
import com.pw.board.Board;
import com.pw.utils.Distance;
import com.pw.utils.Position;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Random;

@Getter
public class TrAgent extends Agent {
    public Codec codec = new SLCodec();
    public Ontology onto = BiddingOntology.getInstance();

    private String id;
    private GomAgent myGom;
    private Position position;
    private Board board;
    private Boolean busy;
    private Integer timeOfInactivity;
    private ArrayList<JobInitialPosition> destinations;

    public void setPosition(Position _position) {
        position = _position;
    }

    public Position getPosition() {
        return position;
    }

    public String getId() {
        return id;
    }

    protected void setup() {
        super.setup();

        this.busy = false;
        this.destinations = new ArrayList<>();
        this.timeOfInactivity = 0;
        Object[] args = getArguments();
        this.id = args[0].toString();
        this.board = (Board) args[1];
        this.position = (Position) args[2];

        // add oneself to the df
        addToDf();

        getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL);
        getContentManager().registerOntology(onto);

        addHelpRespondingBehavior();
        addGomRespondingAndHelpRequestBehaviors();
        addDestinationsCheckingBehavior();
    }

    public double utilityFunction(double deliveryLength,boolean itsMyGom){
        double inactivityParameter =1;
        double deliveryLengthParameter=0.5;
        double loyaltyParameter = 0;
        if (itsMyGom) {
            loyaltyParameter = 20;
        }
        return ((inactivityParameter * timeOfInactivity) + loyaltyParameter - (deliveryLengthParameter * deliveryLength));

    }

    public void prepareHelpCfp(GomJobRequest gomRequest, ACLMessage cfp, Agent bidder) {
        ArrayList<AID> responders = new ArrayList<>();

        CallForProposal cfpContent = new CallForProposal();
        fillCfpFromGomRequest(cfpContent, gomRequest);

        // get other TRs
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setName("factory1");
        template.addServices(sd);
        try {
            // add them as the receivers of the cfp
            DFAgentDescription[] result = DFService.search(bidder, template);
            responders = new ArrayList<>();
            for (int i = 0; i < result.length; ++i) {
                responders.add(result[i].getName());
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        if (responders.size() > 0) {
            // initialize cfp
            // TODO add oneself to the receivers
            for (int i = 0; i < responders.size(); ++i) {
                if (!responders.get(i).equals(getAID()))
                    cfp.addReceiver(responders.get(i));
            }

            cfp.setOntology(onto.getName());
            cfp.setLanguage(codec.getName());

            GetHelp gh = new GetHelp();
            gh.setCallForProposal(cfpContent);

            Action a = new Action(getAID(), gh);
            try {
                getContentManager().fillContent(cfp, a);
            } catch (Codec.CodecException ce) {
                ce.printStackTrace();
            } catch (OntologyException oe) {
                oe.printStackTrace();
            }
            System.out.println(cfp);
        }
    }

    private void fillCfpFromGomRequest(CallForProposal cfpContent, GomJobRequest gomRequest) {
        cfpContent.setSrcGom(gomRequest.getFrom());
        cfpContent.setDestGom(gomRequest.getTo());
        cfpContent.setProposalId(new Random().nextInt());
        cfpContent.setTrNumber(gomRequest.getTrNumber());
        cfpContent.setTokens(calculateTokensForRequest(gomRequest));
    }

    private Integer calculateTokensForRequest(GomJobRequest gomRequest) {
        double distance = Distance.euclidean(gomRequest.getFrom().getPosition(), gomRequest.getTo().getPosition());
        int weight = gomRequest.getMaterialInfo().getWeight();

        return (int)(distance * weight);
    }

    public void moveUp() {
        if (position.getY() < board.height) {
            position.setY(position.getY() + 1);
        }

    }

    public void moveDown() {
        if (position.getY() > 0) {
            position.setY(position.getY() - 1);
        }
    }

    public void moveLeft() {
        if (position.getX() > 0) {
            position.setX(position.getX() - 1);
        }
    }

    public void moveRight() {
        if (position.getX() < board.width) {
            position.setX(position.getX() + 1);
        }
    }

    public void goTo(Position dest) {
        while (position.getX() < dest.getX()) {
            moveRight();
        }
        while (position.getX() > dest.getX()) {
            moveLeft();
        }
        while (position.getY() < dest.getY()) {
            moveUp();
        }
        while (position.getY() > dest.getY()) {
            moveDown();
        }
    }

    private void addToDf() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("tr");
        sd.setName("factory1");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    public void addJobPosition(JobInitialPosition destination){
        if(!destinations.contains(destination)){
            destinations.add(destination);
            System.out.println("Added: "+destination.toString());
        }
    }

    private void addGomRespondingAndHelpRequestBehaviors() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.and(
                        MessageTemplate.MatchOntology(onto.getName()),
                        MessageTemplate.MatchLanguage(codec.getName())));

        addBehaviour(new SendMaterialResponder(this, mt));
    }

    private void addHelpRespondingBehavior() {
        addBehaviour(new CyclicBehaviour() {
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.CFP),
                    MessageTemplate.and(
                            MessageTemplate.MatchOntology(onto.getName()),
                            MessageTemplate.MatchLanguage(codec.getName())));

            @Override
            public void action() {
                ACLMessage cfp = myAgent.receive(mt);
                if (cfp != null) {
                    myAgent.addBehaviour(new HelpResponder(myAgent, cfp));
                } else {
                    block();
                }
            }
        });
    }

    private void addDestinationsCheckingBehavior() {
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                if (!busy) {
                    if (destinations.size() == 0) {
                        timeOfInactivity++;
                    } else {
                        busy = true;
                        timeOfInactivity = 0;
                        JobInitialPosition destination = destinations.get(0);
                        Position destinationPosition = new Position(destination.getPosition().getX(), destination.getPosition().getY());
                        goTo(destinationPosition);

                        // info on reached job starting position
                        ACLMessage result = (destination.getMessage()).createReply();
                        result.setPerformative(ACLMessage.INFORM);
                        send(result);
                        System.out.println("INFORM at "+((TrAgent)myAgent).getPosition().toString()+" : " + super.myAgent.getName() + result);

                        destinations.remove(0);

                        // TODO do the carrying of the material and put the busy = false there
                        busy = false;
                    }
                }
            }
        });
    }
}
