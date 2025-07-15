package com.rain.spiritleveling.api;

import com.rain.spiritleveling.items.custom.SpiritIngredientItem;
import com.rain.spiritleveling.util.SpiritRegistries;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.puffish.attributesmod.api.DynamicEntityAttribute;
import org.joml.Vector2d;

import java.util.*;

public class Element {

    public static final Map<Element, Vector2d> registeredFlowPositions = new HashMap<>();

    protected final Identifier ID;

    protected final EntityAttribute ATTRIBUTE;

    protected final RegistryKey<DamageType> DAMAGE_TYPE;

    protected final Map<Identifier, SpiritIngredientItem> ITEMS = new HashMap<>();
    private final Vector2d flowPos;

    public Element(Identifier elementId, Vector2d flowPos) {
        ID = elementId;
        this.flowPos = flowPos;
        ATTRIBUTE = DynamicEntityAttribute.create(ID).setTracked(true);

        for (Stages stage : Stages.safeValues()) {
            ITEMS.put(this.ID.withSuffixedPath("/" + stage.getString() + "_item"), new SpiritIngredientItem(new Item.Settings(), stage, this));
        }

        DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, ID);
    }

    /**
     * Make sure to only call this BEFORE calling Element::register
     * @param name Will generate an Identifier for the item with structure element_id/name
     * @param item the item instance to be added to the element
     * @return this
     */
    public Element addNewItem(String name, SpiritIngredientItem item) {
        if (item.getElement() != this)
            throw new IllegalStateException(item + " should be constructed with element parameter equal to" + this);

        ITEMS.put(this.ID.withSuffixedPath("/" + name), item);

        return this;
    }

    /**
     * Called to register the element and all other automatically generated objects like the basic items and attribute associated to the element
     * @param element_instance the instance of the element that will be registered
     */
    public static <T extends Element> T register(T element_instance) {

        Vector2d flowPos = new Vector2d(element_instance.getFlowPos());
        // flow position checks
        if (flowPos.absolute().x > 5 || flowPos.absolute().y > 5)
            throw new IllegalStateException("Flow Pos: " + flowPos + " in Element: " + element_instance + " has to be in area from (-5,-5) to (5,5)");


        // register all objects of the element
        Registry.register(SpiritRegistries.ELEMENTS, element_instance.ID, element_instance);

        Registry.register(Registries.ATTRIBUTE, element_instance.ID, element_instance.ATTRIBUTE);

        element_instance.ITEMS.forEach((id, item) -> Registry.register(Registries.ITEM, id, item));

        return element_instance;
    }


    public static Element[] getElements() {
        return SpiritRegistries.ELEMENTS.stream().toArray(Element[]::new);
    }

    /**
     * @return an array of ALL items registered with all registered elements
     */
    public static SpiritIngredientItem[] getItems() {
        List<SpiritIngredientItem> list = new ArrayList<>();
        Arrays.stream(getElements()).forEach(e -> list.addAll(Arrays.asList(getItems(e))));
        return list.toArray(SpiritIngredientItem[]::new);
    }

    /**
     * @return A list of all Items registered to element
     */
    public static SpiritIngredientItem[] getItems(Element element) {
        return element.ITEMS.values().toArray(new SpiritIngredientItem[0]);
    }

    public Vector2d getFlowPos() {
        return this.flowPos;
    }
}