package com.pw.agents;

import com.pw.behaviours.GomProcessingBehavior;
import com.pw.biddingOntology.BiddingOntology;
import com.pw.board.Board;
import com.pw.scenarios.GomDefinition;
import com.pw.utils.Material;
import com.pw.utils.Position;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

@Getter
public class GomAgent extends Agent {
    private final Codec codec = new SLCodec();
    private final Ontology onto = BiddingOntology.getInstance();

    private final Map<Material, Integer> materials = new ConcurrentHashMap<>();

    private GomDefinition definition;
    private Position position;
    public Board board;


    protected void setup() {
        super.setup();

        Object[] args = getArguments();
        this.definition = (GomDefinition) args[0];
        this.board = (Board) args[1];

        getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL);
        getContentManager().registerOntology(onto);

        addMaterialGenerationBehaviors();
        addBehaviour(new GomProcessingBehavior(this, 500));
        if (this.definition.isFinal()) {
            addFinalGomBehavior();
        }
        this.position = definition.getPosition();
        this.board.GomList.add(this);
    }

    public void setPosition(Position position) {
        throw new RuntimeException("GoM's position cannot be changed!");
    }

    public Position getPosition() {
        return definition.getPosition();
    }


    public String getId() {
        return "" + definition.getNumber();
    }

    private void addMaterialGenerationBehaviors() {
        this.definition.getMaterialGenerators().forEach(generator -> {
            addBehaviour(new TickerBehaviour(this, generator.getInterval()) {
                @Override
                protected void onTick() {
                    GomAgent agent = (GomAgent) myAgent;
                    Material material = generator.getMaterial();
                    Integer amount = generator.getAmount();
                    Map<Material, Integer> materials = agent.getMaterials();

                    materials.putIfAbsent(material, 0);
                    materials.put(material, materials.get(material) + amount);
                }
            });
        });
    }

    private void addFinalGomBehavior() {
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                GomAgent agent = (GomAgent) myAgent;
                if (!agent.getMaterials().isEmpty()) {
                    agent.getMaterials().forEach((material, amount) -> {
                        System.out.println(format("%s units of %s arrived to final GOM (#%s)", amount, material, agent.getDefinition().getNumber()));
                    });
                }
            }
        });
    }
}
