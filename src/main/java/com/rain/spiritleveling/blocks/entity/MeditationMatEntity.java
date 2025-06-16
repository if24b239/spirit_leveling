package com.rain.spiritleveling.blocks.entity;

import com.rain.spiritleveling.blocks.AllBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class MeditationMatEntity extends BlockEntity {
    public MeditationMatEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.MEDITATION_MAT_ENTITY, pos, state);
    }

}
