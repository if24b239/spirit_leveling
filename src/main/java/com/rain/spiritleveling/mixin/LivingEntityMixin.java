package com.rain.spiritleveling.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.rain.spiritleveling.util.SpiritAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique
    private static final int regenerationCooldown = 0;

    @ModifyReturnValue( method = "createLivingAttributes", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder addModdedAttributesToBuilder(DefaultAttributeContainer.Builder builder) {
        return builder.add(SpiritAttributes.SPIRIT_POWER)
                .add(SpiritAttributes.SPIRIT_REGENERATION);
    }
}
