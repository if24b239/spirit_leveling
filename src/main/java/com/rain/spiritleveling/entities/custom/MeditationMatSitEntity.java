package com.rain.spiritleveling.entities.custom;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.blocks.entity.MeditationMatEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.RideableInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MeditationMatSitEntity extends Entity implements RideableInventory {

    private MeditationMatEntity blockEntity;

    public MeditationMatSitEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        this.kill();
    }

    @Override
    public void stopRiding() {
        this.move(MovementType.SELF, new Vec3d(0, 1.3, 0));
        super.stopRiding();
    }

    @Override
    public void openInventory(PlayerEntity player) {

        // open blockEntity inventory
        blockEntity.openCraftingInventory();
    }

    public void setBlockEntity(MeditationMatEntity entity) {
        blockEntity = entity;
    }
}
