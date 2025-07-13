package com.rain.spiritleveling.util;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.api.Element;

public class SpiritElements {

    public static final Element WOOD = new Element(SpiritLeveling.loc("wood"));
    public static final Element FIRE = new Element(SpiritLeveling.loc("fire"));
    public static final Element EARTH = new Element(SpiritLeveling.loc("earth"));
    public static final Element METAL = new Element(SpiritLeveling.loc("metal"));
    public static final Element WATER = new Element(SpiritLeveling.loc("water"));

    public static void initialize() {

        Element.register(WOOD);
        Element.register(FIRE);
        Element.register(EARTH);
        Element.register(METAL);
        Element.register(WATER);
    }
}
