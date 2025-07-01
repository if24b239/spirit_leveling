package com.rain.spiritleveling.mixin;

import com.rain.spiritleveling.util.SpiritTags;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.BlockState;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MiningToolItem.class)
public class MiningToolItemMixin extends ToolItem {

    public MiningToolItemMixin(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    /// implement NEEDS_NETHERITE_TOOL functionality
    @Inject(method = "isSuitableFor", at = @At("HEAD"), cancellable = true)
    public void isSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        int level = this.getMaterial().getMiningLevel();
        if (level < MiningLevels.NETHERITE && state.isIn(SpiritTags.Blocks.NEEDS_NETHERITE_TOOL)) {
            cir.setReturnValue(false);
        }
    }
}
