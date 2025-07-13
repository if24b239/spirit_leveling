package com.rain.spiritleveling.effects;

import com.rain.spiritleveling.api.ISpiritEnergyPlayer;
import com.rain.spiritleveling.util.SpiritAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;

public class ActiveSoul extends StatusEffect {
    public ActiveSoul() {

        super(StatusEffectCategory.BENEFICIAL, 0x00baed);

        // adds the status effect changing the spirit regeneration attribute
        this.addAttributeModifier(SpiritAttributes.SPIRIT_REGENERATION, "89542a81-53ff-4d83-b106-246816a9f046", 1.0f, EntityAttributeModifier.Operation.ADDITION);
    }

    /*@Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayerEntity) {
            ((ISpiritEnergyPlayer)entity).spirit_leveling$addCurrentSpiritEnergy((int)Math.pow(2, amplifier));
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {

        int interval = 200 >> amplifier;

        return interval == 0 || (duration % interval == 0);
    }*/


}