package com.rain.spiritleveling.networking.packets;

import com.rain.spiritleveling.blocks.entity.MeditationMatEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class FlipIsReceivingC2SPacket {
    public static void receive(MinecraftServer ignoredServer, ServerPlayerEntity player, ServerPlayNetworkHandler ignoredHandler, PacketByteBuf buf, PacketSender ignoredResponseSender) {

        BlockPos pos = buf.readBlockPos();

        Objects.requireNonNull(player.getWorld().getServer()).execute(() -> {
            MeditationMatEntity blockEntity = ((MeditationMatEntity) player.getWorld().getBlockEntity(pos));

            assert blockEntity != null;
            blockEntity.flipIsReceiving();
        });
    }
}
