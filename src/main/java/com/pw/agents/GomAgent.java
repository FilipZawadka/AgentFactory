package com.pw.agents;

import com.pw.behaviours.GomProcessingBehavior;
import com.pw.biddingOntology.BiddingOntology;
import com.pw.board.BoardObject;
import com.pw.scenerios.GomDefinition;
import com.pw.utils.Material;
import com.pw.utils.Position;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class GomAgent extends Agent implements BoardObject {
    private Codec codec = new SLCodec();
    private Ontology onto = BiddingOntology.getInstance();

    private final Map<Material, Integer> materials = new ConcurrentHashMap<>();

    private GomDefinition definition;
    private AID gomId;

    @Override
    protected void setup() {
        super.setup();

        Object[] args = getArguments();
        this.definition = (GomDefinition) args[0];

        addBehaviour(new GomProcessingBehavior(this, 1000));
    }

    public AID getGomId() {
        return gomId;
    }

    public void setGomId(AID gomId) {
        this.gomId = gomId;
    }

    @Override
    public void setPosition(Position position) {
        throw new RuntimeException("GoM's position cannot be changed!");
    }

    @Override
    public Position getPosition() {
        return definition.getPosition();
    }

    @Override
    public String getId() {
        return "" + definition.getNumber();
    }


}
