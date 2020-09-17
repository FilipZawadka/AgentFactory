package com.pw.biddingOntology;

import jade.content.Concept;
import jade.core.AID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GomInfo implements Concept {
    private AID gomId;
    private PositionInfo position;
}
