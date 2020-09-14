package com.pw.scenerios;

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

        initialGom.addNextGomTarget(gom1);
        gom1.addNextGomTarget(gom2);
        gom2.addNextGomTarget(gom3);

        this.gomDefinitions.addAll(newArrayList(initialGom, gom1, gom2, gom3, gom4, finalGom));
    }
}
