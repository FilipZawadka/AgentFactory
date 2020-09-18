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
//    we carry only one material at a time
//    private Integer amount;
    private Integer weight;
}
