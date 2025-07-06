package com.rain.spiritleveling.blocks.entity;

import com.rain.spiritleveling.api.Elements;
import com.rain.spiritleveling.api.ISpiritEnergyPlayer;
import com.rain.spiritleveling.api.Stages;
import com.rain.spiritleveling.blocks.AllBlockEntities;
import com.rain.spiritleveling.entities.custom.MeditationMatSitEntity;
import com.rain.spiritleveling.items.custom.CultivationManual;
import com.rain.spiritleveling.items.recipe.ShapedSpiritInfusionRecipe;
import com.rain.spiritleveling.items.recipe.ShapelessSpiritInfusionRecipe;
import com.rain.spiritleveling.items.recipe.SpiritInfusionRecipe;
import com.rain.spiritleveling.screens.SpiritInfusionScreenHandler;
import com.rain.spiritleveling.util.ImplementedInventory;
import com.rain.spiritleveling.util.SpiritTags;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MeditationMatEntity extends SpiritEnergyStorageBlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private MeditationMatSitEntity linkedSitEntity;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);
    public static final int CENTRE_SLOT = 0;
    public static final int WOOD_SLOT = elementsToSlot(Elements.WOOD);
    public static final int FIRE_SLOT = elementsToSlot(Elements.FIRE);
    public static final int EARTH_SLOT = elementsToSlot(Elements.EARTH);
    public static final int METAL_SLOT = elementsToSlot(Elements.METAL);
    public static final int WATER_SLOT = elementsToSlot(Elements.WATER);

    protected final PropertyDelegate propertyDelegate;
    private int infusionProgress = 0;
    private int maxInfusionProgress = 0;
    private boolean isReceiving = false;
    private int receivingCooldown = 0;

    private static int elementsToSlot(Elements element) {
        return element.getValue() + 1;
    }

    private static Elements slotToElements(int slot) {
        return Elements.stateOf(slot - 1);
    }

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
                    case 5 -> ((ISpiritEnergyPlayer) Objects.requireNonNull(MeditationMatEntity.this.getLinkedSitEntity().getFirstPassenger())).spirit_leveling$getSpiritPower().getValue();
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
        Optional<? extends SpiritInfusionRecipe> recipe = getCurrentRecipe();

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

    private void updateMaxProgress(SpiritInfusionRecipe recipe) {
        this.maxInfusionProgress = recipe.getMaxProgress();
    }

    private void blockReceive(Entity passenger) {
        if (!(passenger instanceof ISpiritEnergyPlayer newPassenger)) return;

        // prevent overflow of block entity
        if (getConnectedCurrentEnergy() >= getConnectedMaxEnergy())
            return;

        Stages passenger_spirit_power = newPassenger.spirit_leveling$getSpiritPower();

        if (newPassenger.spirit_leveling$removeCurrentSpiritEnergy(getTransferAmount(passenger_spirit_power))) {
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
    private int removeSpiritEnergy(Stages level) {
        int amount = getTransferAmount(level);

        return removeCurrentEnergy(amount);
    }

    private void addSpiritEnergy(Stages level) {
        addCurrentEnergy(getTransferAmount(level));
    }

    private Stages getMatLevel() {
        return Stages.stateOf((int) Math.log10(getConnectedMaxEnergy() - 1));
    }

    /// remove ingredients and spirit energy and add the result into the output slot
    private void craftItem(SpiritInfusionRecipe recipe) {

        Elements manualElement = Elements.NONE;
        ItemStack manual = null;

        // remove item from stack only if they aren't air
        for (int i = WOOD_SLOT; i < inventory.size(); i++) {
            ItemStack stack = this.getStack(i);
            if (isValidManual(stack, recipe.getTag())) {
                manualElement = slotToElements(i);
                manual = stack.copy();
            }

            if (stack.getItem() != Items.AIR)
                decrementStack(i);
        }

        // remove spirit energy
        removeCurrentEnergy(recipe.getCost());

        // add the recipe result into the output slot while keeping manual durability
        if (manual != null) {
            this.setStack(CENTRE_SLOT, manual);
            CultivationManual.setActiveElement(this.getStack(CENTRE_SLOT), manualElement);
        } else
            increaseStack(recipe);
    }

    // check if the item is a cultivation manual and is not active as well as it being in the tag SPIRIT_CULTIVATION_MANUALS
    private boolean isValidManual(ItemStack stack, TagKey<Item> recipe_tag) {
        if (recipe_tag == null || !stack.isIn(recipe_tag) || !stack.isIn(SpiritTags.Items.SPIRIT_CULTIVATION_MANUALS))
            return false;

        boolean alreadyActive = false;
        if (stack.getNbt() != null && stack.getNbt().contains("activeIndex"))
            alreadyActive = stack.getNbt().getInt("activeIndex") != Elements.NONE.getValue();

        return !alreadyActive;
    }

    private void increaseStack(SpiritInfusionRecipe recipe) {
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

    private boolean hasEnoughEnergy(SpiritInfusionRecipe recipe) {
        return getConnectedCurrentEnergy() >= recipe.getCost();
    }

    private boolean increaseProgress() {
        return ++infusionProgress >= maxInfusionProgress;
    }

    private boolean hasRecipe(SpiritInfusionRecipe recipe) {
        return canInsertItemIntoOutputSlot(recipe.getOutput(null)) && validBreakthroughRecipe(recipe);
    }

    private boolean validBreakthroughRecipe(SpiritInfusionRecipe recipe) {
        // is ignored when the output isn't a cultivation manual
        if (!recipe.getOutput(null).isIn(SpiritTags.Items.SPIRIT_CULTIVATION_MANUALS))
            return true;

        boolean valid = false;
        for (int i = WOOD_SLOT; i <= WATER_SLOT; i++) {
            if (isValidManual(this.getStack(i), recipe.getTag()) && this.getStack(i).isIn(recipe.getTag()))
                valid = true;
        }

        return valid;
    }

    private boolean canInsertItemIntoOutputSlot(@NotNull ItemStack output) {
        return this.getStack(CENTRE_SLOT).isEmpty()
                || (this.getStack(CENTRE_SLOT).getItem() == output.getItem()
                && this.getStack(CENTRE_SLOT).getCount() + output.getCount() <= this.getStack(CENTRE_SLOT).getMaxCount());
    }

    private Optional<? extends SpiritInfusionRecipe> getCurrentRecipe() {
        SimpleInventory inv = new SimpleInventory(this.size());
        for (int i = 0; i < inv.size(); i++) {
            inv.setStack(i, this.getStack(i));
        }

        assert getWorld() != null;
        Optional<ShapedSpiritInfusionRecipe> shapedRecipe = getWorld().getRecipeManager().getFirstMatch(ShapedSpiritInfusionRecipe.Type.INSTANCE, inv, getWorld());

        if (shapedRecipe.isPresent())
            return shapedRecipe;

        return getWorld().getRecipeManager().getFirstMatch(ShapelessSpiritInfusionRecipe.Type.INSTANCE, inv, getWorld());
    }

    public void flipIsReceiving() {
        isReceiving = !isReceiving;
        markDirty();
    }

    /// returns the amount of energy transferred from the player to the mat and vice versa
    public static int getTransferAmount(Stages level) {

        return (int) (1 + (Math.pow(10, level.getValue() + 1) / (60 * (1 + ((double) level.getValue() / 6)))));
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
