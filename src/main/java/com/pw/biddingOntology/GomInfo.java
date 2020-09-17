package com.pw.biddingOntology;

import jade.content.Concept;
import jade.core.AID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GomInfo implements Concept {
    private AID gomId;
    private PositionInfo position;
}
