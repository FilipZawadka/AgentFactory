package com.pw.behaviours;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import com.pw.agents.GOTr;
import com.pw.agents.TrAgent;
import com.pw.biddingOntology.BiddingOntology;
import com.pw.biddingOntology.CallForProposal;
import com.pw.biddingOntology.Delivery;
import com.pw.biddingOntology.GetHelp;
import com.pw.biddingOntology.GomInfo;
import com.pw.biddingOntology.JobInitialPosition;
import com.pw.biddingOntology.MaterialInfo;
import com.pw.biddingOntology.PositionInfo;
import com.pw.utils.Position;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;

public class StartJobBehaviour extends SimpleBehaviour {
	private final Codec codec = new SLCodec();
	private final Ontology onto = BiddingOntology.getInstance();
	private final MessageTemplate mt;
	private final List<TrAgent> trAgents;
	private Integer trNumber, tokens;
	private PositionInfo start, end;
	private State state;
	private GomInfo destinationGom;
	private MaterialInfo material;

	@SneakyThrows
	public StartJobBehaviour(Agent a, String conversation_id, JobInitialPosition destination, ACLMessage cfp) {
		super(a);

		this.mt = MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.INFORM_IF),
				MessageTemplate.and(
						MessageTemplate.MatchOntology(onto.getName()),
						MessageTemplate.and(
								MessageTemplate.MatchLanguage(codec.getName()),
								MessageTemplate.MatchConversationId(conversation_id))));

		ContentElement ce = myAgent.getContentManager().extractContent(cfp);
		if (ce instanceof Action && ((Action) ce).getAction() instanceof GetHelp) {
			GetHelp help = (GetHelp) ((Action) ce).getAction();
			CallForProposal cfpContent = help.getCallForProposal();
			this.trNumber = cfpContent.getTrNumber();
			this.start = cfpContent.getSrcGom().getPosition();
			this.end = cfpContent.getDestGom().getPosition();
			this.tokens = cfpContent.getTokens();
			this.destinationGom = cfpContent.getDestGom();
			this.material = cfpContent.getMaterial();
		}

		this.state = State.RECEIVE_INFORM;
		this.trAgents = new ArrayList<>();
		//        this.trAgents.add((TrAgent)myAgent);
		if (destination != null)
			((TrAgent) myAgent).addJobPosition(destination);
	}

	@Override
	public void action() {
		ACLMessage inform;
		//        System.out.println(this.state);

		switch (this.state) {
		case RECEIVE_INFORM:
			inform = myAgent.receive(this.mt);
			if (inform != null) {
				TrAgent tr = ((TrAgent) myAgent).getBoard().getTrByAID(inform.getSender());
				System.out.println(myAgent.getLocalName() + " Received, inform from: " + tr.getLocalName());
				if (!trAgents.contains(tr))
					trAgents.add(tr);
				if (trAgents.size() == trNumber) {
					System.out.println("################# RECEIVED ALL INFORMS");
					this.state = State.CREATE_GOTR;
				}
			} else {
				block();
			}
			break;
		case CREATE_GOTR:
			// TODO gotr id
			System.out.println(format("Initializing GoTR: TRs: %s, start: %s, end: %s", trAgents, start, end));
			System.out.println("################# GOTR GO GO GO");
			//                ((TrAgent)myAgent).getBoard().addGOTr(new Position(start), new Position(end), ((TrAgent)myAgent).getId(), trAgents);
			GOTr gotr = new GOTr(new Position(start), ((TrAgent) myAgent).getId(), ((TrAgent) myAgent).getBoard(),
					trAgents, this.tokens);
			gotr.goTo(new Position(end));
			this.state = State.DONE;
			this.trAgents.clear();

			passMaterialsToGom();
			break;
		case DONE:
			break;
		}
	}

	@Override
	public boolean done() {
		return this.state == State.DONE;
	}

	private void passMaterialsToGom() {
		ACLMessage message = createDeliveryMessage();

		myAgent.send(message);
	}

	@SneakyThrows
	private ACLMessage createDeliveryMessage() {
		TrAgent agent = (TrAgent) myAgent;

		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(this.destinationGom.getGomId());
		message.setLanguage(agent.getCodec().getName());
		message.setOntology(agent.getOnto().getName());

		Delivery delivery = new Delivery(this.material);
		Action action = new Action(this.destinationGom.getGomId(), delivery);
		agent.getContentManager().fillContent(message, action);

		return message;
	}

	private enum State {
		RECEIVE_INFORM, CREATE_GOTR, DONE
	}
}
