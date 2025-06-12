package com.rain.spiritleveling;

import com.rain.spiritleveling.client.ClientHUDAnimation;
import com.rain.spiritleveling.client.SpiritEnergyHudOverlay;
import com.rain.spiritleveling.networking.ModMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class SpiritLevelingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SpiritLeveling.LOGGER.info("Init Client");

        ModMessages.registerS2CPackets();

        // energy bar hud overlay
        //HudRenderCallback.EVENT.register(new SpiritEnergyHudOverlay());
    }
}
