package com.pw.biddingOntology;

import jade.content.AgentAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GomJobRequest implements AgentAction {
    private GomInfo from;
    private GomInfo to;
    private MaterialInfo materialInfo;
}
