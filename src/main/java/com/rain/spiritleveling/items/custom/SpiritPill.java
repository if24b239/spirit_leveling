package com.rain.spiritleveling.items.custom;

import com.rain.spiritleveling.effects.AllEffects;
import com.rain.spiritleveling.api.ISpiritEnergyPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class SpiritPill extends Item {

    private final int PILL_LEVEL;

    public int getSpiritEnergyIncrease() {
        return (int) Math.pow(3, PILL_LEVEL);
    }

    public SpiritPill(Settings settings, int strength) {

        super(settings.food(new FoodComponent.Builder()
                .hunger(4)
                .alwaysEdible()
                .saturationModifier(2)
                .snack()
                .statusEffect(new StatusEffectInstance(AllEffects.ACTIVE_SOUL, 1200, strength), 1.0F)
                .build()));
    
        PILL_LEVEL = strength;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        // run the super function to finish eating
        ItemStack resultingStack = super.finishUsing(stack, world, user);

        // check if user is a player on the server (maybe have to use !world.isClient())?
        if (user instanceof ServerPlayerEntity) {
            // increase the max spirit energy of player
            ((ISpiritEnergyPlayer) user).spirit_leveling$addMaxSpiritEnergy(getSpiritEnergyIncrease());
        }

        // TEMP
        if (user instanceof ServerPlayerEntity)
            ((ISpiritEnergyPlayer)user).spirit_leveling$majorBreakthrough();
        if (user instanceof ServerPlayerEntity)
            ((ISpiritEnergyPlayer)user).spirit_leveling$minorBreakthrough();

        return resultingStack;
    }
}
