package biddingOntology;

import jade.content.Predicate;
import jade.lang.acl.ACLMessage;

public class Destination implements Predicate {
    private ACLMessage message;
    private PositionInfo position;

    public void setPosition(PositionInfo position) {
        this.position = position;
    }

    public PositionInfo getPosition(){
        return this.position;
    }

    public void setMessage(ACLMessage message) {
        this.message = message;
    }

    public ACLMessage getMessage(){
        return this.message;
    }
}
