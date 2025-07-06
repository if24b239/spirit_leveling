package com.rain.spiritleveling.client.hud;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.api.Stages;
import com.rain.spiritleveling.energymanager.ClientSpiritEnergyManager;
import com.rain.spiritleveling.energymanager.MajorSpiritLevel;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class SpiritEnergyHudOverlay implements HudRenderCallback {

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
        Stages sLevel = player.spirit_leveling$getDataSpiritLevel();
        Stages sPower = MajorSpiritLevel.calculateSpiritStrength(currentEnergy);

        ClientSpiritEnergyManager SE_manager = new ClientSpiritEnergyManager(drawContext, sLevel, maxEnergy, minorBottleneck, positionX, positionY);

        SE_manager.drawBar()
                .drawSlots(sPower, currentEnergy, maxEnergy)
                .drawCovers(sPower, sLevel)
                .drawChains(sPower, sLevel);
    }


}
