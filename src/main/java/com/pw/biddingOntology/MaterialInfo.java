package com.pw.biddingOntology;

import jade.content.Concept;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialInfo implements Concept {
    private String name;
    private Integer amount;
    private Integer weight;
}
