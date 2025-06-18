package com.rain.spiritleveling.energymanager;

import java.util.HashMap;
import java.util.Map;

public enum COVER_STATE {
    NONE(0),
    WEAK(1),
    STRONG(2),
    FULL(3);

    private final int value;
    private static final Map<Integer, COVER_STATE> map = new HashMap<>();

    COVER_STATE(int value) {
        this.value = value;
    }

    static {
        for (COVER_STATE state : COVER_STATE.values()) {
            map.put(state.value, state);
        }
    }

    public static COVER_STATE stateOf(int value) {
        return map.get(value);
    }

    public int getValue() {
        return value;
    }
}
