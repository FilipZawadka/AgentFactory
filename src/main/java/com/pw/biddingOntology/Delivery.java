package com.pw.biddingOntology;

import jade.content.AgentAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Delivery implements AgentAction {
    private MaterialInfo material;
}
