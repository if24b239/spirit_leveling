package com.rain.spiritleveling.effects;

import com.rain.spiritleveling.util.SpiritAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ActiveSoul extends StatusEffect {
    public ActiveSoul() {

        super(StatusEffectCategory.BENEFICIAL, 0x00baed);

        // adds the status effect changing the spirit regeneration attribute
        this.addAttributeModifier(SpiritAttributes.SPIRIT_REGENERATION, "89542a81-53ff-4d83-b106-246816a9f046", 1.0f, EntityAttributeModifier.Operation.ADDITION);
    }
}