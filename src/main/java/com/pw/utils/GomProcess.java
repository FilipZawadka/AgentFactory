package com.pw.utils;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GomProcess {
	private boolean isFinal = false;
	private Map<Material, Integer> inputMaterials; // amount by Material
	private List<GomProcessOutput> outputs;

	public static GomProcess finalProcess(Map<Material, Integer> inputMaterials) {
		return new GomProcess(true, inputMaterials, null);
	}

	@Getter
	@Setter
	@Builder
	public static class GomProcessOutput {
		private Material material;
		private Integer materialAmount;
		private GomDefinition destination;
	}
}
