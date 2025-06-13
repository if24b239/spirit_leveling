package com.rain.spiritleveling.client;

import com.rain.spiritleveling.SpiritLeveling;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class HUDAnimationStruct {
    public final ArrayList<Identifier> frameTextures;
    public final int posX;
    public final int posY;
    public final int textureWidth;
    public final int textureHeight;

    public HUDAnimationStruct(int pos_x, int pos_y, int width, int height, ArrayList<Identifier> textures) {
        frameTextures = textures;
        textureWidth = width;
        textureHeight = height;
        posX = pos_x;
        posY = pos_y;

    }

    public void draw(DrawContext drawContext, int current_frame) {
        drawContext.drawTexture(
                frameTextures.get(current_frame),
                posX, posY,
                0, 0,
                textureWidth, textureHeight,
                textureWidth, textureHeight);
    }
}
