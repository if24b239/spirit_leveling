package com.rain.spiritleveling.client;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.energymanager.ClientSpiritEnergyManager;
import com.rain.spiritleveling.energymanager.MajorSpiritLevel;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class SpiritEnergyHudOverlay implements HudRenderCallback {



    private static final ArrayList<Identifier> CHAINS_ANIMATION_TEXTURES = new ArrayList<>();

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {

        int positionX = 6;
        int positionY = 0;
        int barHeight = 104;

        MinecraftClient client = MinecraftClient.getInstance();
        IClientSpiritEnergyPlayer player = null;

        // get bar position
        if (client != null) {
            int height = client.getWindow().getScaledHeight();

            positionY = (height - barHeight) / 2;

            player = (IClientSpiritEnergyPlayer) client.player;
        }

        if (player == null) {
            SpiritLeveling.LOGGER.error("NO PLAYER FOUND ON CLIENT");
            return;
        }

        int maxEnergy = player.spirit_leveling$getDataMaxEnergy();
        int currentEnergy = player.spirit_leveling$getDataCurrentEnergy();
        boolean minorBottleneck = player.spirit_leveling$getDataMinorBottleneck();
        int sLevel = player.spirit_leveling$getDataSpiritLevel();
        int sPower = MajorSpiritLevel.calculateSpiritStrength(currentEnergy);

        ClientSpiritEnergyManager SE_manager = new ClientSpiritEnergyManager(drawContext, sLevel, maxEnergy, minorBottleneck, positionX, positionY);

        SE_manager.drawBar(sLevel)
                .drawSlots(sPower, currentEnergy, maxEnergy)
                .drawCovers(sPower, sLevel)
                .drawChains(sPower, sLevel);
    }


}
