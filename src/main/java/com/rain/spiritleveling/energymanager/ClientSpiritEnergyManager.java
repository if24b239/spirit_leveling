package com.rain.spiritleveling.energymanager;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.client.hud.ClientHUDAnimator;
import com.rain.spiritleveling.util.ClientMinorSpiritLevelFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ClientSpiritEnergyManager extends MajorSpiritLevel<MinorSpiritLevel> {

    private final int posX;
    private final int posY;
    private final DrawContext drawContext;
    private COVER_STATE lastCoverState;
    private int drawn_covers = 0;

    public static ClientHUDAnimator CHAINS_ANIMATOR = ClientHUDAnimator.createClientHUDAnimation(10);
    public static ClientHUDAnimator COVER_ANIMATOR = ClientHUDAnimator.createClientHUDAnimation(4);

    public static Identifier ENERGY_BAR = SpiritLeveling.loc("textures/spirit_energy/hud_bar.png");

    public static Identifier CHAINS = SpiritLeveling.loc("textures/spirit_energy/chains.png");

    private static final Identifier COVER_FULL = SpiritLeveling.loc("textures/spirit_energy/slot_cover_full.png");
    private static final Identifier COVER_STRONG = SpiritLeveling.loc("textures/spirit_energy/slot_cover_strong.png");
    private static final Identifier COVER_WEAK = SpiritLeveling.loc("textures/spirit_energy/slot_cover_weak.png");

    private static final Identifier ENERGY_SLOTS_1 = SpiritLeveling.loc("textures/spirit_energy/energy_10_1.png");
    private static final Identifier ENERGY_SLOTS_2 = SpiritLeveling.loc("textures/spirit_energy/energy_10_2.png");
    private static final Identifier ENERGY_SLOTS_3 = SpiritLeveling.loc("textures/spirit_energy/energy_10_3.png");
    private static final Identifier ENERGY_SLOTS_4 = SpiritLeveling.loc("textures/spirit_energy/energy_10_4.png");
    private static final Identifier ENERGY_SLOTS_5 = SpiritLeveling.loc("textures/spirit_energy/energy_10_5.png");
    private static final Identifier ENERGY_SLOTS_6 = SpiritLeveling.loc("textures/spirit_energy/energy_10_6.png");
    private static final Identifier ENERGY_SLOTS_7 = SpiritLeveling.loc("textures/spirit_energy/energy_10_7.png");

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

    private static final Identifier REMOVE_CHAINS_1 = SpiritLeveling.loc("textures/animation/remove_chain/frame_1.png");
    private static final Identifier REMOVE_CHAINS_2 = SpiritLeveling.loc("textures/animation/remove_chain/frame_2.png");
    private static final Identifier REMOVE_CHAINS_3 = SpiritLeveling.loc("textures/animation/remove_chain/frame_3.png");
    private static final Identifier REMOVE_CHAINS_4 = SpiritLeveling.loc("textures/animation/remove_chain/frame_4.png");
    private static final Identifier REMOVE_CHAINS_5 = SpiritLeveling.loc("textures/animation/remove_chain/frame_5.png");
    private static final Identifier REMOVE_CHAINS_6 = SpiritLeveling.loc("textures/animation/remove_chain/frame_6.png");
    private static final Identifier REMOVE_CHAINS_7 = SpiritLeveling.loc("textures/animation/remove_chain/frame_7.png");
    private static final Identifier REMOVE_CHAINS_8 = SpiritLeveling.loc("textures/animation/remove_chain/frame_8.png");
    private static final Identifier REMOVE_CHAINS_9 = SpiritLeveling.loc("textures/animation/remove_chain/frame_9.png");
    private static final Identifier REMOVE_CHAINS_10 = SpiritLeveling.loc("textures/animation/remove_chain/frame_10.png");
    private static final Identifier REMOVE_CHAINS_11 = SpiritLeveling.loc("textures/animation/remove_chain/frame_11.png");

    public static final ArrayList<Identifier> COVER_ANIMATION_TO_STRONG_TEXTURES = new ArrayList<>() {
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
    public static final ArrayList<Identifier> COVER_ANIMATION_TO_WEAK_TEXTURES = new ArrayList<>() {
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
    public static final ArrayList<Identifier> COVER_ANIMATION_TO_NONE_TEXTURES = new ArrayList<>() {
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
    public static final ArrayList<Identifier> REMOVE_CHAINS_ANIMATION = new ArrayList<>() {
        {
            add(REMOVE_CHAINS_1);
            add(REMOVE_CHAINS_2);
            add(REMOVE_CHAINS_3);
            add(REMOVE_CHAINS_4);
            add(REMOVE_CHAINS_5);
            add(REMOVE_CHAINS_6);
            add(REMOVE_CHAINS_7);
            add(REMOVE_CHAINS_8);
            add(REMOVE_CHAINS_9);
            add(REMOVE_CHAINS_10);
            add(REMOVE_CHAINS_11);
        }
    };

    public ClientSpiritEnergyManager(@NotNull DrawContext draw_context, int s_level, int max_energy, boolean minorBottleneck, int pos_x, int pos_y) {
        super(s_level, max_energy, minorBottleneck, new ClientMinorSpiritLevelFactory());
        drawContext = draw_context;
        posX = pos_x;
        posY = pos_y;

        // set animator rel positions
        COVER_ANIMATOR.setPosition(pos_x, pos_y);
        CHAINS_ANIMATOR.setPosition(pos_x, pos_y);
    }

    // draw the chains and render the animation over it if needed
    public void drawChains(int spirit_power, int spirit_level) {
        if (spirit_power < spirit_level)
            return;

        // render the animations
        CHAINS_ANIMATOR.render(drawContext);

        int num = getChainNumber();
        int animation_num = 0;

        if (CHAINS_ANIMATOR.getCurrentAnimation() != null)
            animation_num = (CHAINS_ANIMATOR.getCurrentAnimation().getY() + 2) / 10;

        // avoid holes in cover rendering when animation is behind
        if (num < animation_num)
            num = animation_num;

        int relX = -5;
        int relY = -2;
        int chainsHeight = 16;
        int chainsWidth = 20;

        // draw the chains
        for (int i = 0; i < num ; i++) {
            drawContext.drawTexture(
                CHAINS,
                posX + relX, posY + relY + (10 * i),
                0,0,
                chainsWidth, chainsHeight,
                chainsWidth, chainsHeight
            );
        }
    }

    public ClientSpiritEnergyManager drawCovers(int spirit_power, int spirit_level) {
        // render animations
        COVER_ANIMATOR.render(drawContext);

        // don't draw covers when spirit power is too low (cover animation is reset upon spirit level increase)
        if (spirit_power < spirit_level)
            return this;

        // choose the number of covers either based on the number of complete minor levels or based on the position of currently running animation
        int num = getCoversNumber();
        int animation_num = 0;

        if (COVER_ANIMATOR.getCurrentAnimation() != null)
            animation_num = COVER_ANIMATOR.getCurrentAnimation().getY() / 10;

        // avoid holes in cover rendering when animation is behind
        if (num < animation_num)
            num = animation_num;

        int relX = 3;
        int relY = 0;
        int coversHeight = 11;
        int coversWidth = 5;
        Identifier texture = getCoverTexture(COVER_STATE.FULL);

        // draw the covers
        for (int i = 0; i < num; i++) {
            if (i == num -1) texture = getCoverTexture(lastCoverState);

            if (texture != null)
                drawContext.drawTexture(
                        texture,
                        posX + relX, posY + relY + (10 * i),
                        0, 0,
                        coversWidth, coversHeight,
                        coversWidth, coversHeight
                );
        }

        return this;
    }

    public ClientSpiritEnergyManager drawSlots(int spirit_power, int current_energy, int max_energy) {
        int num = getSlotsNumber(spirit_power, current_energy, max_energy);

        if (spirit_power >= SLOT_LIST.size()) num = 6;

        int relX = 3;
        int relY = 0;
        int slotsHeight = 101;
        int slotsWidth = 5;

        // draw the slots
        int offset = (10 - num) * 10;

        drawContext.drawTexture(
                SLOT_LIST.get(spirit_power),
                posX + relX, posY + relY + offset,
                0,0,
                slotsWidth, slotsHeight - offset,
                slotsWidth, slotsHeight
        );

        return this;
    }

    public ClientSpiritEnergyManager drawBar(int spirit_level) {     // TODO ADD MULTIPLE BAR DESIGNS TO REPRESENT EACH MAJOR LEVEL

        int barHeight = 104;
        int barWidth = 11;

        drawContext.drawTexture(ENERGY_BAR, posX, posY, 0, 0, barWidth, barHeight, barWidth, barHeight);

        return this;
    }

    private int getChainNumber() {
        int num_chained = 0;
        for (MinorSpiritLevel l : levels) {
            if (l.getIsChained())
                num_chained++;
        }

        return num_chained;
    }

    private int getCoversNumber() {
        int minor_level = getMinorLevel();
        int num_covers = (10 - minor_level);

        if (minor_level < 10)
            lastCoverState = levels.get(minor_level).state;

        return num_covers;
    }

    public int getSlotsNumber(int spirit_power, int current_energy, int max_energy) {

        int size_slots = MinorSpiritLevel.getLevelSize(spirit_power);

        int num_slots = current_energy / size_slots;

        // for partial covers display with full energy
        if (current_energy >= max_energy && num_slots < 10 && max_energy > 10)
            num_slots++;

        return num_slots;
    }

    // returns null if the cover state is COVER_STATE.NONE
    private Identifier getCoverTexture(COVER_STATE state) {
        if (state == COVER_STATE.FULL) {
            return  COVER_FULL;
        } else if (state == COVER_STATE.STRONG) {
            return COVER_STRONG;
        } else if (state == COVER_STATE.WEAK) {
            return COVER_WEAK;
        }

        return null;
    }
}
