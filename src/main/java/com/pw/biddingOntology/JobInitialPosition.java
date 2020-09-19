package com.pw.biddingOntology;

import jade.content.Predicate;
import jade.core.AID;
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
    private AID sender;
    private String conversation;
}
