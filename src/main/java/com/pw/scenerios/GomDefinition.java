package com.pw.scenerios;

import com.pw.utils.Position;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class GomDefinition {
    private static AtomicInteger counter = new AtomicInteger(0);

    private final boolean isInitial;
    private final boolean isFinal;
    private final Position position;
    private final int number;

    private final List<GomDefinition> outgoingRelations = new ArrayList<>();
    private final List<GomDefinition> incomingRelations = new ArrayList<>();

    public GomDefinition(boolean isInitial, boolean isFinal, Position position) {
        this.isInitial = isInitial;
        this.isFinal = isFinal;
        this.position = position;

        number = counter.getAndIncrement();
    }

    public GomDefinition(Position position) {
        this(false, false, position);
    }

    public void addNextGomTarget(GomDefinition next) {
        this.outgoingRelations.add(next);
        next.incomingRelations.add(this);
    }
}
