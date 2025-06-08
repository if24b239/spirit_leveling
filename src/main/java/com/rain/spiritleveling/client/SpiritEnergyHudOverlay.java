package com.rain.spiritleveling.client;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.util.ISpiritEnergyPlayer;
import com.rain.spiritleveling.api.SpiritEnergyData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class SpiritEnergyHudOverlay implements HudRenderCallback {

    private static final Identifier ENERGY_BAR = SpiritLeveling.loc("textures/spirit_energy/hud_bar.png");

    private static final Identifier ENERGY_SLOTS_1 = SpiritLeveling.loc("textures/spirit_energy/energy_10_1.png");
    private static final Identifier ENERGY_SLOTS_2 = SpiritLeveling.loc("textures/spirit_energy/energy_10_2.png");
    private static final Identifier ENERGY_SLOTS_3 = SpiritLeveling.loc("textures/spirit_energy/energy_10_3.png");
    private static final Identifier ENERGY_SLOTS_4 = SpiritLeveling.loc("textures/spirit_energy/energy_10_4.png");
    private static final Identifier ENERGY_SLOTS_5 = SpiritLeveling.loc("textures/spirit_energy/energy_10_5.png");
    private static final Identifier ENERGY_SLOTS_6 = SpiritLeveling.loc("textures/spirit_energy/energy_10_6.png");
    private static final Identifier ENERGY_SLOTS_7 = SpiritLeveling.loc("textures/spirit_energy/energy_10_7.png");

    private static final Identifier SLOT_COVER_FULL = SpiritLeveling.loc("textures/spirit_energy/slot_cover_full.png");
    private static final Identifier SLOT_COVER_STRONG = SpiritLeveling.loc("textures/spirit_energy/slot_cover_strong.png");
    private static final Identifier SLOT_COVER_WEAK = SpiritLeveling.loc("textures/spirit_energy/slot_cover_weak.png");

    private static final Identifier CHAINS = SpiritLeveling.loc("textures/spirit_energy/chains.png");

    private static final ArrayList<Identifier> SLOT_LIST = new ArrayList<>() {
        {
            add(ENERGY_SLOTS_1);
            add(ENERGY_SLOTS_2);
            add(ENERGY_SLOTS_3);
            add(ENERGY_SLOTS_4);
            add(ENERGY_SLOTS_5);
            add(ENERGY_SLOTS_6);
            add(ENERGY_SLOTS_7);
        }
    };


    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {

        int bar_x = 6;
        int bar_y = 0;
        int barHeight = 104;
        int barWidth = 11;

        MinecraftClient client = MinecraftClient.getInstance();

        // get bar position
        if (client != null) {
            int height = client.getWindow().getScaledHeight();

            bar_y = (height - barHeight) / 2;
        }

        // get slots sizes and position relative to bar
        int slots_x = bar_x + 3;
        int slots_y = bar_y;
        int slotsHeight = 101;
        int slotsWidth = 5;

        // draw the bar
        drawContext.drawTexture(ENERGY_BAR, bar_x, bar_y, 0, 0, barWidth, barHeight, barWidth, barHeight);

        // get data for drawing slots and covers
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;

        int currentEnergy = ((ISpiritEnergyPlayer)player).spirit_leveling$getCurrentData();
        int maxEnergy = ((ISpiritEnergyPlayer)player).spirit_leveling$getMaxData();
        int spiritLevel = ((ISpiritEnergyPlayer)player).spirit_leveling$getLevelData();
        int spiritPower = ((ISpiritEnergyPlayer)player).spirit_leveling$getPowerData();
        boolean minorBottleneck = ((ISpiritEnergyPlayer)player).spirit_leveling$getMinorBottleneck();
        boolean isMaxEnergy = maxEnergy == currentEnergy;

        // set spiritPower to 6 (corresponding to ENERGY_SLOTS_7) to continue drawing it above spiritPower 6
        if (SLOT_LIST.size() - 1 < spiritPower) spiritPower = 6;

        // only draw as many slots as current energy allows
        currentEnergy /= (int)Math.pow(10, spiritPower);
        if (isMaxEnergy && currentEnergy < 10 && maxEnergy > 10) currentEnergy++;

        int offset = (10 - currentEnergy) * 10;

        drawContext.drawTexture(
                SLOT_LIST.get(spiritPower),
                slots_x, slots_y + offset,
                0, offset,
                slotsWidth, slotsHeight - offset,
                slotsWidth, slotsHeight);

        // get minor level and percentage until minor level is complete
        int minorLevelDiff = (int)Math.pow(10, spiritLevel);
        int currentMinorLevel = SpiritEnergyData.getMinorLevel(maxEnergy, spiritLevel);
        int energyOverCurrentMinor = maxEnergy - (currentMinorLevel * minorLevelDiff);
        double ratioNextLevel = (double) energyOverCurrentMinor / minorLevelDiff;

        // get cover position and sizes in relation to bar
        int cover_x = bar_x + 3;
        int cover_y = bar_y;
        int coverHeight = 11;
        int coverWidth = 5;

        // chains position
        int chains_x = bar_x - 5;
        int chains_y = bar_y - 2;
        int chainsHeight = 16;
        int chainsWidth = 20;

        // only draw covers if spiritPower is equal to spiritLevel
        if (spiritLevel != spiritPower) return;

        // logic for slot covers
        for (int i = 0; i <= 9; i++) {
            Identifier drawTarget = SLOT_COVER_FULL;
            boolean drawChains = spiritLevel != 0;
            if (i > 9 - currentMinorLevel) continue;
            if (i == 9 - currentMinorLevel) {
                if (ratioNextLevel >= 0.66F) {
                    drawTarget = SLOT_COVER_WEAK;
                } else if (ratioNextLevel >= 0.33F) {
                    drawTarget = SLOT_COVER_STRONG;
                }

                drawChains = minorBottleneck;
            }
            //draw the cover
            drawContext.drawTexture(
                    drawTarget,
                    cover_x, cover_y + (10 * i),
                    0,0,
                    coverWidth, coverHeight,
                    coverWidth, coverHeight);

            //draw chains
            if (!drawChains) continue;
            drawContext.drawTexture(
                    CHAINS,
                    chains_x, chains_y + (10 * i),
                    0, 0,
                    chainsWidth, chainsHeight,
                    chainsWidth, chainsHeight);
        }
    }
}
