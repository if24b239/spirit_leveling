package com.rain.spiritleveling.client;

import com.rain.spiritleveling.SpiritLeveling;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ClientHUDAnimation {

    private final int ticksPerFrame;
    private final ArrayList<HUDAnimationStruct> animationQueue = new ArrayList<>();

    private float progress = 0;
    private int currentFrame = 0;
    private HUDAnimationStruct currentAnimation = null;

    private ClientHUDAnimation(int ticks_per_frame) {
        ticksPerFrame = ticks_per_frame;
    }

    public static ClientHUDAnimation createClientHUDAnimation(int ticks_per_frame) {
        return new ClientHUDAnimation(ticks_per_frame);
    }

    public void addQueue(HUDAnimationStruct animation) {
        animationQueue.add(animation);
    }

    public void render(DrawContext drawContext) {
        if (currentAnimation == null)
            updateCurrentAnimation();

        if (currentAnimation != null)
            currentAnimation.draw(drawContext, currentFrame);

        increaseProgress();
    }

    // increases current frame in ticksPerFrame intervals and resets the currentAnimation and member variables if currentFrame is bigger than the frames in the animation
    private void increaseProgress() {
        if (currentAnimation == null) return;

        progress++;
        if (progress >= ticksPerFrame)
            currentFrame++;
        progress %= ticksPerFrame;

        if (currentFrame >= currentAnimation.frameTextures.size()) {
            currentAnimation = null;
            currentFrame = 0;
            progress = 0;
        }
    }

    private void updateCurrentAnimation() {
        assert currentAnimation == null;
        if (animationQueue.isEmpty())
            return;

        currentAnimation = animationQueue.remove(0);

    }

    public boolean getIsEmpty() {
        return animationQueue.isEmpty();
    }
}
