package com.pw.scenerios;

import com.google.common.collect.ImmutableMap;
import com.pw.utils.GomProcess;
import com.pw.utils.Material;
import com.pw.utils.Position;

import static com.google.common.collect.Lists.newArrayList;

public class Scenerio1 extends Scenario {
    public Scenerio1() {
        super(16, 16);

        GomDefinition initialGom = new GomDefinition(true, false, new Position(0, 2));
        GomDefinition gom1 = new GomDefinition(new Position(6, 2));
        GomDefinition gom2 = new GomDefinition(new Position(6, 14));
        GomDefinition gom3 = new GomDefinition(new Position(12, 2));
        GomDefinition gom4 = new GomDefinition(new Position(12, 14));
        GomDefinition finalGom = new GomDefinition(false, true, new Position(15, 12));

        Material m1 = Material.builder().name("m1").weight(5).build();
        Material m2 = Material.builder().name("m2").weight(5).build();
        Material m3 = Material.builder().name("m3").weight(5).build();
        Material m4 = Material.builder().name("m4").weight(5).build();
        Material m5 = Material.builder().name("m5").weight(5).build();

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
