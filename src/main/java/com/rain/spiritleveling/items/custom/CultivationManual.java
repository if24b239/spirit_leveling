package com.rain.spiritleveling.items.custom;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.api.Phases;
import com.rain.spiritleveling.api.ISpiritEnergyPlayer;
import com.rain.spiritleveling.api.Stages;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
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
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CultivationManual extends Item {

    private static final Map<EntityAttribute, MutableText> ATTRIBUTE_DISPLAY = new HashMap<>();
    private static final int DURABILITY = 8;

    private final Map<Phases, EntityAttribute> attributes;
    private final Map<Phases, EntityAttributeModifier> modifiers;

    private final Stages level;

    /// level has to be at least 1 since 0 does not have breakthroughs
    public CultivationManual(Settings settings, Stages level, List<EntityAttribute> attributes, List<EntityAttributeModifier> modifiers) {
        super(settings
                .maxDamage(DURABILITY)
                .rarity(Rarity.RARE)
        );

        this.level = level;
        this.attributes = IntStream.range(0, attributes.size()).boxed().collect(Collectors.toMap(Phases::stateOf, attributes::get));
        this.modifiers = IntStream.range(0, modifiers.size()).boxed().collect(Collectors.toMap(Phases::stateOf, modifiers::get));
    }

    public static Phases getActivePhase(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();

        if (nbt == null || !nbt.contains("activeIndex"))
            return Phases.NONE;

        return Phases.stateOf(nbt.getInt("activeIndex"));
    }

    public static void setActiveElement(ItemStack stack, Phases elementIndex) {
        stack.getOrCreateNbt().putInt("activeIndex", elementIndex.getValue());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Phases activePhase = getActivePhase(stack);
        for (Phases e : Phases.safeValues()) {
            addAttributeDisplay(attributes.get(e));

            MutableText text = Text.empty().append(Text.translatable(e.getTooltipString()))
                    .append(": ")
                    .append(getOperationValue(modifiers.get(e).getOperation(), modifiers.get(e).getValue()))
                    .append(ATTRIBUTE_DISPLAY.get(attributes.get(e)))
                    .formatted((e == activePhase) ? Formatting.DARK_AQUA : Formatting.WHITE);

            tooltip.add(text);
        }
    }

    private Text getOperationValue(EntityAttributeModifier.Operation operation, double value) {
        MutableText out = Text.empty();

        switch (operation) {
            case ADDITION -> out.append("+" + value + " ");
            case MULTIPLY_BASE -> out.append("+" + value*100 + "% ");
            case MULTIPLY_TOTAL -> out.append((100+value*100) + "% ");
        }

        return out;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return (getActivePhase(stack) == Phases.NONE) ? 0 : 48;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack stack = user.getStackInHand(hand);
        user.setCurrentHand(hand);

        return TypedActionResult.consume(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        Phases activePhase = getActivePhase(stack);

        if (world.isClient() || activePhase == Phases.NONE)
            return stack;

        if (user instanceof ServerPlayerEntity && !((ISpiritEnergyPlayer) user).spirit_leveling$minorBreakthrough(this.getLevel()))
            return stack;

        // increase attribute and reset the manual with one durability less
        incrementAttribute(user, activePhase);
        setActiveElement(stack, Phases.NONE);

        return damageStack(stack);
    }

    private ItemStack damageStack(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamage(copy.getDamage() + 1);

        if (copy.getDamage() >= copy.getMaxDamage())
            return ItemStack.EMPTY;

        return copy;
    }

    /// throws exception when element is Phases. NONE
    private void incrementAttribute(LivingEntity user, Phases element) {
        if (element == Phases.NONE)
            throw new IllegalArgumentException("increment Attribute element can't be Phases.NONE");

        // the attribute that will be modified
        EntityAttribute att = this.attributes.get(element);

        // the modifier to change the attribute
        EntityAttributeModifier mod = this.modifiers.get(element);

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

        // add uuid and attribute id to the player nbt for display and respawn
        if (user instanceof ServerPlayerEntity)
            ((ISpiritEnergyPlayer) user).spirit_leveling$addModifierUUID(Registries.ATTRIBUTE.getId(att) ,mod.getId());
    }

    public Stages getLevel() {
        return this.level;
    }

    public static class Builder {
        Identifier itemId;

        private final List<EntityAttribute> attributes = new ArrayList<>(Collections.nCopies(5, null));
        private final List<EntityAttributeModifier> modifiers = new ArrayList<>(Collections.nCopies(5, null));

        private Stages level = Stages.MORTAL;

        private Builder(@NotNull Identifier itemId) {
            this.itemId = itemId;
        }

        public static Builder create(@NotNull String itemId) {
            return new Builder(SpiritLeveling.loc(itemId));
        }

        public Builder addAttributeAndModifier(Phases element, @NotNull EntityAttribute attribute, double value, EntityAttributeModifier.Operation operation) {
            validateElement(element);

            // add the attribute id
            this.attributes.set(element.getValue(), attribute);

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

        public Builder setLevel(Stages lvl) {
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

        private void validateElement(Phases element) {
            if (element == Phases.NONE)
                throw new IllegalStateException("Can't set Phases.NONE");

            if (attributes.get(element.getValue()) != null)
                throw new IllegalStateException("The attribute on index " + element + " has already been set");

            if (modifiers.get(element.getValue()) != null)
                throw new IllegalStateException("The modifier for index " + element + " has already been set");
        }

        private void validateComplete() {
            for (EntityAttribute a : this.attributes) {
                if (a == null)
                    throw new IllegalStateException("All indices of target attributes in CultivationManual need to be filled");
            }

            for (EntityAttributeModifier i : this.modifiers) {
                if (i == null)
                    throw new IllegalStateException("All modifiers in CultivationManual need to be created correctly");
            }

            if (level.getValue() < Stages.SPIRIT_CONDENSATION.getValue())
                throw new IllegalStateException("Level needs to be at least 1 in CultivationManual");
        }
    }

    public static void generateModel(ItemModelGenerator modelGenerator, @NotNull CultivationManual item) {
        Models.GENERATED_THREE_LAYERS.upload(
                ModelIds.getItemModelId(item),
                TextureMap.layered(SpiritLeveling.loc("item/manual_base"), SpiritLeveling.loc("item/manual_" + item.getLevel().getString()), Registries.ITEM.getId(item).withPrefixedPath("item/")),
                modelGenerator.writer);
    }

    public static void addAttributeDisplay(EntityAttribute attribute) {
        if (ATTRIBUTE_DISPLAY.containsKey(attribute))
            return;

        ATTRIBUTE_DISPLAY.put(attribute, Text.translatable("tooltip.spiritleveling." + Objects.requireNonNull(Registries.ATTRIBUTE.getId(attribute)).toShortTranslationKey()));
    }
}
