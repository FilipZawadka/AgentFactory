package com.pw.scenerios;

import com.google.common.collect.ImmutableMap;
import com.pw.utils.GomDefinition;
import com.pw.utils.GomDefinition.MaterialGenerator;
import com.pw.utils.GomProcess;
import com.pw.utils.GomProcess.GomProcessOutput;
import com.pw.utils.Material;
import com.pw.utils.Position;

import static com.google.common.collect.Lists.newArrayList;

public class LinearScenario extends Scenario {
    public LinearScenario() {
        super(16, 16, 1000000);

        Material m1 = Material.builder().name("m1").weight(5).build();
        Material m2 = Material.builder().name("m2").weight(5).build();
        Material m3 = Material.builder().name("m3").weight(5).build();
        Material m4 = Material.builder().name("m4").weight(5).build();

        GomDefinition gom1 = new GomDefinition(new Position(6, 2),
            MaterialGenerator.builder()
                .material(m1)
                .amount(1)
                .interval(5000)
                .build());
        GomDefinition gom2 = new GomDefinition(new Position(6, 14));
        GomDefinition gom3 = new GomDefinition(new Position(12, 2));
        GomDefinition gom4 = new GomDefinition(true, new Position(12, 14));

        gom1.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m1, 1))
            .outputs(newArrayList(GomProcessOutput.builder()
                .material(m2)
                .materialAmount(1)
                .destination(gom2)
                .build()))
            .build());
        gom2.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m2, 1))
            .outputs(newArrayList(GomProcessOutput.builder()
                .material(m2)
                .materialAmount(1)
                .destination(gom3)
                .build()))
            .build());
        gom3.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m3, 1))
            .outputs(newArrayList(GomProcessOutput.builder()
                .material(m4)
                .materialAmount(1)
                .destination(gom4)
                .build()))
            .build());

        this.gomDefinitions.addAll(newArrayList(gom1, gom2, gom3, gom4));
    }
}
