package com.pw.utils;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class GomDefinition {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final List<MaterialGenerator> materialGenerators = new ArrayList<>();
    private final Position position;
    private final int number;

    private final List<GomProcess> processes = new ArrayList<>();

    public GomDefinition(Position position, MaterialGenerator... materialGenerators) {
        this.position = position;
        this.materialGenerators.addAll(Arrays.asList(materialGenerators));

        this.number = COUNTER.getAndIncrement();
    }

    public void addProcess(GomProcess process) {
        this.processes.add(process);
    }

    public Position getPosition() {
        return new Position(this.position);
    }

    @Data
    @Builder
    public static class MaterialGenerator {
        private final Material material;
        private final Integer amount;
        private final Integer interval;
    }
}
