package com.pw.biddingOntology;

import jade.content.AgentAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetHelp implements AgentAction {
	private CallForProposal callForProposal;
}
