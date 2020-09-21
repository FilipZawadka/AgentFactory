package com.pw.behaviours;

import com.pw.agents.TrAgent;
import com.pw.biddingOntology.BiddingOntology;
import com.pw.biddingOntology.CallForProposal;
import com.pw.biddingOntology.GetHelp;
import com.pw.biddingOntology.JobInitialPosition;
import com.pw.biddingOntology.PositionInfo;
import com.pw.biddingOntology.SendResult;
import com.pw.utils.Distance;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SSContractNetResponder;
import lombok.SneakyThrows;

public class HelpResponder extends SSContractNetResponder {
	private final Codec codec = new SLCodec();
	private final Ontology onto = BiddingOntology.getInstance();

	public HelpResponder(Agent a, ACLMessage cfp) {
		super(a, cfp);
	}

	@SneakyThrows
	@Override
	protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
		ContentElement ce = myAgent.getContentManager().extractContent(cfp);
		GetHelp help;
		CallForProposal cfpContent;
		if (ce instanceof Action && ((Action) ce).getAction() instanceof GetHelp) {
			help = (GetHelp) ((Action) ce).getAction();
			cfpContent = help.getCallForProposal();
		} else {
			throw new Exception("ce is not Action or not Gethelp");
		}

		int tokens = Distance.absolute(cfpContent.getDestGom().getPosition(), cfpContent.getSrcGom().getPosition());
		float taskDistance = Distance
				.absolute(new PositionInfo(((TrAgent) myAgent).getPosition()), cfpContent.getSrcGom().getPosition());
		float utility = ((TrAgent) myAgent).utilityFunction(tokens, taskDistance, false);

		ACLMessage reply = cfp.createReply();
		if (((TrAgent) myAgent).getBreakContractValue() != -1 && utility <= ((TrAgent) myAgent)
				.getBreakContractValue()) {
			reply.setPerformative(ACLMessage.REFUSE);
			System.out.println("REFUSE FROM " + myAgent.getLocalName());
			((TrAgent) myAgent).sortDestinations();
			((TrAgent) myAgent).timeOfInactivity += 50;
		} else {
			reply.setPerformative(ACLMessage.PROPOSE);
			System.out.println("PROPOSE FROM " + myAgent.getLocalName());
		}
		reply.setOntology(onto.getName());
		reply.setLanguage(codec.getName());

		SendResult sr = new SendResult();
		sr.setResult(utility);
		System.out.println(utility);

		Action a = new Action(super.myAgent.getAID(), sr);
		try {
			super.myAgent.getContentManager().fillContent(reply, a);
		} catch (Codec.CodecException cex) {
			cex.printStackTrace();
		} catch (OntologyException oex) {
			oex.printStackTrace();
		}

		return reply;
	}

	@Override
	protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
			throws FailureException {
		// save the job destination in the destinations array
		ContentElement ce = null;
		try {
			ce = myAgent.getContentManager().extractContent(accept);
		} catch (Codec.CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}

		if (ce instanceof JobInitialPosition) {
			JobInitialPosition destination = (JobInitialPosition) ce;
			destination.setSender(accept.getSender());
			destination.setConversation(accept.getConversationId());
			((TrAgent) myAgent).addJobPosition(destination);

			ACLMessage result = accept.createReply();
			result.setPerformative(ACLMessage.INFORM);
			result.setConversationId(destination.getConversation());
			result.setOntology(onto.getName());
			return result;
		}

		return null;
	}
}