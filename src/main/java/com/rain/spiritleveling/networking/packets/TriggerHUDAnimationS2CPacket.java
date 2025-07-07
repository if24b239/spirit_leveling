package com.rain.spiritleveling.networking.packets;

import com.rain.spiritleveling.client.hud.HUDAnimationStruct;
import com.rain.spiritleveling.energymanager.ClientSpiritEnergyManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class TriggerHUDAnimationS2CPacket  {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler ignoredHandler, PacketByteBuf buf, PacketSender ignoredSender) {

        boolean bool = buf.readBoolean();

        // clear only the animator queue
        if (bool) {
            ClientSpiritEnergyManager.COVER_ANIMATOR.clearQueue();
            ClientSpiritEnergyManager.CHAINS_ANIMATOR.clearQueue();
            return;
        }

        int[] data = buf.readIntArray();

        int index = data[0];

        if (data.length == 3) {
            // cover animation was called
            for (int i = data[1]; i > data[2]; i--) {
                // setup animation structure
                int relative_x = 0;
                int relative_y = index * 10;

                ArrayList<Identifier> textures = ClientSpiritEnergyManager.COVER_ANIMATION_TO_NONE_TEXTURES;

                if (i == 2) {
                    textures = ClientSpiritEnergyManager.COVER_ANIMATION_TO_WEAK_TEXTURES;
                } else if (i == 3) {
                    textures = ClientSpiritEnergyManager.COVER_ANIMATION_TO_STRONG_TEXTURES;
                }

                HUDAnimationStruct animation = new HUDAnimationStruct(relative_x, relative_y, 11, 14, textures);

                // add animation to queue
                client.execute(() -> ClientSpiritEnergyManager.COVER_ANIMATOR.addQueue(animation));
            }

        } else {
            // chains animation was called

            int relative_x = -12;
            int relative_y = (index * 10) - 2;

            HUDAnimationStruct animation = new HUDAnimationStruct(relative_x, relative_y, 27, 26, ClientSpiritEnergyManager.REMOVE_CHAINS_ANIMATION);

            //add animation to queue
            client.execute(() -> ClientSpiritEnergyManager.CHAINS_ANIMATOR.addQueue(animation));
        }

    }
}
