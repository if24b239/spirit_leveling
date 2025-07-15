package com.rain.spiritleveling.util;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.api.Element;
import org.joml.Vector2d;

public class SpiritElements {

    public static final Element WOOD = new Element(SpiritLeveling.loc("wood"), new Vector2d(0f,4f));
    public static final Element FIRE = new Element(SpiritLeveling.loc("fire"), new Vector2d(5f, 2f));
    public static final Element EARTH = new Element(SpiritLeveling.loc("earth"), new Vector2d(3f,-3f));
    public static final Element METAL = new Element(SpiritLeveling.loc("metal"), new Vector2d(-3f,-3f));
    public static final Element WATER = new Element(SpiritLeveling.loc("water"), new Vector2d(-5f, 2f));

    public static void initialize() {

        Element.register(WOOD);
        Element.register(FIRE);
        Element.register(EARTH);
        Element.register(METAL);
        Element.register(WATER);

        SpiritLeveling.LOGGER.info("{}", SpiritMath.calculateElementFlow(WOOD, FIRE));
        SpiritLeveling.LOGGER.info("{}", SpiritMath.calculateElementFlow(FIRE,EARTH));
        SpiritLeveling.LOGGER.info("{}", SpiritMath.calculateElementFlow(EARTH,METAL));
        SpiritLeveling.LOGGER.info("{}", SpiritMath.calculateElementFlow(METAL,WATER));
        SpiritLeveling.LOGGER.info("{}", SpiritMath.calculateElementFlow(WATER,WOOD));
    }
}
