package com.rain.spiritleveling.screens;

import com.rain.spiritleveling.api.Stages;
import com.rain.spiritleveling.blocks.entity.MeditationMatEntity;
import com.rain.spiritleveling.networking.AllMessages;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class SpiritInfusionScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final MeditationMatEntity blockEntity;

    public SpiritInfusionScreenHandler(int syncID, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncID, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()),
                new ArrayPropertyDelegate(6));
    }

    public SpiritInfusionScreenHandler(int syncID, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate propertyDelegate) {
        super(AllScreenHandlers.SPIRIT_INFUSION, syncID);
        checkSize((Inventory)blockEntity, 6);

        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = propertyDelegate;
        this.blockEntity = (MeditationMatEntity) blockEntity;


        // set the slots
        this.addSlot(new Slot(inventory, MeditationMatEntity.CENTRE_SLOT, 80,48) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        }); // CENTRE_SLOT
        this.addSlot(new Slot(inventory, MeditationMatEntity.WOOD_SLOT, 80,8)); // WOOD_SLOT
        this.addSlot(new Slot(inventory, MeditationMatEntity.FIRE_SLOT, 122,39)); // FIRE_SLOT
        this.addSlot(new Slot(inventory, MeditationMatEntity.EARTH_SLOT, 106,87)); // EARTH_SLOT
        this.addSlot(new Slot(inventory, MeditationMatEntity.METAL_SLOT, 54,87)); // METAL_SLOT
        this.addSlot(new Slot(inventory, MeditationMatEntity.WATER_SLOT, 38,39)); // WATER_SLOT

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(this.propertyDelegate);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory inventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 109  + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory inventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 167));
        }
    }

    public double getRelativeProgress() {
        if (propertyDelegate.get(1) == 0) {
            return 0;
        }

        return (double) propertyDelegate.get(0) / propertyDelegate.get(1);
    }

    public int getCurrentEnergy() {
        return propertyDelegate.get(2);
    }

    public int getMaxEnergy() {
        return propertyDelegate.get(3);
    }

    public boolean getIsReceiving() {
        return propertyDelegate.get(4) != 0;
    }

    public Stages getPassengerPower() {
        return Stages.stateOf(propertyDelegate.get(5));
    }

    public void flipIsReceiving() {
        PacketByteBuf buf = PacketByteBufs.create().writeBlockPos(blockEntity.getPos());
        ClientPlayNetworking.send(AllMessages.FLIP_IS_RECEIVING, buf);
    }
}
