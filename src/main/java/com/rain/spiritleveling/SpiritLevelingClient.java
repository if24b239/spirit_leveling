package com.rain.spiritleveling;

import com.rain.spiritleveling.client.AllRenderers;
import com.rain.spiritleveling.client.hud.SpiritEnergyHudOverlay;
import com.rain.spiritleveling.networking.AllMessages;
import com.rain.spiritleveling.screens.AllScreenHandlers;
import com.rain.spiritleveling.screens.MeditationMatScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class SpiritLevelingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SpiritLeveling.LOGGER.info("Init Client");


        // register client side content
        AllMessages.registerS2CPackets();
        AllRenderers.initialize();

        // energy bar hud overlay
        HudRenderCallback.EVENT.register(new SpiritEnergyHudOverlay());

        // screen linking
        HandledScreens.register(AllScreenHandlers.MEDITATION_MAT, MeditationMatScreen::new);
    }
}
