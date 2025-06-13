package net.justmili.trueend.client;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.entity.UnknownEntity;
import net.justmili.trueend.init.TrueEndEntities;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TrueEnd.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityClient {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TrueEndEntities.UNKNOWN.get(), UnknownRenderer::new);
    }

    public static class UnknownRenderer extends MobRenderer<UnknownEntity, HumanoidModel<UnknownEntity>> {
        public UnknownRenderer(EntityRendererProvider.Context context) {
            super(
                context,
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)),
                0.5f
            );
        }

        @Override
        public ResourceLocation getTextureLocation(UnknownEntity entity) {
            return ResourceLocation.parse("true_end:textures/entity/unknown/unknown_0.png");
        }
    }
}