package com.pw.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class GomProcess {
    private Map<Material, Integer> inputMaterials; // amount by Material
    private List<GomProcessOutput> outputs;

    @Getter
    @Setter
    @Builder
    public static class GomProcessOutput {
        private Material material;
        private Integer materialAmount;
        private GomDefinition destination;
    }
}
