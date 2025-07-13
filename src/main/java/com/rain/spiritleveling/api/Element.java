package com.rain.spiritleveling.api;

import com.rain.spiritleveling.items.custom.SpiritIngredientItem;
import com.rain.spiritleveling.util.SpiritRegistries;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.puffish.attributesmod.api.DynamicEntityAttribute;

import java.util.HashMap;
import java.util.Map;

public abstract class Element {

    protected final Identifier ID;

    protected final EntityAttribute ATTRIBUTE;

    protected final Map<Stages, SpiritIngredientItem> ITEMS = new HashMap<>();

    protected Element(Identifier elementId) {
        ID = elementId;
        ATTRIBUTE = DynamicEntityAttribute.create(ID).setTracked(true);

        for (Stages stage : Stages.safeValues()) {
            ITEMS.put(stage, new SpiritIngredientItem(new Item.Settings(), stage, this));
        }
    }

    public static <T extends Element> void register(T element_instance) {
        Registry.register(SpiritRegistries.ELEMENTS, element_instance.ID, element_instance);

        Registry.register(Registries.ATTRIBUTE, element_instance.ID, element_instance.ATTRIBUTE);

        for (Stages stage : Stages.safeValues()) {
            Registry.register(Registries.ITEM, element_instance.ID.withSuffixedPath(stage.getString()), element_instance.ITEMS.get(stage));
        }
    }
}