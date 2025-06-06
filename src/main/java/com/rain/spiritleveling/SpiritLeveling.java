package com.rain.spiritleveling;

import com.rain.spiritleveling.effects.SpiritEffects;
import com.rain.spiritleveling.events.ModEvents;
import com.rain.spiritleveling.items.SpiritItems;
import com.rain.spiritleveling.networking.ModMessages;
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

		// register all Items
		SpiritItems.initialize();

		// register networking
		ModMessages.registerC2SPackets();

		// register events
		ModEvents.initialize();

		// register effects
		SpiritEffects.initialize();

	}

	// get a registry Identifier for SpiritLeveling
	public static Identifier loc(String path) {
		return new Identifier(MOD_ID, path);
	}
}