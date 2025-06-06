package com.rain.spiritleveling.effects;

import com.rain.spiritleveling.SpiritLeveling;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SpiritEffects {

    public static final StatusEffect ACTIVE_SOUL = new ActiveSoul();

    public static void initialize() {

        Registry.register(Registries.STATUS_EFFECT, SpiritLeveling.loc("active_soul"), ACTIVE_SOUL);
    }
}
