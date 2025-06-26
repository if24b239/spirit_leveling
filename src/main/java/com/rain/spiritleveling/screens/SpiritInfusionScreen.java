package com.rain.spiritleveling.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.blocks.entity.MeditationMatEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.ArrayList;

public class SpiritInfusionScreen extends HandledScreen<SpiritInfusionScreenHandler> {

    private static final Identifier TEXTURE = SpiritLeveling.loc("textures/gui/spirit_infusion.png");
    private static final ArrayList<Pair> PROGRESS_POS = new ArrayList<>() {
        {
            add(new Pair(101, 14));
            add(new Pair(119, 60));
            add(new Pair(75, 94));
            add(new Pair(45, 60));
            add(new Pair(48, 14));
        }
    };

    private final ToggleButtonWidget toggleButton;

    public SpiritInfusionScreen(SpiritInfusionScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundWidth = 176;
        backgroundHeight = 191;
        SpiritLeveling.LOGGER.info("{}", handler.getIsReceiving());
        this.toggleButton = new ToggleButtonWidget(0, 0, 12, 12, handler.getIsReceiving());
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int mouseX, int mouseY) {
        assert this.client != null;

        /// SPIRIT ENERGY BAR
        if (mouseX >= this.x + 12 && mouseX <= this.x + 18 && mouseY >= this.y + 4 && mouseY <= this.y + 106) {
            final int level = getLevel(handler.getMaxEnergy());
            final double natural = Math.round((200 *  Math.pow(1.5f, level) / (256 >> level) * 100d)) / 100d;

            this.setTooltip(Tooltip.wrapLines(this.client, Text.translatable("gui.spiritleveling.spirit_infusion.hover_bar", handler.getCurrentEnergy(), handler.getMaxEnergy(), natural)));
        }

        /// WOOD SLOT
        if (isPointWithinBounds(79, 7, 18, 18, mouseX, mouseY) && handler.getSlot(MeditationMatEntity.WOOD_SLOT).getStack().isEmpty()) {
            this.setTooltip(Tooltip.wrapLines(this.client, Text.translatable("gui.spiritleveling.spirit_infusion.wood_slot")));
        }

        /// FIRE SLOT
        if (isPointWithinBounds(121, 38, 18, 18, mouseX, mouseY) && handler.getSlot(MeditationMatEntity.FIRE_SLOT).getStack().isEmpty()) {
            this.setTooltip(Tooltip.wrapLines(this.client, Text.translatable("gui.spiritleveling.spirit_infusion.fire_slot")));
        }

        /// EARTH SLOT
        if (isPointWithinBounds(105, 86, 18, 18, mouseX, mouseY) && handler.getSlot(MeditationMatEntity.EARTH_SLOT).getStack().isEmpty()) {
            this.setTooltip(Tooltip.wrapLines(this.client, Text.translatable("gui.spiritleveling.spirit_infusion.earth_slot")));
        }

        /// METAL SLOT
        if (isPointWithinBounds(53, 86, 18, 18, mouseX, mouseY) && handler.getSlot(MeditationMatEntity.METAL_SLOT).getStack().isEmpty()) {
            this.setTooltip(Tooltip.wrapLines(this.client, Text.translatable("gui.spiritleveling.spirit_infusion.metal_slot")));
        }

        /// WATER SLOT
        if (isPointWithinBounds(37, 38, 18, 18, mouseX, mouseY) && handler.getSlot(MeditationMatEntity.WATER_SLOT).getStack().isEmpty()) {
            this.setTooltip(Tooltip.wrapLines(this.client, Text.translatable("gui.spiritleveling.spirit_infusion.water_slot")));
        }

        super.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        titleY = 1000;
        playerInventoryTitleY = 1000;

        // set button properties
        toggleButton.setPosition(this.x + 28, this.y + 89);
        toggleButton.setTextureUV(176,108,12, 12, TEXTURE);

        if (handler.getIsReceiving()) {
            toggleButton.setTooltip(getToggledTooltip());
        } else {
            toggleButton.setTooltip(getBasicTooltip());
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);

        int entityX = this.x + 88;
        int entityY = this.y + 78;

        assert this.client != null;
        assert this.client.player != null;
        drawEntity(context, entityX, entityY, 20, entityX - mouseX, entityY - 20 - mouseY, this.client.player);

        // draw the visualization of crafting progress
        drawProgress(context);

        // draw the current and max spirit energy
        drawEnergyBar(context);

        // draw and update button
        drawToggleButton(context, mouseX, mouseY, delta);
    }

    private void drawToggleButton(DrawContext context, int mouseX, int mouseY, float delta) {
        toggleButton.setToggled(handler.getIsReceiving());
        if (handler.getIsReceiving()) {
            toggleButton.setTooltip(getToggledTooltip());
        } else {
            toggleButton.setTooltip(getBasicTooltip());
        }
        toggleButton.render(context, mouseX, mouseY, delta);
    }

    /// calls the C2S packet to change the isReceiving state of the blockEntity
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.toggleButton.mouseClicked(mouseX, mouseY, button)) {
            handler.flipIsReceiving();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private Tooltip getBasicTooltip() {
        int mat_level = getLevel(handler.getMaxEnergy());
        double value = (int) Math.pow(2.2f, mat_level);
        value /= 1.25;

        return Tooltip.of(Text.translatable("gui.spiritleveling.spirit_infusion.hover_button", value));
    }

    private Tooltip getToggledTooltip() {
        int player_power = handler.getPassengerPower();
        double value = (int) Math.pow(2.2f, player_power);
        value /= 1.25;

        return Tooltip.of(Text.translatable("gui.spiritleveling.spirit_infusion.hover_button.toggled", value));
    }

    private void drawProgress(DrawContext context) {
        double progress = handler.getRelativeProgress();

        if (progress > 1)
            throw new IllegalStateException("Spirit Infusion crafting progress should never be above 1");

        // get the number of fully filled bars and the ratio of the last not filled one
        progress *= 5;

        // draw all already filled arrow textures
        for (int i = 0; i < (int) progress; i++) {
            context.drawTexture(TEXTURE,
                    PROGRESS_POS.get(i).x + x, PROGRESS_POS.get(i).y + y,
                    176, i * 22,
                    27, 22);
        }

        // draw texture progressing in the right direction
        drawCurrentProgressBar(context, progress);
    }

    private void drawEnergyBar(DrawContext context) {

        int displayLevel = getLevel(handler.getCurrentEnergy());
        int maxLevel = getLevel(handler.getMaxEnergy());
        int displayBarCount = (int) (handler.getCurrentEnergy() / Math.pow(10, displayLevel));
        int displayCoversCount = (displayLevel == maxLevel) ? 10 - (int) (handler.getMaxEnergy() / Math.pow(10, maxLevel)) : 0;

        for (int i = 0; i < displayBarCount; i++) {
            context.drawTexture(TEXTURE,
                    x + 14, y + 96 - i * 10,
                    displayLevel * 3,191,
                    3, 9);
        }

        for (int i = 0; i < displayCoversCount; i++) {
            context.drawTexture(TEXTURE,
                    x + 13, y + 5 + i * 10,
                    21,191,
                    5, 11);
        }
    }

    private int getLevel(int energy) {
        return (int) Math.log10(Math.max(energy - 1, 1));
    }

    private void drawCurrentProgressBar(DrawContext context, double progress) {
        final int alreadyFilledBars = (int) progress;
        int u = 176;
        int v = alreadyFilledBars * 22;
        int width;
        int height;
        int posX = PROGRESS_POS.get(alreadyFilledBars).x + x;
        int posY = PROGRESS_POS.get(alreadyFilledBars).y + y;

        switch (alreadyFilledBars) {
            case 0, 4 -> {
                height = 20;
                width = (int) (Math.ceil((progress - (int) progress) * 27));
            }
            case 1 -> {
                width = 12;
                height = (int) (Math.ceil((progress - (int) progress) * 22));
            }
            case 2 -> {
                width = (int) (Math.ceil((progress - (int) progress) * 26));
                height = 5;
                u += 26 - width;
                posX += 26 - width;
            }
            case 3 -> {
                width = 12;
                height = (int) (Math.ceil((progress - (int) progress) * 22));
                v += 22 - height;
                posY += 22 - height;
            }
            case 5 -> {
                width = 0;
                height = 0;
            }
            default -> throw new IllegalStateException("Somehow more than 5 bars have been filled");
        }

        context.drawTexture(TEXTURE,
                posX, posY,
                u, v,
                width, height);

    }

    public static void drawEntity(DrawContext context, int x, int y, int size, float mouseX, float mouseY, LivingEntity entity) {
        float f = (float)Math.atan(mouseX / 40.0F);
        float g = (float)Math.atan(mouseY / 40.0F);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateX(g * 20.0F * (float) (Math.PI / 180.0));
        quaternionf.mul(quaternionf2);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = 180.0F + f * 20.0F;
        entity.setYaw(180.0F + f * 40.0F);
        entity.setPitch(-g * 20.0F);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        drawEntity(context, x, y, size, quaternionf, quaternionf2, entity);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
    }

    @SuppressWarnings("deprecation")
    public static void drawEntity(DrawContext context, int x, int y, int size, Quaternionf quaternionf, @Nullable Quaternionf quaternionf2, LivingEntity entity) {
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 50.0);
        context.getMatrices().multiplyPositionMatrix(new Matrix4f().scaling(size, size, -size));
        context.getMatrices().multiply(quaternionf);
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        if (quaternionf2 != null) {
            quaternionf2.conjugate();
            entityRenderDispatcher.setRotation(quaternionf2);
        }

        entityRenderDispatcher.setRenderShadows(false);
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, context.getMatrices(), context.getVertexConsumers(), 15728880));
        context.draw();
        entityRenderDispatcher.setRenderShadows(true);
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }

}

class Pair {
    int x;
    int y;

    public Pair(int first, int second) {
        x = first;
        y = second;
    }
}


