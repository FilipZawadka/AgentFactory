package com.pw.scenerios;

import java.util.ArrayList;
import java.util.List;

import com.pw.utils.GomDefinition;

import lombok.Getter;

@Getter
public abstract class Scenario {
	protected final Integer boardWidth;
	protected final Integer boardHeight;

	protected final List<GomDefinition> gomDefinitions = new ArrayList<>();
	protected final Integer trBreakContractValue;

	protected Scenario(Integer boardWidth, Integer boardHeight, Integer trBreakContractValue) {
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
		this.trBreakContractValue = trBreakContractValue;
	}
}
