package com.pw.scenerios;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Scenario {
    protected final Integer boardWidth;
    protected final Integer boardHeight;

    protected final List<GomDefinition> gomDefinitions = new ArrayList<>();
    protected final Boolean trBreakingContract;

    protected Scenario(Integer boardWidth, Integer boardHeight, Boolean trBreakingContract) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.trBreakingContract = trBreakingContract;
    }
}
