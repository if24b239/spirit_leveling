package com.rain.spiritleveling.client;

import com.rain.spiritleveling.SpiritLeveling;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class ClientHUDAnimation {
    private boolean isPlaying = false;
    private float progress = 0.0f;
    private int posX = 0;
    private int posY = 0;


    private final int frames;
    private final float duration;
    private final ArrayList<Identifier> FRAME_TEXTURES;
    private final int width;
    private final int height;


    private ClientHUDAnimation(float tickPerFrame, ArrayList<Identifier> frameTextures, int textureWidth, int textureHeight) {
        this.frames = frameTextures.size();
        this.duration = tickPerFrame * frames;
        this.FRAME_TEXTURES = frameTextures;
        this.width = textureWidth;
        this.height = textureHeight;
    }


    public static ClientHUDAnimation createClientHUDAnimation(float tickPerFrame, ArrayList<Identifier> frameTextures, int textureWidth, int textureHeight) {
        return new ClientHUDAnimation(tickPerFrame, frameTextures, textureWidth, textureHeight);
    }

    // start the animation (make sure to call setPosition() before rendering)
    public void trigger() {
        isPlaying = true;
        progress = 0;
    }

    // progress the animation
    private void update() {

        progress += 1f;

        if (progress >= duration) {
            isPlaying = false;
        }
    }

    public void render(DrawContext drawContext) {
        if (!isPlaying) return;

        int currentFrameIndex = Math.min((int)(progress / getTickPerFrame()), frames - 1);


        SpiritLeveling.LOGGER.info("INDEX: {}", currentFrameIndex);

        drawContext.drawTexture( FRAME_TEXTURES.get(currentFrameIndex),
                posX, posY,
                0,0,
                width, height,
                width, height
        );

        update();
    }

    // returns the float value of how many ticks each frame should be displayed
    private float getTickPerFrame() {
        return duration / frames;
    }

    public void setPosition(int x, int y) {
        posX = x;
        posY = y;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }
}
