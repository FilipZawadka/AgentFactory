package com.pw.scenerios;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Scenario {
    protected final Integer boardWidth;
    protected final Integer boardHeight;

    protected final List<GomDefinition> gomDefinitions = new ArrayList<>();

    protected Scenario(Integer boardWidth, Integer boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }
}
