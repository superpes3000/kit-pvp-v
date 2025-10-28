package net.strauss.kitpvpmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.strauss.kitpvpmod.entity.mob.BearHoneyEntity;

public class BearHoneyRenderer extends EntityRenderer<BearHoneyEntity> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("kitpvpmod", "textures/entity/honeycomb.png");

    public BearHoneyRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BearHoneyEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       net.minecraft.client.renderer.MultiBufferSource buffer, int packedLight) {

    }

    @Override
    public ResourceLocation getTextureLocation(BearHoneyEntity entity) {
        return TEXTURE;
    }
}
