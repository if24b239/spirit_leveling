package com.rain.spiritleveling;

import com.rain.spiritleveling.blocks.AllBlockEntities;
import com.rain.spiritleveling.blocks.AllBlocks;
import com.rain.spiritleveling.effects.AllEffects;
import com.rain.spiritleveling.entities.AllEntities;
import com.rain.spiritleveling.events.AllEvents;
import com.rain.spiritleveling.items.AllItems;
import com.rain.spiritleveling.items.AllRecipes;
import com.rain.spiritleveling.networking.AllMessages;
import com.rain.spiritleveling.screens.AllScreenHandlers;
import com.rain.spiritleveling.util.SpiritAttributes;
import com.rain.spiritleveling.util.SpiritElements;
import com.rain.spiritleveling.util.SpiritRegistries;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SpiritLeveling implements ModInitializer {
	public static final String MOD_ID = "spiritleveling";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Init Mod");

		// register Mod content
		SpiritRegistries.initialize();
		SpiritAttributes.initialize();
		AllMessages.registerC2SPackets();
		AllEvents.initialize();
		AllEffects.initialize();
		AllScreenHandlers.initialize();
		AllRecipes.registerRecipes();
		AllEntities.initialize();
		AllBlocks.initialize();
		AllBlockEntities.initialize();
		AllItems.initialize();
		SpiritElements.initialize();
	}

	// get a registry Identifier for SpiritLeveling
	public static Identifier loc(String path) {
		return new Identifier(MOD_ID, path);
	}
}