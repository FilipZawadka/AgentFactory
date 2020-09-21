package com.pw.agents;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pw.behaviours.GomDeliveryListenerBehavior;
import com.pw.behaviours.GomProcessingBehavior;
import com.pw.biddingOntology.BiddingOntology;
import com.pw.board.Board;
import com.pw.utils.GomDefinition;
import com.pw.utils.Material;
import com.pw.utils.Position;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import lombok.Getter;

@Getter
public class GomAgent extends Agent {
	private final Codec codec = new SLCodec();
	private final Ontology onto = BiddingOntology.getInstance();

	private final Map<Material, Integer> materials = new ConcurrentHashMap<>();
	public Board board;
	private GomDefinition definition;
	private Position position;

	protected void setup() {
		super.setup();

		Object[] args = getArguments();
		this.definition = (GomDefinition) args[0];
		this.board = (Board) args[1];

		getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL);
		getContentManager().registerOntology(onto);

		addMaterialGenerationBehaviors();
		addBehaviour(new GomProcessingBehavior(this, 500));
		addBehaviour(new GomDeliveryListenerBehavior(this));

		this.position = this.definition.getPosition();
		this.board.GomList.add(this);
	}

	public void incrementMaterial(Material material, int amount) {
		checkArgument(material != null);

		this.materials.putIfAbsent(material, 0);
		this.materials.put(material, this.materials.get(material) + amount);
	}

	public Position getPosition() {
		return this.definition.getPosition();
	}

	public void setPosition(Position position) {
		throw new RuntimeException("GoM's position cannot be changed!");
	}

	public String getId() {
		return "" + this.definition.getNumber();
	}

	private void addMaterialGenerationBehaviors() {
		this.definition.getMaterialGenerators().forEach(generator -> {
			addBehaviour(new TickerBehaviour(this, generator.getInterval()) {
				@Override
				protected void onTick() {
					//GomAgent agent = (GomAgent) myAgent;
					((GomAgent) myAgent).incrementMaterial(generator.getMaterial(), generator.getAmount());
				}
			});
		});
	}
}
