package com.pw.agents;

import com.pw.behaviours.HelpResponder;
import com.pw.behaviours.SendMaterialResponder;
import com.pw.biddingOntology.*;
import com.pw.board.Board;
import com.pw.utils.Distance;
import com.pw.utils.NeighborPosition;
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
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Random;

import static com.pw.Factory.CFP_ID_COUNTER;
import static com.pw.utils.Naming.GOM;

@Getter
public class TrAgent extends Agent {
    public Codec codec = new SLCodec();
    public Ontology onto = BiddingOntology.getInstance();

    private String id;
    private Position position;
    private Board board;
    private Boolean busy;
    private Integer timeOfInactivity;
    private ArrayList<JobInitialPosition> destinations;
    private Integer tokens;
    private Integer breakContractValue;
    private long lastActiveTime;

    @SneakyThrows
    public void setPosition(Position _position) {
        position = _position;
        //Thread.sleep(100);
        //board.updateGUI();
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
        lastActiveTime = System.currentTimeMillis();
        this.tokens = 0;
        unpackArguments();

        this.board.TrList.add(this);
        addToDf();

        getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL);
        getContentManager().registerOntology(onto);

        addHelpRespondingBehavior();
        addGomRespondingAndHelpRequestBehaviors();
        addDestinationsCheckingBehavior();
    }

    private void unpackArguments(){
        Object[] args = getArguments();
        this.id = args[0].toString();
        this.board = (Board) args[1];
        this.position = (Position) args[2];
        this.breakContractValue = (Integer) args[3];
    }

    public float utilityFunction(int offeredTokens,float deliveryLength,boolean itsMyGom){
        float inactivityParameter =1;
        float deliveryLengthParameter = 1;
        float loyaltyParameter = 0;
        float offeredTokensParameter = 1;
        if (itsMyGom) {
            loyaltyParameter = 20;
        }
        return ((inactivityParameter * timeOfInactivity) + loyaltyParameter +(offeredTokens*offeredTokensParameter)- (deliveryLengthParameter * deliveryLength));

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
            for (int i = 0; i < responders.size(); ++i) {
                if (!responders.get(i).equals(getAID()))
                    cfp.addReceiver(responders.get(i));
            }

            cfp.setOntology(onto.getName());
            cfp.setLanguage(codec.getName());
            cfp.setConversationId("cfp"+CFP_ID_COUNTER.getAndIncrement());

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
            System.out.println("CFP FROM "+getLocalName());
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
        double distance = Distance.absolute(gomRequest.getFrom().getPosition(), gomRequest.getTo().getPosition());

        return (int)(distance);
    }

    public void lock(){
        this.busy = true;
    }

    public void addTokens(Integer tokens){
        this.tokens += tokens;
    }

    public void release(){
        this.busy = false;
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
    public boolean isPositionFree(Position position){
        for (GomAgent a: board.GomList){
            if(Distance.isEqual(a.getPosition(),position)){
                return true;
            }
        }
        for (TrAgent a: board.TrList){
            if(Distance.isEqual(a.getPosition(),position)){
                return false;
            }
        }
        for (GOTr a: board.GOTrList){
            if(Distance.isEqual(a.getPosition(),position)){
                return false;
            }
        }
        return true;
    }

    public void goTo(Position dest) {
        while(!Distance.isEqual(position,dest)) {
            boolean blocked = true;
            while (position.getX() < dest.getX() && isPositionFree(NeighborPosition.getRightPosition(position))) {
                moveRight();
                blocked = false;
            }
            while (position.getX() > dest.getX() && isPositionFree(NeighborPosition.getLeftPosition(position))) {
                moveLeft();
                blocked = false;
            }
            while (position.getY() < dest.getY() && isPositionFree(NeighborPosition.getUpPosition(position))) {
                moveUp();
                blocked = false;
            }
            while (position.getY() > dest.getY() && isPositionFree(NeighborPosition.getDownPosition(position))) {
                moveDown();
                blocked = false;
            }
            if (blocked){
                switch((int)(Math.random()*4)){
                    case 0:
                        if(isPositionFree(NeighborPosition.getRightPosition(position))){
                            moveRight();
                            break;
                        }
                    case 1:
                        if(isPositionFree(NeighborPosition.getLeftPosition(position))){
                            moveLeft();
                            break;
                        }
                    case 2:
                        if(isPositionFree(NeighborPosition.getUpPosition(position))){
                            moveUp();
                            break;
                        }
                    default:
                        if(isPositionFree(NeighborPosition.getDownPosition(position))){
                            moveDown();
                            break;
                        }
                }
            }
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
            System.out.println(getLocalName()+" ADDED TO DESTINATIONS: "+destination.getPosition().toString());
        }
    }

    private void addGomRespondingAndHelpRequestBehaviors() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.and(
                        MessageTemplate.MatchOntology(onto.getName()),
                        MessageTemplate.and(
                                MessageTemplate.MatchSender(new AID(GOM(Integer.parseInt(this.id)), AID.ISLOCALNAME)),
                        MessageTemplate.MatchLanguage(codec.getName()))));

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
                        timeOfInactivity = (int)((System.currentTimeMillis() - lastActiveTime)*0.001);
                    } else {
                        lock();
                        timeOfInactivity = 0;
                        lastActiveTime = System.currentTimeMillis();
                        JobInitialPosition destination = destinations.get(0);
                        Position destinationPosition = new Position(destination.getPosition().getX(), destination.getPosition().getY());
                        System.out.println(getLocalName()+" GO TO DESTINATION "+" "+destinationPosition.toString());
                        goTo(destinationPosition);

                        // info on reached job starting position
                        ACLMessage result = new ACLMessage(ACLMessage.INFORM_IF);
                        result.addReceiver(destination.getSender());
                        result.setConversationId(destination.getConversation());
                        result.setOntology(onto.getName());
                        result.setLanguage(codec.getName());
                        send(result);
                        System.out.println("INFORM at "+((TrAgent)myAgent).getPosition().toString()+" : " + super.myAgent.getName()+result);

                        destinations.remove(0);
                    }
                }
            }
        });
    }
}
