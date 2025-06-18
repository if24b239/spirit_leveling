package com.rain.spiritleveling.client.hud;

import net.minecraft.client.gui.DrawContext;
import java.util.ArrayList;

public class ClientHUDAnimator {

    private final int ticksPerFrame;
    private final ArrayList<HUDAnimationStruct> animationQueue = new ArrayList<>();
    private int posX = 0;
    private int posY = 0;

    private float progress = 0;
    private int currentFrame = 0;
    private HUDAnimationStruct currentAnimation = null;

    private ClientHUDAnimator(int ticks_per_frame) {
        ticksPerFrame = ticks_per_frame;
    }

    public static ClientHUDAnimator createClientHUDAnimation(int ticks_per_frame) {
        return new ClientHUDAnimator(ticks_per_frame);
    }

    public void addQueue(HUDAnimationStruct animation) {
        animationQueue.add(animation);
    }

    // draw the animation with relative position of the animator
    public void render(DrawContext drawContext) {
        if (currentAnimation == null)
            updateCurrentAnimation();

        if (currentAnimation != null)
            currentAnimation.draw(drawContext, currentFrame, posX, posY);

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

    public void setPosition(int pos_x, int pos_y) {
        posX = pos_x;
        posY = pos_y;
    }

    public HUDAnimationStruct getCurrentAnimation() {
        return currentAnimation;
    }

    // instantly stops all animations
    public void clearQueue() {
        animationQueue.clear();
        currentAnimation = null;
    }
}
