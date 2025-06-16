package com.rain.spiritleveling.client.renderer;

import com.rain.spiritleveling.entities.custom.MeditationMatSitEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class MeditationMatSitRenderer extends EntityRenderer<MeditationMatSitEntity>{
    public MeditationMatSitRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(MeditationMatSitEntity entity) {
        return null;
    }

    @Override
    public boolean shouldRender(MeditationMatSitEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}
