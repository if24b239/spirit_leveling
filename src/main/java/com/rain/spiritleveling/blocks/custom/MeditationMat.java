package com.rain.spiritleveling.blocks.custom;

import com.rain.spiritleveling.blocks.AllBlockEntities;
import com.rain.spiritleveling.blocks.entity.MeditationMatEntity;
import com.rain.spiritleveling.entities.AllEntities;
import com.rain.spiritleveling.entities.custom.MeditationMatSitEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class MeditationMat extends SpiritEnergyBlock {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    private static final VoxelShape SHAPE = Block.createCuboidShape(1, 0, 1, 15, 3.05, 15);

    public MeditationMat(Settings settings) {
        super(settings
                .dynamicBounds()
                .blockVision((state, world, pos) -> false), 4
        );
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MeditationMatEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext cnt) {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext cnt) {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    // get all the items in the inventory and spawn them into the world
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof MeditationMatEntity) {
                ItemScatterer.spawn(world, pos, (MeditationMatEntity) blockEntity);
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    // overwrite on use to let player sit on mat and link the sit entity to the block entity
    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) return ActionResult.SUCCESS;

        MeditationMatEntity blockEntity = ((MeditationMatEntity) world.getBlockEntity(pos));
        MeditationMatSitEntity linkedEntity = null;

        // test if there is already a sit entity linked to the block entity
        if (blockEntity != null)
            linkedEntity = blockEntity.getLinkedSitEntity();

        // create a new sit entity and then link it
        if (linkedEntity == null || !linkedEntity.isAlive()) {
             linkedEntity = AllEntities.MAT_SIT_ENTITY.spawn((ServerWorld) world, pos.add(0, -1, 0), SpawnReason.TRIGGERED);

            assert blockEntity != null;
            blockEntity.setLinkedSitEntity(linkedEntity);

             assert linkedEntity != null;
             linkedEntity.setBlockEntity(blockEntity);
        }

        // ensures only one player can sit on the mat
        if (!linkedEntity.hasPassengers())
            player.startRiding(linkedEntity);

        ((ServerPlayerEntity) player).networkHandler.syncWithPlayerPosition();

        return ActionResult.CONSUME;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);

        MeditationMatEntity blockEntity = ((MeditationMatEntity) world.getBlockEntity(pos));

        if (blockEntity != null)
            blockEntity.killSitEntity();
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, AllBlockEntities.MEDITATION_MAT_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}
