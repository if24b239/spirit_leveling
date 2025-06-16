package com.rain.spiritleveling.networking;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.networking.packets.TriggerHUDAnimationS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class AllMessages {

    public static final Identifier HUD_ANIMATION = SpiritLeveling.loc("hud_animation");

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(HUD_ANIMATION, TriggerHUDAnimationS2CPacket::receive);
    }

    public static void registerC2SPackets() {

    }
}
