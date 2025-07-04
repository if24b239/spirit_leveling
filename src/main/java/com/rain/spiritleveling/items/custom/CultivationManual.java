package com.rain.spiritleveling.items.custom;

import com.rain.spiritleveling.api.Elements;
import com.rain.spiritleveling.api.ISpiritEnergyPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CultivationManual extends Item {

    private static final int DURABILITY = 8;

    private final List<Identifier> attributes;
    private final List<EntityAttributeModifier> modifiers;

    private final int level;

    /// level has to be at least 1 since 0 does not have breakthroughs
    public CultivationManual(Settings settings, int level, List<Identifier> attributes, List<EntityAttributeModifier> modifiers) {
        super(settings.maxDamage(DURABILITY));

        this.level = level;
        this.attributes = attributes;
        this.modifiers = modifiers;
    }

    public static Elements getActiveElement(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();

        if (nbt == null || !nbt.contains("activeIndex"))
            return Elements.NONE;

        return Elements.stateOf(nbt.getInt("activeIndex"));
    }

    public static void setActiveElement(ItemStack stack, Elements elementIndex) {
        stack.getOrCreateNbt().putInt("activeIndex", elementIndex.getValue());
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return (getActiveElement(stack) == Elements.NONE) ? 0 : 48;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPYGLASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack stack = user.getStackInHand(hand);
        user.setCurrentHand(hand);

        return TypedActionResult.consume(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        Elements activeIndex = getActiveElement(stack);

        if (world.isClient() || activeIndex == Elements.NONE)
            return stack;

        if (user instanceof ServerPlayerEntity && !((ISpiritEnergyPlayer) user).spirit_leveling$minorBreakthrough(this.getLevel()))
            return stack;

        // increase attribute and reset the manual with one durability less
        incrementAttribute(user, activeIndex);
        setActiveElement(stack, Elements.NONE);

        return damageStack(stack);
    }

    private ItemStack damageStack(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamage(copy.getDamage() + 1);

        if (copy.getDamage() >= copy.getMaxDamage())
            return ItemStack.EMPTY;

        return copy;
    }

    /// throws exception when element is Elements.NONE
    private void incrementAttribute(LivingEntity user, Elements element) {
        if (element == Elements.NONE)
            throw new IllegalArgumentException("increment Attribute element can't be Element.NONE");

        // the attribute that will be modified
        EntityAttribute att = Registries.ATTRIBUTE.get(this.attributes.get(element.getValue()));

        // the modifier to change the attribute
        EntityAttributeModifier mod = this.modifiers.get(element.getValue());

        // the instance of the attribute on the user
        EntityAttributeInstance attributeInstance = user.getAttributes().getCustomInstance(att);

        if (attributeInstance == null)
            throw new IllegalStateException("The attribute:" + att + " has no instance on this entity " + user);

        // the modifier already on the user or null if not modified by the Manual yet
        EntityAttributeModifier userMod = attributeInstance.getModifier(mod.getId());

        if (userMod == null) {
            // adds the modifier for the first time
            attributeInstance.addPersistentModifier(mod);
        } else {
            // remove the modifier and replace it by the same one with higher value based on the base modifier
            attributeInstance.removeModifier(mod.getId());
            attributeInstance.addPersistentModifier(new EntityAttributeModifier(
                    userMod.getId(),
                    userMod.getName(),
                    userMod.getValue() + mod.getValue(),
                    userMod.getOperation()
            ));
        }

        // add uuid to the player nbt for display
        if (user instanceof ServerPlayerEntity)
            ((ISpiritEnergyPlayer) user).spirit_leveling$addModifierUUID(mod.getId());
    }

    public int getLevel() {
        return this.level;
    }

    public static class Builder {
        Identifier itemId;

        private final List<Identifier> attributes = new ArrayList<>(Collections.nCopies(5, null));
        private final List<EntityAttributeModifier> modifiers = new ArrayList<>(Collections.nCopies(5, null));

        private int level = 0;

        private Builder(@NotNull Identifier itemId) {
            this.itemId = itemId;
        }

        public static Builder create(@NotNull Identifier itemId) {
            return new Builder(itemId);
        }

        public Builder addAttributeAndModifier(Elements element, @NotNull EntityAttribute attribute, int value, EntityAttributeModifier.Operation operation) {
            validateElement(element);
            validateAttribute(attribute);

            // add the attribute id
            this.attributes.set(element.getValue(), Registries.ATTRIBUTE.getId(attribute));

            // setup and add the attribute modifier
            String uuid_seed = this.itemId.toUnderscoreSeparatedString() + "_" + element + "_" + value;
            String name = this.itemId + ":" + element;

            this.modifiers.set(element.getValue(), new EntityAttributeModifier(
                    UUID.nameUUIDFromBytes(uuid_seed.getBytes(StandardCharsets.UTF_8)),
                    name,
                    value,
                    operation
            ));

            return this;
        }

        public Builder setLevel(int lvl) {
            this.level = lvl;

            return this;
        }

        public CultivationManual build(Settings settings) {
            validateComplete();

            return new CultivationManual(
                    settings,
                    this.level,
                    this.attributes,
                    this.modifiers);
        }

        private void validateElement(Elements element) {
            if (element == Elements.NONE)
                throw new IllegalStateException("Can't set Elements.NONE");

            if (attributes.get(element.getValue()) != null)
                throw new IllegalStateException("The attribute on index " + element + " has already been set");

            if (modifiers.get(element.getValue()) != null)
                throw new IllegalStateException("The modifier for index " + element + " has already been set");
        }

        private void validateAttribute(EntityAttribute attribute) {
            if (Registries.ATTRIBUTE.getId(attribute) == null)
                throw new IllegalArgumentException("The attribute " + attribute + " has not been registered");
        }

        private void validateComplete() {
            for (Identifier i : this.attributes) {
                if (i == null)
                    throw new IllegalStateException("All indices of target attributes in CultivationManual need to be filled");
            }

            for (EntityAttributeModifier i : this.modifiers) {
                if (i == null)
                    throw new IllegalStateException("All modifiers in CultivationManual need to be created correctly");
            }

            if (level < 1)
                throw new IllegalStateException("Level needs to be at least 1 in CultivationManual");
        }
    }
}
