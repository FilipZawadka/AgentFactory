package com.pw.scenerios;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Lists.newArrayList;

import com.pw.utils.GomDefinition;
import com.pw.utils.GomDefinition.MaterialGenerator;
import com.pw.utils.GomProcess;
import com.pw.utils.GomProcess.GomProcessOutput;
import com.pw.utils.Material;
import com.pw.utils.Position;

public class OutcastScenario extends Scenario {
	public OutcastScenario() {
		super(16, 16, -10);

		Material m1 = Material.builder().name("m1").weight(3).build();
		Material m2 = Material.builder().name("m2").weight(3).build();
		Material m3 = Material.builder().name("m3").weight(3).build();
		Material m4 = Material.builder().name("m4").weight(3).build();
		Material m5 = Material.builder().name("m5").weight(3).build();
		Material m6 = Material.builder().name("m6").weight(3).build();
		Material m7 = Material.builder().name("m7").weight(3).build();
		Material m8 = Material.builder().name("m8").weight(3).build();

		GomDefinition gom1 = new GomDefinition(new Position(0, 2),
				MaterialGenerator.builder()
						.material(m1)
						.amount(1)
						.interval(7100)
						.build());
		GomDefinition gom2 = new GomDefinition(new Position(4, 2));
		GomDefinition gom3 = new GomDefinition(new Position(8, 2));
		GomDefinition gom4 = new GomDefinition(new Position(12, 2));
		GomDefinition gom5 = new GomDefinition(new Position(12, 14));
		GomDefinition gom6 = new GomDefinition(new Position(10, 2));

		gom1.addProcess(GomProcess.builder()
				.inputMaterials(of(m1, 1))
				.outputs(newArrayList(
						GomProcessOutput.builder()
								.material(m2)
								.materialAmount(1)
								.destination(gom5)
								.build(),
						GomProcessOutput.builder()
								.material(m3)
								.materialAmount(1)
								.destination(gom2)
								.build()))
				.build());
		gom2.addProcess(GomProcess.builder()
				.inputMaterials(of(m3, 1))
				.outputs(newArrayList(
						GomProcessOutput.builder()
								.material(m4)
								.materialAmount(1)
								.destination(gom5)
								.build(),
						GomProcessOutput.builder()
								.material(m5)
								.materialAmount(1)
								.destination(gom3)
								.build()))
				.build());
		gom3.addProcess(GomProcess.builder()
				.inputMaterials(of(m5, 1))
				.outputs(newArrayList(GomProcessOutput.builder()
						.material(m6)
						.materialAmount(1)
						.destination(gom4)
						.build()))
				.build());
		gom4.addProcess(GomProcess.builder()
				.inputMaterials(of(m6, 1))
				.outputs(newArrayList(GomProcessOutput.builder()
						.material(m7)
						.materialAmount(1)
						.destination(gom6)
						.build()))
				.build());
		gom5.addProcess(GomProcess.builder()
				.inputMaterials(of(m2, 1, m4, 1))
				.outputs(newArrayList(GomProcessOutput.builder()
						.material(m8)
						.materialAmount(1)
						.destination(gom6)
						.build()))
				.build());

		this.gomDefinitions.addAll(newArrayList(gom1, gom2, gom3, gom4, gom5, gom6));
	}
}
