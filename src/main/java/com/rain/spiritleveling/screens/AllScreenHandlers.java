package com.rain.spiritleveling.screens;

import com.rain.spiritleveling.SpiritLeveling;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public class AllScreenHandlers {

    public static ScreenHandlerType<SpiritInfusionScreenHandler> SPIRIT_INFUSION = Registry.register(Registries.SCREEN_HANDLER, SpiritLeveling.loc("spirit_infusion"),
            new ExtendedScreenHandlerType<>(SpiritInfusionScreenHandler::new));

    public static void initialize() {
        SpiritLeveling.LOGGER.info("Initializing Screens Handlers");
    }
}
