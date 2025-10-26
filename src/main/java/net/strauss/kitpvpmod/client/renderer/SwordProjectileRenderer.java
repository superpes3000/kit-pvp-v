package net.strauss.kitpvpmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.strauss.kitpvpmod.entity.SwordProjectileEntity;

public class SwordProjectileRenderer extends EntityRenderer<SwordProjectileEntity> {

    public SwordProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(SwordProjectileEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        ItemStack stack = entity.getItem();

        if (!stack.isEmpty()) {
            matrixStack.pushPose();

            // Позиция в центре сущности
            matrixStack.translate(0.0D, 0.1D, 0.0D);

            // ✅ 1. Повернуть в сторону направления полёта
            matrixStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-entity.getYRot()));
            matrixStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(entity.getXRot()));

            // ✅ 2. Дополнительная коррекция модели (чтобы клинок был вперёд)
            matrixStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90f));   // повернём чтобы «встал» на ребро
            matrixStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180f));  // чтобы рукоять была сзади

            // ✅ 3. Немного уменьшим, чтобы не казался слишком большим
            matrixStack.scale(0.8f, 0.8f, 0.8f);

            // ✅ 4. Рендер меча
            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    packedLight,
                    net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
                    matrixStack,
                    buffer,
                    entity.getCommandSenderWorld(),
                    0
            );

            matrixStack.popPose();
        }

        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SwordProjectileEntity entity) {
        // Не требуется, так как мы рендерим ItemStack
        return null;
    }
}
