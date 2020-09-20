package com.pw.behaviours;

import com.pw.agents.GomAgent;
import com.pw.biddingOntology.Delivery;
import com.pw.utils.Material;
import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;

import static java.lang.String.format;

public class GomDeliveryListenerBehavior extends CyclicBehaviour {

    public GomDeliveryListenerBehavior(Agent a) {
        super(a);
    }

    @SneakyThrows
    @Override
    public void action() {
        GomAgent agent = (GomAgent) myAgent;

        MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
            MessageTemplate.and(
                MessageTemplate.MatchOntology(agent.getOnto().getName()),
                MessageTemplate.MatchLanguage(agent.getCodec().getName())));

        ACLMessage message = myAgent.receive(mt);
        if (message != null) {
            Delivery delivery = parseMessage(message);


            System.out.println(format("%s received %s", agent.getLocalName(), delivery.getMaterial()));
            agent.incrementMaterial(new Material(delivery.getMaterial()), 1);
        } else {
            block();
        }
    }

    @SneakyThrows
    private Delivery parseMessage(ACLMessage message) {
        ContentElement ce = myAgent.getContentManager().extractContent(message);
        if (ce instanceof Action && ((Action) ce).getAction() instanceof Delivery) {
            return (Delivery) ((Action) ce).getAction();
        }
        throw new RuntimeException("ce is neither Action nor Delivery");
    }
}
