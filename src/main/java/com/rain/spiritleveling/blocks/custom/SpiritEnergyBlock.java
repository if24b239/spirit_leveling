package com.rain.spiritleveling.blocks.custom;

import com.rain.spiritleveling.blocks.entity.SpiritEnergyStorageBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpiritEnergyBlock extends BlockWithEntity implements BlockEntityProvider {

    private final int MAX_STORAGE;

    public SpiritEnergyBlock(Settings settings, int maxStorage) {
        super(settings
                .requiresTool()
                .strength(3.5f, 3f)
        );
        MAX_STORAGE = maxStorage;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {

        return new SpiritEnergyStorageBlockEntity(pos, state, MAX_STORAGE);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (world.isClient()) return;

        SpiritEnergyStorageBlockEntity blockEntity = (SpiritEnergyStorageBlockEntity) world.getBlockEntity((pos));

        if (blockEntity != null)
            blockEntity.markStaleConnectedMax();
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);

        if (world.isClient()) return;

        SpiritEnergyStorageBlockEntity blockEntity = (SpiritEnergyStorageBlockEntity) world.getBlockEntity((pos));

        if (blockEntity != null)
            blockEntity.markStaleConnectedMax();
    }
}
