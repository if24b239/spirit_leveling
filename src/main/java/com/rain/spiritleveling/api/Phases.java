package com.rain.spiritleveling.api;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Phases {
    NONE(-1, "none", ""),
    WOOD(0, "wood", "☴"),
    FIRE(1, "fire", "☲"),
    EARTH(2, "earth", "☷"),
    METAL(3, "metal", "☰"),
    WATER(4, "water", "☵");

    private final int value;
    private final String name;
    private final String tooltipString;
    private static final Map<Integer, Phases> map = new HashMap<>();

    Phases(int value, String string, @Nullable String tooltipString) {
        this.value = value;
        this.name = string;
        this.tooltipString = tooltipString;
    }

    static {
        for (Phases state : Phases.values()) {
            map.put(state.value, state);
        }
    }

    public static Phases stateOf(int value) {
        return map.get(value);
    }

    @Override
    public String toString() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getTooltipString() {
        return this.tooltipString;
    }

    public static Phases[] safeValues() {
        return Arrays.stream(Phases.values()).filter(phase -> phase != Phases.NONE).toArray(Phases[]::new);
    }
}
