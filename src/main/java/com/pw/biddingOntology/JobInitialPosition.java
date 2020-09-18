package com.pw.biddingOntology;

import jade.content.Predicate;
import jade.lang.acl.ACLMessage;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JobInitialPosition implements Predicate {
    // starting position of the job
    private PositionInfo position;
    // message to reply to when the initial position is reached
    private ACLMessage message;
}
