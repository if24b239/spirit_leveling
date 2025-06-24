package com.rain.spiritleveling.blocks.entity;

import com.rain.spiritleveling.blocks.AllBlockEntities;
import com.rain.spiritleveling.entities.custom.MeditationMatSitEntity;
import com.rain.spiritleveling.items.AllItems;
import com.rain.spiritleveling.items.recipe.ShapedSpiritInfusionRecipe;
import com.rain.spiritleveling.screens.SpiritInfusionScreenHandler;
import com.rain.spiritleveling.util.ImplementedInventory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MeditationMatEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private MeditationMatSitEntity linkedSitEntity;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);
    public static final int CENTRE_SLOT = 0;
    public static final int WOOD_SLOT = 1;
    public static final int FIRE_SLOT = 2;
    public static final int EARTH_SLOT = 3;
    public static final int METAL_SLOT = 4;
    public static final int WATER_SLOT = 5;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 1; // TODO: temp 256
    private int spiritEnergy = 0;
    private int maxSpiritEnergy;
    private boolean isReceiving = false;

    public MeditationMatEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.MEDITATION_MAT_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> MeditationMatEntity.this.progress;
                    case 1 -> MeditationMatEntity.this.maxProgress;
                    case 2 -> MeditationMatEntity.this.spiritEnergy;
                    case 3 -> MeditationMatEntity.this.maxSpiritEnergy;
                    case 4 -> (MeditationMatEntity.this.isReceiving) ? 1 : 0;
                    default -> throw new IllegalStateException("Unexpected value: " + index);
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> MeditationMatEntity.this.progress = value;
                    case 1 -> MeditationMatEntity.this.maxProgress = value;
                    case 2 -> MeditationMatEntity.this.spiritEnergy = value;
                    case 3 -> MeditationMatEntity.this.maxSpiritEnergy = value;
                    case 4 -> MeditationMatEntity.this.isReceiving = value != 0;
                    default -> throw new IllegalStateException("Unexpected value: " + index);
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }

    public void setLinkedSitEntity(MeditationMatSitEntity entity) {
        linkedSitEntity = entity;
    }

    public MeditationMatSitEntity getLinkedSitEntity() {
        return linkedSitEntity;
    }

    public void killSitEntity() {
        if (linkedSitEntity == null) return;

        linkedSitEntity.kill();

        linkedSitEntity = null;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("gui.spiritleveling.spirit_infusion");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("meditation_inventory.progress", progress);
        nbt.putInt("meditation_inventory.spiritEnergy", spiritEnergy);
        nbt.putInt("meditation_inventory.maxSpiritEnergy", maxSpiritEnergy);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("meditation_inventory.progress");
        spiritEnergy = nbt.getInt("meditation_inventory.spiritEnergy");
        maxSpiritEnergy = nbt.getInt("meditation_inventory.maxSpiritEnergy");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SpiritInfusionScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
    
    public void flipIsReceiving() {
        this.isReceiving = !isReceiving;
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) return;

        /// CRAFTING LOGIC
        Optional<ShapedSpiritInfusionRecipe> recipe = getCurrentRecipe();
        
        if (recipe.isPresent() && hasRecipe(recipe.get())) {

            if (increaseProgress() && hasEnoughEnergy(recipe.get())) {
                craftItem(recipe.get());
                resetProgress();
            }
        } else {
            resetProgress();
        }

        /// SPIRIT POWER LOGIC
        if (linkedSitEntity != null) {

        }

        // save both the increased progress and the crafted item
        markDirty(world, pos, state);
    }

    private void craftItem(ShapedSpiritInfusionRecipe recipe) {

        // remove item from stack only if they aren't air
        for (int i = WOOD_SLOT; i < inventory.size(); i++) {
            if (this.getStack(i).getItem() != Items.AIR)
                decrementStack(i);
        }

        // add the recipe result into the output slot
        increaseStack(recipe);
    }

    private void increaseStack(ShapedSpiritInfusionRecipe recipe) {
        ItemStack stack = this.getStack(CENTRE_SLOT).copy();

        ItemStack output = recipe.craft(new SimpleInventory(), null);

        if (!canInsertItemIntoOutputSlot(output))
            throw new IllegalStateException("Can't insert crafting result into output slot in spirit infusion");

        if (stack.isEmpty()) {
            this.setStack(CENTRE_SLOT, output);
        } else {
            stack.increment(output.getCount());
            this.setStack(CENTRE_SLOT, stack);
        }
    }

    // make sure the items with remainder keep it in the slots
    private void decrementStack(int slot) {
        ItemStack stack = this.inventory.get(slot).copy();
        if (stack.getItem().hasRecipeRemainder()) {
            this.setStack(slot, stack.getRecipeRemainder());
            return;
        }

        this.removeStack(slot, 1);
    }

    private void resetProgress() {
        progress = 0;
    }

    private boolean hasEnoughEnergy(ShapedSpiritInfusionRecipe recipe) {
        //return spiritEnergy >= recipe.getCost();
        return true; // TODO: temporary
    }

    private boolean increaseProgress() {
        return progress++ >= maxProgress;
    }

    private boolean hasRecipe(ShapedSpiritInfusionRecipe recipe) {
        return canInsertItemIntoOutputSlot(recipe.getOutput(null));
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {

        return (this.getStack(CENTRE_SLOT).getItem() == output.getItem()
                && this.getStack(CENTRE_SLOT).getCount() + output.getCount() <= this.getStack(CENTRE_SLOT).getMaxCount())
                || this.getStack(CENTRE_SLOT).isEmpty();
    }

    private Optional<ShapedSpiritInfusionRecipe> getCurrentRecipe() {
        SimpleInventory inv = new SimpleInventory(this.size());
        for (int i = 0; i < inv.size(); i++) {
            inv.setStack(i, this.getStack(i));
        }

        assert getWorld() != null;
        return getWorld().getRecipeManager().getFirstMatch(ShapedSpiritInfusionRecipe.Type.INSTANCE, inv, getWorld());
    }

}
