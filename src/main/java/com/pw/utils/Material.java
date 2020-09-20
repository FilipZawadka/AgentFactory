package com.pw.utils;

import com.pw.biddingOntology.MaterialInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Material {
    private String name;
    private Integer weight;

    public Material(MaterialInfo materialInfo) {
        this.name = materialInfo.getName();
        this.weight = materialInfo.getWeight();
    }
}
