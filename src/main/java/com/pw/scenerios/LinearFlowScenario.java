package com.pw.scenerios;

import com.google.common.collect.ImmutableMap;
import com.pw.scenerios.GomDefinition.MaterialGenerator;
import com.pw.utils.GomProcess;
import com.pw.utils.Material;
import com.pw.utils.Position;

import static com.google.common.collect.Lists.newArrayList;

public class LinearFlowScenario extends Scenario {
    public LinearFlowScenario() {
        super(16, 16, 1000000);

        Material m1 = Material.builder().name("m1").weight(5).build();
        Material m2 = Material.builder().name("m2").weight(5).build();
        Material m3 = Material.builder().name("m3").weight(5).build();
        Material m4 = Material.builder().name("m4").weight(5).build();
        Material m5 = Material.builder().name("m5").weight(5).build();

        GomDefinition initialGom = new GomDefinition(new Position(0, 2),
            MaterialGenerator.builder()
                .material(m1)
                .amount(1)
                .interval(5000)
                .build());
        GomDefinition gom1 = new GomDefinition(new Position(6, 2));
        GomDefinition gom2 = new GomDefinition(new Position(6, 14));
        GomDefinition gom3 = new GomDefinition(new Position(12, 2));
        GomDefinition gom4 = new GomDefinition(new Position(12, 14));
        GomDefinition finalGom = new GomDefinition(true, new Position(15, 12));

        initialGom.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m1, 1))
            .outputMaterial(m1)
            .outputMaterialAmount(1)
            .destination(gom1)
            .build());
        gom1.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m1, 1))
            .outputMaterial(m2)
            .outputMaterialAmount(1)
            .destination(gom2)
            .build());
        gom2.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m2, 1))
            .outputMaterial(m3)
            .outputMaterialAmount(1)
            .destination(gom3)
            .build());
        gom3.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m3, 1))
            .outputMaterial(m4)
            .outputMaterialAmount(1)
            .destination(gom4)
            .build());
        gom4.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m4, 1))
            .outputMaterial(m5)
            .outputMaterialAmount(1)
            .destination(finalGom)
            .build());

        this.gomDefinitions.addAll(newArrayList(initialGom, gom1, gom2, gom3, gom4, finalGom));
    }
}