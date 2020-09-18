package com.pw.biddingOntology;

import jade.content.AgentAction;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetHelp implements AgentAction {
    private CallForProposal callForProposal;
}
