package com.pw.scenarios;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
