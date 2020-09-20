package com.pw.utils;

import com.pw.scenerios.GomDefinition;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class GomProcess {
    private Map<Material, Integer> inputMaterials; // amount by Material
    private Material outputMaterial;
    private Integer outputMaterialAmount;
    private GomDefinition destination;
}
