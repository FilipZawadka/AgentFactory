package com.pw.biddingOntology;

import jade.content.Concept;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MaterialInfo implements Concept {
    private String name;
    private Integer amount;
    private Integer weight;
}
