package com.rain.spiritleveling.api;

import java.util.HashMap;
import java.util.Map;

public enum Stages {
    MORTAL(0, "mortal"),
    SPIRIT_CONDENSATION(1, "spirit_condensation"),
    FOUNDATION(2, "foundation"),
    GOLDEN_CORE(3, "golden_core"),
    NASCENT_SPIRIT(4, "nascent_spirit"),
    F(5, "F"),
    G(6, "G");

    private final int value;
    private final String string;
    private static final Map<Integer, Stages> map = new HashMap<>();

    static {
        for (Stages state : Stages.values()) {
            map.put(state.getValue(), state);
        }
    }

    Stages(int value, String string) {
        this.value = value;
        this.string = string;
    }

    public int getValue() {
        return value;
    }

    public String getString() {
        return string;
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
