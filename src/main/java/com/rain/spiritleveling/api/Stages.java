package com.rain.spiritleveling.api;

import java.util.HashMap;
import java.util.Map;

public enum Stages {
    MORTAL(0),
    SPIRIT_CONDENSATION(1),
    FOUNDATION(2),
    GOLDEN_CORE(3),
    NASCENT_SPIRIT(4),
    F(5),
    G(6);

    private final int value;
    private static final Map<Integer, Stages> map = new HashMap<>();

    static {
        for (Stages state : Stages.values()) {
            map.put(state.getValue(), state);
        }
    }

    Stages(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getSpiritEnergy() {
        return (int) Math.pow(10, this.value);
    }

    public static Stages stateOf(int value) {
        if (value > map.size())
            throw new IllegalArgumentException("There is no Stage for the value " + value);

        return map.get(value);
    }

    public Stages next() {
        int newStage = this.getValue() + 1;
        if (newStage >= map.size())
            throw new IllegalStateException("There is no next stage to " + this);
        return Stages.stateOf(newStage);
    }
}
