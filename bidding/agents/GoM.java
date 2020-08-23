package agents;

import behaviours.SendMaterialInitiator;
import biddingOntology.BiddingOntology;
import board.BoardObject;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import utils.Position;

public class GoM extends Agent implements BoardObject {
    public Codec codec = new SLCodec();
    public Ontology onto = BiddingOntology.getInstance();

    private String id;
    private AID gomId;
    private Position position;

    @Override
    protected void setup() {
        super.setup();

        Object[] args = getArguments();

        this.id = args[0].toString();
        this.position = new Position(Integer.parseInt(args[1].toString()), Integer.parseInt(args[2].toString()));

        // for clear output
        if(getLocalName().contains("1")) {
            addBehaviour(new SendMaterialInitiator(this, 10000));
        }
    }

    public AID getGomId() {
        return gomId;
    }

    public void setGomId(AID gomId) {
        this.gomId = gomId;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
