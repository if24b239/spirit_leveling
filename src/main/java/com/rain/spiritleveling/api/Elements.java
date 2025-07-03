package com.rain.spiritleveling.api;

import com.rain.spiritleveling.energymanager.COVER_STATE;

import java.util.HashMap;
import java.util.Map;

public enum Elements {
    NONE(-1, ""),
    WOOD(0, "wood"),
    FIRE(1, "fire"),
    EARTH(2, "earth"),
    METAL(3, "metal"),
    WATER(4, "water");

    private final int value;
    private final String name;
    private static final Map<Integer, Elements> map = new HashMap<>();

    Elements(int value, String string) {
        this.value = value;
        this.name = string;
    }

    static {
        for (Elements state : Elements.values()) {
            map.put(state.value, state);
        }
    }

    public static Elements stateOf(int value) {
        return map.get(value);
    }

    @Override
    public String toString() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
