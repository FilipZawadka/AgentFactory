package com.pw.biddingOntology;

import jade.content.Concept;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CallForProposal implements Concept {
    private Integer proposalId;
    private Integer trNumber;
    private GomInfo destGom, srcGom;
    private Integer tokens;
}
