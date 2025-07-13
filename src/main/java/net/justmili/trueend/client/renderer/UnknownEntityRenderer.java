package net.justmili.trueend.client.renderer;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.entity.Unknown;
import net.justmili.trueend.init.Entities;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TrueEnd.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class UnknownEntityRenderer {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Entities.UNKNOWN.get(), UnknownRenderer::new);
    }

    public static class UnknownRenderer extends MobRenderer<Unknown, HumanoidModel<Unknown>> {
        public UnknownRenderer(EntityRendererProvider.Context context) {
            super(
                    context,
                    new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)),
                    0.5f);
        }

        @Override
        public boolean shouldRender(Unknown entity, Frustum frustum, double camX, double camY, double camZ) {
            return true;
        }

        @Override
        public ResourceLocation getTextureLocation(Unknown entity) {
            // int skin = 0;
            // skin = (int) (Math.random() * 4);
            // return ResourceLocation.parse("true_end:textures/entity/unknown/unknown_"+skin+".png");
            return ResourceLocation.parse("true_end:textures/entity/unknown/unknown_0.png");
        }
    }
}