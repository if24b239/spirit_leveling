package com.rain.spiritleveling.effects;

import com.rain.spiritleveling.api.SpiritEnergyData;
import com.rain.spiritleveling.util.ISpiritEnergyPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;

public class ActiveSoul extends StatusEffect {
    public ActiveSoul() {

        super(StatusEffectCategory.BENEFICIAL, 0x00baed);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayerEntity) {
            ((ISpiritEnergyPlayer)entity).spirit_leveling$addCurrentSpiritEnergy((int)Math.pow(2, amplifier));
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {

        int interval = 200 >> amplifier;

        return interval == 0 || (duration % interval == 0);
    }

}