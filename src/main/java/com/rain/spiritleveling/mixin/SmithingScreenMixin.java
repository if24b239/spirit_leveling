package com.rain.spiritleveling.mixin;

import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenMixin extends ForgingScreenHandler {

    public SmithingScreenMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "decrementStack", at = @At("HEAD"), cancellable = true)
    private void decrementStackWithRemainder(int slot, CallbackInfo ci) {
        ItemStack stack = this.input.getStack(slot);
        if (stack.getItem().hasRecipeRemainder()) {
            this.input.setStack(slot, stack.getRecipeRemainder());
            ci.cancel();
        }
    }
}
