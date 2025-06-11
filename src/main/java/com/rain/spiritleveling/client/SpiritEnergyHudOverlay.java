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

    private static final Identifier COVER_TO_STRONG_1 = SpiritLeveling.loc("textures/animation/cover_to_strong/frame_1.png");
    private static final Identifier COVER_TO_STRONG_2 = SpiritLeveling.loc("textures/animation/cover_to_strong/frame_2.png");
    private static final Identifier COVER_TO_STRONG_3 = SpiritLeveling.loc("textures/animation/cover_to_strong/frame_3.png");
    private static final Identifier COVER_TO_STRONG_4 = SpiritLeveling.loc("textures/animation/cover_to_strong/frame_4.png");
    private static final Identifier COVER_TO_STRONG_5 = SpiritLeveling.loc("textures/animation/cover_to_strong/frame_5.png");
    private static final Identifier COVER_TO_STRONG_6 = SpiritLeveling.loc("textures/animation/cover_to_strong/frame_6.png");
    private static final Identifier COVER_TO_STRONG_7 = SpiritLeveling.loc("textures/animation/cover_to_strong/frame_7.png");
    private static final Identifier COVER_TO_STRONG_8 = SpiritLeveling.loc("textures/animation/cover_to_strong/frame_8.png");
    private static final Identifier COVER_TO_STRONG_9 = SpiritLeveling.loc("textures/animation/cover_to_strong/frame_9.png");

    private static final Identifier COVER_TO_WEAK_1 = SpiritLeveling.loc("textures/animation/cover_to_weak/frame_1.png");
    private static final Identifier COVER_TO_WEAK_2 = SpiritLeveling.loc("textures/animation/cover_to_weak/frame_2.png");
    private static final Identifier COVER_TO_WEAK_3 = SpiritLeveling.loc("textures/animation/cover_to_weak/frame_3.png");
    private static final Identifier COVER_TO_WEAK_4 = SpiritLeveling.loc("textures/animation/cover_to_weak/frame_4.png");
    private static final Identifier COVER_TO_WEAK_5 = SpiritLeveling.loc("textures/animation/cover_to_weak/frame_5.png");
    private static final Identifier COVER_TO_WEAK_6 = SpiritLeveling.loc("textures/animation/cover_to_weak/frame_6.png");
    private static final Identifier COVER_TO_WEAK_7 = SpiritLeveling.loc("textures/animation/cover_to_weak/frame_7.png");
    private static final Identifier COVER_TO_WEAK_8 = SpiritLeveling.loc("textures/animation/cover_to_weak/frame_8.png");
    private static final Identifier COVER_TO_WEAK_9 = SpiritLeveling.loc("textures/animation/cover_to_weak/frame_9.png");

    private static final Identifier COVER_TO_NONE_1 = SpiritLeveling.loc("textures/animation/cover_to_none/frame_1.png");
    private static final Identifier COVER_TO_NONE_2 = SpiritLeveling.loc("textures/animation/cover_to_none/frame_2.png");
    private static final Identifier COVER_TO_NONE_3 = SpiritLeveling.loc("textures/animation/cover_to_none/frame_3.png");
    private static final Identifier COVER_TO_NONE_4 = SpiritLeveling.loc("textures/animation/cover_to_none/frame_4.png");
    private static final Identifier COVER_TO_NONE_5 = SpiritLeveling.loc("textures/animation/cover_to_none/frame_5.png");
    private static final Identifier COVER_TO_NONE_6 = SpiritLeveling.loc("textures/animation/cover_to_none/frame_6.png");
    private static final Identifier COVER_TO_NONE_7 = SpiritLeveling.loc("textures/animation/cover_to_none/frame_7.png");
    private static final Identifier COVER_TO_NONE_8 = SpiritLeveling.loc("textures/animation/cover_to_none/frame_8.png");
    private static final Identifier COVER_TO_NONE_9 = SpiritLeveling.loc("textures/animation/cover_to_none/frame_9.png");

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

    private static final ArrayList<Identifier> CHAINS_ANIMATION_TEXTURES = new ArrayList<>();
    private static final ArrayList<Identifier> SLOT_COVER_ANIMATION_TO_STRONG_TEXTURES = new ArrayList<>() {
        {
            add(COVER_TO_STRONG_1);
            add(COVER_TO_STRONG_2);
            add(COVER_TO_STRONG_3);
            add(COVER_TO_STRONG_4);
            add(COVER_TO_STRONG_5);
            add(COVER_TO_STRONG_6);
            add(COVER_TO_STRONG_7);
            add(COVER_TO_STRONG_8);
            add(COVER_TO_STRONG_9);
        }
    };
    private static final ArrayList<Identifier> SLOT_COVER_ANIMATION_TO_WEAK_TEXTURES = new ArrayList<>() {
        {
            add(COVER_TO_WEAK_1);
            add(COVER_TO_WEAK_2);
            add(COVER_TO_WEAK_3);
            add(COVER_TO_WEAK_4);
            add(COVER_TO_WEAK_5);
            add(COVER_TO_WEAK_6);
            add(COVER_TO_WEAK_7);
            add(COVER_TO_WEAK_8);
            add(COVER_TO_WEAK_9);
        }
    };
    private static final ArrayList<Identifier> SLOT_COVER_ANIMATION_TO_NONE_TEXTURES = new ArrayList<>() {
        {
            add(COVER_TO_NONE_1);
            add(COVER_TO_NONE_2);
            add(COVER_TO_NONE_3);
            add(COVER_TO_NONE_4);
            add(COVER_TO_NONE_5);
            add(COVER_TO_NONE_6);
            add(COVER_TO_NONE_7);
            add(COVER_TO_NONE_8);
            add(COVER_TO_NONE_9);
        }
    };
    private static final ArrayList<Identifier> SLOT_COVER_ANIMATION_ALL_TEXTURES = new ArrayList<>() {
        {
            add(COVER_TO_STRONG_1);
            add(COVER_TO_STRONG_2);
            add(COVER_TO_STRONG_3);
            add(COVER_TO_STRONG_4);
            add(COVER_TO_STRONG_5);
            add(COVER_TO_STRONG_6);
            add(COVER_TO_STRONG_7);
            add(COVER_TO_STRONG_8);
            add(COVER_TO_STRONG_9);
            add(COVER_TO_WEAK_1);
            add(COVER_TO_WEAK_2);
            add(COVER_TO_WEAK_3);
            add(COVER_TO_WEAK_4);
            add(COVER_TO_WEAK_5);
            add(COVER_TO_WEAK_6);
            add(COVER_TO_WEAK_7);
            add(COVER_TO_WEAK_8);
            add(COVER_TO_WEAK_9);
            add(COVER_TO_NONE_1);
            add(COVER_TO_NONE_2);
            add(COVER_TO_NONE_3);
            add(COVER_TO_NONE_4);
            add(COVER_TO_NONE_5);
            add(COVER_TO_NONE_6);
            add(COVER_TO_NONE_7);
            add(COVER_TO_NONE_8);
            add(COVER_TO_NONE_9);
        }
    };

    public static final ClientHUDAnimation CHAINS_ANIMATION = ClientHUDAnimation.createClientHUDAnimation(3, CHAINS_ANIMATION_TEXTURES, 0 ,0);
    public static final ClientHUDAnimation SLOT_COVER_ANIMATION_TO_STRONG = ClientHUDAnimation.createClientHUDAnimation(3, SLOT_COVER_ANIMATION_TO_STRONG_TEXTURES, 11, 17);
    public static final ClientHUDAnimation SLOT_COVER_ANIMATION_TO_WEAK = ClientHUDAnimation.createClientHUDAnimation(3, SLOT_COVER_ANIMATION_TO_WEAK_TEXTURES,11,17);
    public static final ClientHUDAnimation SLOT_COVER_ANIMATION_TO_NONE = ClientHUDAnimation.createClientHUDAnimation(3, SLOT_COVER_ANIMATION_TO_NONE_TEXTURES,11,17);
    public static final ClientHUDAnimation SLOT_COVER_ANIMATION_ALL = ClientHUDAnimation.createClientHUDAnimation(2, SLOT_COVER_ANIMATION_ALL_TEXTURES, 11, 17);

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
        boolean drawLastChain = ((ISpiritEnergyPlayer)player).spirit_leveling$getDrawLastChain();
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
            boolean drawToNone = false;
            if (i > 9 - currentMinorLevel) continue;
            if (i == 9 - currentMinorLevel) {
                if (ratioNextLevel >= 0.66F) {
                    drawTarget = SLOT_COVER_WEAK;
                } else if (ratioNextLevel >= 0.33F) {
                    drawTarget = SLOT_COVER_STRONG;
                } else if (ratioNextLevel == 0.00F) {
                    drawToNone = true;
                }

                drawChains = drawLastChain && drawChains;
            }
            //draw the covers
            drawContext.drawTexture(
                    drawTarget,
                    cover_x, cover_y + (10 * i),
                    0,0,
                    coverWidth, coverHeight,
                    coverWidth, coverHeight);

            // render animation to strong
            if (drawTarget == SLOT_COVER_STRONG && SLOT_COVER_ANIMATION_TO_STRONG.getIsPlaying()) {
                SLOT_COVER_ANIMATION_TO_STRONG.setPosition(cover_x - 3, cover_y + (10 * i));
                SLOT_COVER_ANIMATION_TO_STRONG.render(drawContext);
            }

            // render animation to weak
            if (drawTarget == SLOT_COVER_WEAK && SLOT_COVER_ANIMATION_TO_WEAK.getIsPlaying()) {
                SLOT_COVER_ANIMATION_TO_WEAK.setPosition(cover_x - 3, cover_y + (10 * i));
                SLOT_COVER_ANIMATION_TO_WEAK.render(drawContext);
            }

            // render animation to none
            if (drawToNone && SLOT_COVER_ANIMATION_TO_NONE.getIsPlaying()) {
                SLOT_COVER_ANIMATION_TO_NONE.setPosition(cover_x - 3, cover_y + (10 * (i + 1)));
                SLOT_COVER_ANIMATION_TO_NONE.render(drawContext);
            }

            // render full animation
            if (drawToNone && maxEnergy <= 10 && SLOT_COVER_ANIMATION_ALL.getIsPlaying()) {
                SLOT_COVER_ANIMATION_ALL.setPosition(cover_x - 3, cover_y + (10 * (i + 1)));
                SLOT_COVER_ANIMATION_ALL.render(drawContext);
            }

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
