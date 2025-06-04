package com.rain.spiritleveling;

import com.rain.spiritleveling.items.SpiritItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SpiritLeveling implements ModInitializer {
	public static final String MOD_ID = "spiritleveling";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// register and apply new attributes
		SpiritAttributes.bootstrap();

		SpiritItems.initialize();

		LOGGER.info("Hello Fabric world!");
	}

	// get a registry Identifier for SpiritLeveling
	public static Identifier loc(String path) {
		return new Identifier(MOD_ID, path);
	}
}