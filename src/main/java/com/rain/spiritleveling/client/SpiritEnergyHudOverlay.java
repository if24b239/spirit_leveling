package com.rain.spiritleveling.client;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.util.ISpiritEnergyPlayer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class SpiritEnergyHudOverlay implements HudRenderCallback {

    private static final Identifier ENERGY_BAR = SpiritLeveling.loc("textures/spirit_energy/hud_bar.png");
    private static final Identifier ENERGY_SLOTS_1 = SpiritLeveling.loc("textures/spirit_energy/energy_10.png");
    private static final Identifier ENERGY_SLOTS_2 = SpiritLeveling.loc("textures/spirit_energy/energy_100.png");
    private static final Identifier ENERGY_SLOTS_3 = SpiritLeveling.loc("textures/spirit_energy/energy_1000.png");

    private static final ArrayList<Identifier> SLOT_LIST = new ArrayList<>() {
        {
            add(ENERGY_SLOTS_1);
            add(ENERGY_SLOTS_2);
            add(ENERGY_SLOTS_3);
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

        // draw the slots based on current energy
        assert MinecraftClient.getInstance().player != null;
        int currentEnergy = ((ISpiritEnergyPlayer) MinecraftClient.getInstance().player).spirit_leveling$getCurrentData();

        // only draw as many slots as current energy allows
        for (int i = 1; i <= SLOT_LIST.size(); i++) {
            if (currentEnergy <= Math.pow(10, i)) {
                currentEnergy /= (int)Math.pow(10, i-1);
                drawContext.drawTexture(SLOT_LIST.get(i-1), slots_x, slots_y, slotsWidth, currentEnergy * 10, 0,0, slotsWidth, currentEnergy * 10, slotsWidth, slotsHeight);
                break;
            }
        }
    }
}
