package com.pw.scenerios;

import com.google.common.collect.ImmutableMap;
import com.pw.utils.GomDefinition;
import com.pw.utils.GomDefinition.MaterialGenerator;
import com.pw.utils.GomProcess;
import com.pw.utils.GomProcess.GomProcessOutput;
import com.pw.utils.Material;
import com.pw.utils.Position;

import static com.google.common.collect.Lists.newArrayList;

public class TreeFlowScenario extends Scenario {
    public TreeFlowScenario() {
        super(16, 16, -1);

        Material m1 = Material.builder().name("m1").weight(2).build();
        Material m2 = Material.builder().name("m2").weight(2).build();
        Material m3 = Material.builder().name("m3").weight(2).build();
        Material m4 = Material.builder().name("m4").weight(2).build();
        Material m5 = Material.builder().name("m5").weight(2).build();
        Material m6 = Material.builder().name("m6").weight(2).build();

        GomDefinition gom1 = new GomDefinition(new Position(2, 15),
            MaterialGenerator.builder()
                .material(m1)
                .amount(1)
                .interval(10000)
                .build());
        GomDefinition gom2 = new GomDefinition(new Position(2, 7),
            MaterialGenerator.builder()
                .material(m2)
                .amount(1)
                .interval(13000)
                .build(),
            MaterialGenerator.builder()
                .material(m3)
                .amount(1)
                .interval(16000)
                .build());
        GomDefinition gom3 = new GomDefinition(new Position(2, 1),
            MaterialGenerator.builder()
                .material(m4)
                .amount(1)
                .interval(19000)
                .build());

        GomDefinition gom4 = new GomDefinition(new Position(7, 11));
        GomDefinition gom5 = new GomDefinition(new Position(7, 3));

        GomDefinition gom6 = new GomDefinition(new Position(14, 7));

        gom1.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m1, 1))
            .outputs(newArrayList(GomProcessOutput.builder()
                .material(m1)
                .materialAmount(1)
                .destination(gom4)
                .build()))
            .build());
        gom2.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m2, 1))
            .outputs(newArrayList(GomProcessOutput.builder()
                .material(m2)
                .materialAmount(1)
                .destination(gom4)
                .build()))
            .build());
        gom2.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m3, 1))
            .outputs(newArrayList(GomProcessOutput.builder()
                .material(m3)
                .materialAmount(1)
                .destination(gom5)
                .build()))
            .build());
        gom3.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m4, 1))
            .outputs(newArrayList(GomProcessOutput.builder()
                .material(m4)
                .materialAmount(1)
                .destination(gom5)
                .build()))
            .build());

        gom4.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m1, 1, m2, 1))
            .outputs(newArrayList(GomProcessOutput.builder()
                .material(m5)
                .materialAmount(1)
                .destination(gom6)
                .build()))
            .build());
        gom5.addProcess(GomProcess.builder()
            .inputMaterials(ImmutableMap.of(m3, 1, m4, 1))
            .outputs(newArrayList(GomProcessOutput.builder()
                .material(m6)
                .materialAmount(1)
                .destination(gom6)
                .build()))
            .build());

        gom6.addProcess(GomProcess.finalProcess(ImmutableMap.of(m5, 1, m6, 1)));

        this.gomDefinitions.addAll(newArrayList(gom1, gom2, gom3, gom4, gom5, gom6));
    }
}
