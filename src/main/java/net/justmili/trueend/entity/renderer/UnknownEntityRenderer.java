package net.justmili.trueend.entity.renderer;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.entity.Unknown;
import net.justmili.trueend.init.Entities;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;

@Mod.EventBusSubscriber(modid = TrueEnd.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class UnknownEntityRenderer {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Entities.UNKNOWN.get(), UnknownRenderer::new);
    }
    public static class UnknownRenderer extends MobRenderer<Unknown, HumanoidModel<Unknown>> {
        public UnknownRenderer(EntityRendererProvider.Context context) {
            super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
            this.addLayer(new RenderLayer<>(this) {
                @Override
                public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, Unknown entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

                    VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(getTextureLocation(entity)));
                    this.getParentModel().renderToBuffer(poseStack, vertexConsumer, 15728640, LivingEntityRenderer.getOverlayCoords(entity, 0), 1f, 1f, 1f, 1f);
                }
            });
        }

        @Override
        public boolean shouldRender(Unknown entity, Frustum frustum, double camX, double camY, double camZ) {
            return true;
        }

        @Override
        public ResourceLocation getTextureLocation(Unknown entity) {
            return ResourceLocation.parse("true_end:textures/entity/unknown/" + entity.getTextureName() + ".png");
        }
    }
}