package com.pw.scenarios;

import com.pw.utils.Material;
import com.pw.utils.Position;

public class TreeFlowScenario extends Scenario {
    protected TreeFlowScenario(Integer boardWidth, Integer boardHeight, Integer trBreakContractValue) {
        super(16, 16, 1000000);

        Material m1 = Material.builder().name("m1").weight(5).build();
        Material m2 = Material.builder().name("m2").weight(5).build();
        Material m3 = Material.builder().name("m3").weight(5).build();
        Material m4 = Material.builder().name("m4").weight(5).build();
        Material m5 = Material.builder().name("m5").weight(5).build();


        GomDefinition gom1 = new GomDefinition(new Position(2, 15));
        GomDefinition gom2 = new GomDefinition(new Position(2, 7));
        GomDefinition gom3 = new GomDefinition(new Position(2, 1));

        GomDefinition gom4 = new GomDefinition(new Position(6, 11));
        GomDefinition gom5 = new GomDefinition(new Position(6, 3));

        GomDefinition gom6 = new GomDefinition(new Position(14, 7));


    }
}
