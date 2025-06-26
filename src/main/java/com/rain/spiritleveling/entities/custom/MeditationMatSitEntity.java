package com.rain.spiritleveling.entities.custom;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.blocks.entity.MeditationMatEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.RideableInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MeditationMatSitEntity extends Entity implements RideableInventory {

    private MeditationMatEntity blockEntity;
    private BlockPos blockPos;

    public MeditationMatSitEntity(EntityType<?> type, World world) {
        super(type, world);
        this.setNoGravity(true);
        this.setInvulnerable(true);
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

        if (nbt.contains("block_pos", NbtElement.LONG_TYPE)) {
            this.blockPos = BlockPos.fromLong(nbt.getLong("block_pos"));
            this.blockEntity = ((MeditationMatEntity) this.getWorld().getBlockEntity(this.blockPos));

            // ensure the block entity is still there and if not kill the entity
            if (this.blockEntity != null) {
                this.blockEntity.setLinkedSitEntity(this);
            } else {
                this.removePassenger(this.getFirstPassenger());
            }
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

        if (blockPos != null) {
            nbt.putLong("block_pos", blockPos.asLong());
        }
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

        if (!(player instanceof ServerPlayerEntity)) return;

        NamedScreenHandlerFactory screenHandlerFactory = blockEntity;

        if (screenHandlerFactory == null) {

            return;
        }

        player.openHandledScreen(screenHandlerFactory);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    public void setBlockEntity(MeditationMatEntity entity) {
        blockEntity = entity;
        blockPos = entity.getPos();
    }
}
