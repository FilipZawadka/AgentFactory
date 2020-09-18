package com.pw.biddingOntology;

import jade.content.AgentAction;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GomJobRequest implements AgentAction {
    private GomInfo from;
    private GomInfo to;
    private MaterialInfo materialInfo;

    public int getTrNumber(){
        return this.materialInfo.getWeight();
    }
}
