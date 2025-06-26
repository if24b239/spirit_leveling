package com.rain.spiritleveling.client;

import com.rain.spiritleveling.client.renderer.MeditationMatSitRenderer;
import com.rain.spiritleveling.entities.AllEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class AllRenderers {

    public static void initialize() {

        EntityRendererRegistry.register(AllEntities.MAT_SIT_ENTITY, MeditationMatSitRenderer::new);
    }
}
