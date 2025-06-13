package com.rain.spiritleveling.networking.packets;

import com.rain.spiritleveling.client.HUDAnimationStruct;
import com.rain.spiritleveling.client.SpiritEnergyHudOverlay;
import com.rain.spiritleveling.util.ClientSpiritEnergyManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class TriggerHUDAnimationS2CPacket  {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        HUDAnimationStruct test = new HUDAnimationStruct(30, 30, 11, 17, ClientSpiritEnergyManager.COVER_ANIMATION_TO_STRONG_TEXTURES);

        client.execute(() -> ClientSpiritEnergyManager.COVER_ANIMATOR.addQueue(test));
    }
}
