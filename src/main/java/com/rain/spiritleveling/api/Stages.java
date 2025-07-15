package com.rain.spiritleveling.api;

import net.minecraft.entity.attribute.EntityAttributeModifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum Stages {
    MORTAL(0, "mortal"),
    SPIRIT_CONDENSATION(1, "spirit_condensation"),
    FOUNDATION(2, "foundation"),
    GOLDEN_CORE(3, "golden_core"),
    NASCENT_SPIRIT(4, "nascent_spirit"),
    F(5, "f"),
    G(6, "g"),
    LAST(7, "PLACEHOLDER");

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

    public static Stages[] safeValues() {
        return Arrays.stream(Stages.values()).filter(stage -> stage != Stages.LAST).toArray(Stages[]::new);
    }

    /**
     * @return EntityAttributeModifier to multiply SpiritPower based on current Spirit Energy
     */
    public EntityAttributeModifier getPowerModifier() {
        return new EntityAttributeModifier(UUID.fromString("fa5e0263-1412-44bf-90f4-1cf1e22a50ca"),"power multiplier", this.value, EntityAttributeModifier.Operation.MULTIPLY_BASE);
    }

    /**
     * @return EntityAttributeModifier to add SpiritPower based on max Spirit Energy
     */
    public EntityAttributeModifier getLevelModifier() {
        return new EntityAttributeModifier(UUID.fromString("087dffe8-6522-4b2e-8ff0-e072736d998f"), "power adder", this.value, EntityAttributeModifier.Operation.ADDITION);
    }


}
