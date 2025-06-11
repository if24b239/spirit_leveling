package com.rain.spiritleveling.networking.packets;

import com.rain.spiritleveling.client.SpiritEnergyHudOverlay;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class TriggerHUDAnimationS2CPacket  {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        switch (buf.readInt()) {
            case 0:
                client.execute(SpiritEnergyHudOverlay.SLOT_COVER_ANIMATION_TO_STRONG::trigger);
                break;
            case 1:
                client.execute(SpiritEnergyHudOverlay.SLOT_COVER_ANIMATION_TO_WEAK::trigger);
                break;
            case 2:
                client.execute(SpiritEnergyHudOverlay.SLOT_COVER_ANIMATION_TO_NONE::trigger);
                break;
            case 3:
                client.execute(SpiritEnergyHudOverlay.SLOT_COVER_ANIMATION_ALL::trigger);
                break;
        }
    }
}
