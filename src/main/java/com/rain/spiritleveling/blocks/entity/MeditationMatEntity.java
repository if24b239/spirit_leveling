package com.rain.spiritleveling.blocks.entity;

import com.rain.spiritleveling.api.ISpiritEnergyPlayer;
import com.rain.spiritleveling.blocks.AllBlockEntities;
import com.rain.spiritleveling.entities.custom.MeditationMatSitEntity;
import com.rain.spiritleveling.items.recipe.ShapedSpiritInfusionRecipe;
import com.rain.spiritleveling.screens.SpiritInfusionScreenHandler;
import com.rain.spiritleveling.util.ImplementedInventory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class MeditationMatEntity extends SpiritEnergyStorageBlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private MeditationMatSitEntity linkedSitEntity;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);
    public static final int CENTRE_SLOT = 0;
    public static final int WOOD_SLOT = 1;
    public static final int FIRE_SLOT = 2;
    public static final int EARTH_SLOT = 3;
    public static final int METAL_SLOT = 4;
    public static final int WATER_SLOT = 5;

    protected final PropertyDelegate propertyDelegate;
    private int infusionProgress = 0;
    private int maxInfusionProgress = 0;
    private boolean isReceiving = false;
    private int receivingCooldown = 0;

    public MeditationMatEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.MEDITATION_MAT_ENTITY, pos, state, 4);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> MeditationMatEntity.this.infusionProgress;
                    case 1 -> MeditationMatEntity.this.maxInfusionProgress;
                    case 2 -> MeditationMatEntity.this.getConnectedCurrentEnergy();
                    case 3 -> MeditationMatEntity.this.getConnectedMaxEnergy();
                    case 4 -> (MeditationMatEntity.this.isReceiving) ? 1 : 0;
                    case 5 -> ((ISpiritEnergyPlayer) Objects.requireNonNull(MeditationMatEntity.this.getLinkedSitEntity().getFirstPassenger())).spirit_leveling$getSpiritPower();
                    default -> throw new IllegalStateException("Unexpected value: " + index);
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> MeditationMatEntity.this.infusionProgress = value;
                    case 1 -> MeditationMatEntity.this.maxInfusionProgress = value;
                    case 4 -> MeditationMatEntity.this.isReceiving = value != 0;
                    default -> throw new IllegalStateException("Unexpected value: " + index);
                }
            }

            @Override
            public int size() {
                return 6;
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
        nbt.putInt("meditation_inventory.infusionProgress", infusionProgress);
        nbt.putBoolean("meditation_inventory.isReceiving", isReceiving);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        infusionProgress = nbt.getInt("meditation_inventory.infusionProgress");
        isReceiving = nbt.getBoolean("meditation_inventory.isReceiving");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SpiritInfusionScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) return;

        /// CRAFTING LOGIC
        Optional<ShapedSpiritInfusionRecipe> recipe = getCurrentRecipe();

        // start the crafting only with matching recipe and enough spiritEnergy otherwise reset the progress
        if (recipe.isPresent() && hasRecipe(recipe.get()) && hasEnoughEnergy(recipe.get())) {

            updateMaxProgress(recipe.get());
            if (increaseProgress()) {
                craftItem(recipe.get());
                resetProgress();
            }
        } else {
            resetProgress();
        }

        /// SPIRIT POWER LOGIC

        if (linkedSitEntity != null && ++receivingCooldown % 25 == 0) {
            Entity passenger = linkedSitEntity.getFirstPassenger();

            if (isReceiving) {
                blockReceive(passenger);
            } else {
                entityAbsorb(passenger);
            }

            receivingCooldown = 0;
        }

        // save both the increased progress and the crafted item
        markDirty(world, pos, state);
    }

    private void updateMaxProgress(ShapedSpiritInfusionRecipe recipe) {
        this.maxInfusionProgress = recipe.getMaxProgress();
    }

    private void blockReceive(Entity passenger) {
        if (!(passenger instanceof ISpiritEnergyPlayer newPassenger)) return;

        // prevent overflow of block entity
        if (getConnectedCurrentEnergy() >= getConnectedMaxEnergy())
            return;

        int passenger_spirit_power = newPassenger.spirit_leveling$getSpiritPower();

        if (newPassenger.spirit_leveling$removeCurrentSpiritEnergy((int) Math.pow(2.2, passenger_spirit_power))) {
            addSpiritEnergy(passenger_spirit_power);
        }
    }

    private void entityAbsorb(Entity passenger) {
        if (!(passenger instanceof ISpiritEnergyPlayer newPassenger)) return;

        // prevent overflow of entity
        if(newPassenger.spirit_leveling$isAtMax())
            return;

        newPassenger.spirit_leveling$addCurrentSpiritEnergy(removeSpiritEnergy(getMatLevel()));
    }

    ///  returns amount of spirit energy removed
    private int removeSpiritEnergy(int level) {
        int amount = (int) Math.pow(2.2f, level);

        return removeCurrentEnergy(amount);
    }

    private void addSpiritEnergy(int level) {
        addCurrentEnergy((int) Math.pow(2.2, level));
    }

    private int getMatLevel() {
        return (int) Math.log10(getConnectedMaxEnergy() - 1);
    }

    /// remove ingredients and spirit energy and add the result into the output slot
    private void craftItem(ShapedSpiritInfusionRecipe recipe) {

        // remove item from stack only if they aren't air
        for (int i = WOOD_SLOT; i < inventory.size(); i++) {
            if (this.getStack(i).getItem() != Items.AIR)
                decrementStack(i);
        }

        // remove spirit energy
        removeCurrentEnergy(recipe.getCost());

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
        infusionProgress = 0;
    }

    private boolean hasEnoughEnergy(ShapedSpiritInfusionRecipe recipe) {
        return getCurrentEnergy() >= recipe.getCost();
    }

    private boolean increaseProgress() {
        return ++infusionProgress >= maxInfusionProgress;
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

    public void flipIsReceiving() {
        isReceiving = !isReceiving;
        markDirty();
    }

    ///  make meditation mats only check for block directly underneath it that are not other meditation mats
    @Override
    public Collection<SpiritEnergyStorageBlockEntity> getNeighbors() {
        Collection<SpiritEnergyStorageBlockEntity> allNeighbors = new ArrayList<>();

        assert this.getWorld() != null;
        BlockEntity neighbor = this.getWorld().getBlockEntity(this.getPos().offset(Direction.DOWN));

        if (neighbor instanceof MeditationMatEntity)
            return allNeighbors;

        if (neighbor instanceof SpiritEnergyStorageBlockEntity)
            allNeighbors.add(((SpiritEnergyStorageBlockEntity) neighbor));

        return allNeighbors;
    }
}
