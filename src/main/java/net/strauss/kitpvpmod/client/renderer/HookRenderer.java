package net.strauss.kitpvpmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.strauss.kitpvpmod.KitPvpMod;
import net.strauss.kitpvpmod.entity.projectile.HookProjectile;

public class HookRenderer extends EntityRenderer<HookProjectile> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KitPvpMod.MOD_ID, "textures/entity/projectile/none.png");
    public HookRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(HookProjectile entity) {
        return TEXTURE;
    }

    @Override
    public void render(HookProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
    }
}