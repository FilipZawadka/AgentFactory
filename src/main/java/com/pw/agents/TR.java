package com.pw.agents;

import com.pw.behaviours.HelpResponder;
import com.pw.behaviours.SendMaterialResponder;
import com.pw.biddingOntology.*;
import com.pw.board.Board;
import com.pw.board.BoardObject;
import com.pw.utils.Position;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.Random;

public class TR extends Agent implements BoardObject {
    public Codec codec = new SLCodec();
    public Ontology onto = BiddingOntology.getInstance();

    private String id;
    private GoM myGom;
    private Position position;
    public Board board;
    public Boolean busy;
    public Integer timeOfInactivity;
    public ArrayList<Position> destinations;

    @Override
    public void setPosition(Position _position) {
        position = _position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    protected void setup() {
        super.setup();
        busy = false;
        destinations = new ArrayList<>();
        timeOfInactivity = 0;

        Object[] args = getArguments();
        this.id = args[0].toString();
        board = (Board) args[1];

        // add oneself to the df
        addToDf();

        // info on TR's gom
        // myGom = getMyGom();

        getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL);
        getContentManager().registerOntology(onto);

        // RESPOND TO HELP REQUEST
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

        // SEND HELP REQUESTS
        MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
            MessageTemplate.and(
                MessageTemplate.MatchOntology(onto.getName()),
                MessageTemplate.MatchLanguage(codec.getName())));

        addBehaviour(new SendMaterialResponder(this, mt));

        //Completing destinations
        //wysyłanie wiadomości chyba trzeba dodać
        addBehaviour(new TickerBehaviour(this, 10000) {
            @Override
            protected void onTick() {
                if (!busy) {
                    busy = true;
                    if (destinations.size() == 0) {
                        timeOfInactivity++;
                    } else {
                        timeOfInactivity = 0;
                        goTo(destinations.get(0));
                        destinations.remove(0);
                    }
                }
            }
        });
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

    public int prepareCfp(ACLMessage gomRequest, ACLMessage bid, Agent bidder) {
        ArrayList<AID> responders = new ArrayList<>();

        // to be retrieved from the gomRequest
        int trNumber = 2;
        int tokens = 10;

        GomInfo destGom = new GomInfo();
        destGom.setPosition(new PositionInfo(1, 1));
        destGom.setGomId(new AID("otherGoM", AID.ISLOCALNAME));

        GomInfo srcGom = new GomInfo();
        destGom.setPosition(new PositionInfo(0, 0));
        destGom.setGomId(new AID("GoM" + this.id, AID.ISLOCALNAME));

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
                    bid.addReceiver(responders.get(i));
            }

            bid.setOntology(onto.getName());
            bid.setLanguage(codec.getName());

            GetHelp gh = new GetHelp();
            Proposal prop = new Proposal();

            // to be retrieved from the gomRequest
            prop.setSrcGom(srcGom);
            prop.setDestGom(destGom);
            prop.setProposalId(new Random().nextInt());
            prop.setTrNumber(trNumber);
            prop.setTokens(tokens);
            gh.setProposal(prop);

            Action a = new Action(getAID(), gh);
            try {
                getContentManager().fillContent(bid, a);
            } catch (Codec.CodecException ce) {
                ce.printStackTrace();
            } catch (OntologyException oe) {
                oe.printStackTrace();
            }
            System.out.println(bid);
        }

        return trNumber;
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

    @Override
    protected void takeDown() {
        super.takeDown();
    }


}
